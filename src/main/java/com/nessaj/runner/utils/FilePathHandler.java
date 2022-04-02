package com.nessaj.runner.utils;

/**
 * @author keming
 * @Date 2022/04/02 21:14
 */
public class FilePathHandler {

    public static String geneRegularClassContext(String path) {
        String classPath = path.substring(path.lastIndexOf("classes") + 8, path.length() - 6);
        if (classPath.contains("/")) {
            return classPath.replace("/", ".");
        } else if (classPath.contains("\\")) {
            return classPath.replace("\\", ".");
        }
        return classPath;
    }

}
