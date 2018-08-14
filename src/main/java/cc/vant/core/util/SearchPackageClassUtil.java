package cc.vant.core.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 给定包名,搜索指定包下所有的类
 *
 * @author Vant
 * @version 2018/8/2 上午 7:48
 */
public class SearchPackageClassUtil {
    /**
     * @return 包下的所有类名, 这些类名是互不重复的
     */
    public static String[] searchPackageClass(String packageName) {
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> resources = null;

        try {
            resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!resources.hasMoreElements()) {
            throw new IllegalArgumentException("找不到此包");
        }

        Set<String> classNames = new HashSet<>();
        do {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                String filePath = null;
                try {
                    filePath = URLDecoder.decode(url.getFile(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                File file = new File(filePath);
                searchByClass0(file, packageName, classNames);
            } else if ("jar".equals(protocol)) {
                try {
                    JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();

                        if (jarEntry.getName().startsWith(packageDirName)) {
                            if (jarEntry.getName().endsWith(".class")) {
                                classNames.add(jarEntry.getName().replace('/', '.'));
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } while (resources.hasMoreElements());

        String[] names = classNames.toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].substring(0, names[i].length() - 6);
        }
        return names;
    }


    /**
     * 也许可以用多线程加速
     */
    private static void searchByClass0(File file, String prefix, Set<String> result) {
        File[] files = file.listFiles();
        for (File tmpFile : files) {
            if (tmpFile.getName().endsWith(".class")) {
                result.add(prefix + '.' + tmpFile.getName());
            } else if (tmpFile.isDirectory()) {
                searchByClass0(tmpFile, prefix + '.' + tmpFile.getName(), result);
            }
        }
    }
}