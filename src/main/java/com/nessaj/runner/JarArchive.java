package com.nessaj.runner;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author keming
 * @Date 2022/03/25 22:22
 */
public class JarArchive {

    private final JarFile jarFile;

    public JarArchive(String path) throws IOException {
        this(new File(path));
    }

    public JarArchive(File file) throws IOException {
        this(new JarFile(file));
    }

    public JarArchive(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public ClassLoader loadJarClass(ClassLoader classLoader) throws ClassNotFoundException {
        Enumeration<JarEntry> entries = jarFile.entries();
//        System.out.println("======================" + jarFile.getName());
//        while (entries.hasMoreElements()) {
//            JarEntry jarEntry = entries.nextElement();
//            String jarPathName = jarEntry.getName();
//            // ignore module-info.class
//            if ((jarPathName.endsWith("module-info.class")) || jarPathName.contains("-")) {
//                continue;
//            }
////            if (jarPathName != null && jarPathName.endsWith(".class")) {
////                System.out.println("------->>>" + jarPathName);
////                classLoader.loadClass(jarPathName.replace("/", ".").substring(0, jarPathName.length() - 6));
////            }
//            if (jarFile.getName().equals("E:\\etc\\demo1\\WEB-INF\\lib\\spring-boot-2.6.3.jar")) {
////                System.out.println(jarEntry.getName());
//                System.out.println(jarPathName);
//            }
//
//        }
        if (jarFile.getName().equals("E:\\etc\\demo1\\WEB-INF\\lib\\spring-boot-2.6.3.jar")) {
            try {
                iterator(jarFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classLoader;
    }

    private void iterator(JarFile jarFile) throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String jarPathName = jarEntry.getName();
            if (jarPathName.endsWith(".factories")) {
                jarFile.getInputStream(jarEntry);
                System.out.println("jarPathName:" + jarPathName);
            }
        }
    }

    @FunctionalInterface
    public interface FileFilter {
        boolean matches(File file);
    }

}
