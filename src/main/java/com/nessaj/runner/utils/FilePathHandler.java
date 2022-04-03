package com.nessaj.runner.utils;

import com.nessaj.runner.Launcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author keming
 * @Date 2022/04/02 21:14
 */
public class FilePathHandler {

    public static String getLocation(Class<?> clazz) {
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = null;
        try {
            location = codeSource != null ? codeSource.getLocation().toURI() : null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return location != null ? location.getSchemeSpecificPart() : null;
    }

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
