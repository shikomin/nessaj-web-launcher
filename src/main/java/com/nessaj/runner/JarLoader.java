package com.nessaj.runner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author keming
 * @Date 2022/03/28 18:13
 */
public class JarLoader {

    private static final String WEB_INF = "WEB-INF";

    private static final String META_INF = "META-INF";

    private static final String CLASSES_DIR = "/classes/";

    private static final String LIB_DIR = "/lib";

    private final File root;

    private File web_inf;

    private List<JarArchive> jarArchives;

    private List<URL> urls;


    public JarLoader(String path) {
        this(new File(path));
    }

    protected JarLoader(File root) {
        this.root = root;
    }

    public List<JarArchive> loadJarFiles() throws IOException {
        JarArchive.FileFilter fileFilter = this::webInfFilter;
        web_inf = searchWebInf(fileFilter);
        File lib = new File(web_inf.getAbsolutePath() + LIB_DIR);
        File[] jars = lib.listFiles();
        int initSize = guessSize(jars);
        jarArchives = new ArrayList<>(initSize);
        urls = new ArrayList<>(initSize);
        int length = jars.length;
        for (int i = 0; i < length; i++) {
            if (jars[i].getName().endsWith(".jar")) {
                jarArchives.add(new JarArchive(jars[i]));
                urls.add(new URL("jar:file:" + jars[i].getAbsolutePath() + "!/"));
            }
        }
        return jarArchives;
    }

    public URL[] generateUrls() throws MalformedURLException {
        urls.add(new URL("file:" + web_inf.getAbsolutePath() + CLASSES_DIR));
        return urls.toArray(new URL[0]);
    }

    private URL[] geneClassURLs(File path, List<URL> urls) throws MalformedURLException {
        File[] files = path.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                geneClassURLs(files[i], urls);
            } else if (files[i].getName().endsWith(".class")) {
                urls.add(new URL("file:" + files[i].getAbsolutePath()));
            }
        }
        return urls.toArray(new URL[0]);
    }

    public File getWeb_inf() {
        if (web_inf == null) {
            JarArchive.FileFilter fileFilter = this::webInfFilter;
            return searchWebInf(fileFilter);
        }
        return web_inf;
    }

    private int guessSize(File[] jars) {
        return jars != null ? jars.length + 10 : 10;
    }

    private File searchWebInf(JarArchive.FileFilter fileFilter) {
        if (!root.isDirectory()) {
            return null;
        }
        File[] subFiles = root.listFiles();
        int length = subFiles.length;
        for (int i = 0; i < length; i++) {
            if (fileFilter.matches(subFiles[i])) {
                return subFiles[i];
            }
        }
        return null;
    }

    private boolean webInfFilter(File file) {
        return file.isDirectory() && file.getName().equals(WEB_INF);
    }


}
