package com.nessaj.runner;

import com.nessaj.runner.exception.MainClassNotFound;
import com.nessaj.runner.utils.FilePathHandler;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author keming
 * @Date 2022/04/02 19:22
 */
public class MainClassRunner implements Runner {

    private final File path;
    private final String[] args;
    private Class<?> mainClass;
    private ClassLoader currentClassLoader;

    public MainClassRunner(String path, String[] args) {
        this(new File(path), args);
    }

    public MainClassRunner(File path, String[] args) {
        this.path = path;
        this.args = args;
        this.mainClass = null;
        this.currentClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void run() throws MainClassNotFound, InvocationTargetException, IllegalAccessException {
        findMainClass(path);
        Assert.classNotNull(mainClass, "can not find the springboot application in this path:" + path);
        Class<?> clazz = mainClass;
        this.runMainMethod(clazz);
    }

    public void runMainMethod(Class<?> targetClass) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = targetClass.getMethods();
        Method targetMethod = null;
        for (int i = 0; i < methods.length; i++) {
            if (isMainMethod(methods[i])) {
                targetMethod = methods[i];
                break;
            }
        }
        targetMethod.invoke(null, (Object) args);
    }


    private void findMainClass(File path) throws MainClassNotFound {
        File[] files = path.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                findMainClass(files[i]);
            } else if (files[i].getAbsolutePath().endsWith(".class")) {
                loadClass(files[i].getAbsolutePath());
            }
        }
    }

    private void loadClass(String absolutePath) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(FilePathHandler.geneRegularClassContext(absolutePath), true, currentClassLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isMainClass(clazz)) {
            mainClass = clazz;
        }
    }

    private boolean isMainClass(Class<?> candidate) {
        Annotation[] annotations = candidate.getDeclaredAnnotations();
        int size = annotations.length;
        for (int i = 0; i < size; i++) {
            if (annotations[i].annotationType().getName().equals("org.springframework.boot.autoconfigure.SpringBootApplication")) {
                return true;
            }
        }
        return false;
    }

    public boolean isMainMethod(Method method) {
        int modifiers = method.getModifiers();
        if (method.getName().equals("main") && Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
            return true;
        return false;
    }

}
