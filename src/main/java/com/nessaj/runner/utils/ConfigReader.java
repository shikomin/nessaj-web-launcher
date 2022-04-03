package com.nessaj.runner.utils;

import com.nessaj.runner.Launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author keming
 * @Date 2022/04/03 13:57
 */
public class ConfigReader {

    private static ConfigReader defaultConfigReader;
    private Properties properties;

    private ConfigReader() {
        this.properties = new Properties();
        InputStream inputStream = Launcher.class.getResourceAsStream("/launcher.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigReader getDefaultConfigReader() {
        if (defaultConfigReader == null) {
            synchronized (ConfigReader.class) {
                if (defaultConfigReader == null) {
                    defaultConfigReader = new ConfigReader();
                }
            }
        }
        return defaultConfigReader;
    }

    public Properties getProperties() {
        return this.properties;
    }

}
