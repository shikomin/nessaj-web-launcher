package com.nessaj.runner;


import com.nessaj.runner.exception.MainClassNotFound;
import com.nessaj.runner.utils.ConfigReader;
import com.nessaj.runner.utils.FilePathHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author keming
 * @Date 2022/03/28 20:16
 */
public class Launcher {

    private static final String LAUNCH_PATH_IN_PROPERTIES = "launch.path";
    private final String localDirectory;
    private List<JarArchive> jarArchives;
    private URL[] urls;

    public Launcher(String localDirectory) {
        this.localDirectory = localDirectory;
    }

    public void launch(String[] args) {
        JarLoader jarLoader = new JarLoader(localDirectory);
        try {
            jarArchives = jarLoader.loadJarFiles();
            urls = jarLoader.generateUrls();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(urls));
        ClassLoader classLoader = createClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        MainClassRunner runner = new MainClassRunner(localDirectory, args);
        try {
            runner.run();
        } catch (MainClassNotFound | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

    /**
     * @deprecated
     */
    @Deprecated
    private ClassLoader loadClassFromJarFile(ClassLoader classLoader) throws ClassNotFoundException {
        int size = jarArchives.size();
        for (int i = 0; i < size; i++) {
            jarArchives.get(i).loadJarClass(classLoader);
        }
        return classLoader;
    }

    /**
     * @deprecated
     */
    @Deprecated
    private ClassLoader loadClassFromClasses(ClassLoader classLoader) throws ClassNotFoundException {
        File classes = getClassesPath();
        loadClass(classes, classLoader);
        return classLoader;
    }

    private void loadClass(File path, ClassLoader classLoader) throws ClassNotFoundException {
        File[] files = path.listFiles();
        int size = files.length;
        for (int i = 0; i < size; i++) {
            if (files[i].isDirectory()) {
                loadClass(files[i], classLoader);
            } else if (files[i].getAbsolutePath().endsWith(".class")) {
                classLoader.loadClass(FilePathHandler.geneRegularClassContext(files[i].getAbsolutePath()));
            }
        }
    }

    private File getClassesPath() {
        int size = urls.length;
        for (int i = 0; i < size; i++) {
            String filePath = urls[i].getPath();
            System.out.println("filepath:" + filePath);
            if (filePath.endsWith("classes/")) {
                return new File(filePath);
            }
        }
        return null;
    }

    private ClassLoader createClassLoader() {
        return new URLClassLoader(urls);
    }

    public static void main(String[] args) {
        Properties properties = ConfigReader.getDefaultConfigReader().getProperties();
        Launcher launcher = new Launcher(properties.get(LAUNCH_PATH_IN_PROPERTIES).toString());
        launcher.launch(args);
    }

}
