package com.nessaj.runner.utils;

import com.nessaj.runner.Launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author keming
 * @Date 2022/04/03 13:57
 */
public class ConfigReader {

    private static final String DEFAULT_PATH = "/launcher.properties";
    private static ConfigReader configReader;
    private Properties properties;

    private ConfigReader() {
        this(Launcher.class.getResourceAsStream(DEFAULT_PATH));
    }

    private ConfigReader(String path) {
        this(readPath(path));
    }

    private ConfigReader(InputStream inputStream) {
        this.properties = new Properties();
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStream readPath(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static ConfigReader getConfigReader() {
        if (configReader == null) {
            synchronized (ConfigReader.class) {
                if (configReader == null) {
                    configReader = new ConfigReader();
                }
            }
        }
        return configReader;
    }

    public static ConfigReader getConfigReader(String path) {
        if (configReader == null) {
            synchronized (ConfigReader.class) {
                if (configReader == null) {
                    configReader = new ConfigReader(path);
                }
            }
        }
        return configReader;
    }

    public Properties getProperties() {
        return this.properties;
    }

}
