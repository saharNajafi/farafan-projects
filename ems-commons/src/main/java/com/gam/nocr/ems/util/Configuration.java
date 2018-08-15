package com.gam.nocr.ems.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class Configuration {
    static private Properties configuration = null;

    public static void load() throws IOException {
        configuration = readFile();
    }

    public static Properties getProperties() {
        getInstance();
        return configuration;
    }

    public static String getProperty(String key) {
        if (key == null){
            throw new IllegalArgumentException("Key is Empty!");
        }
        return getInstance().getProperty(key);
    }

    public static String getProperty(String key, String... args) {
        return MessageFormat.format(getProperty(key), args);
    }

    private static Properties getInstance() {
        if (configuration == null) {
            try {
                configuration = readFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return configuration;
    }

    public static String getPropertyWithDefault(String key, String defaultValue) {
        return getInstance().getProperty(key, defaultValue);
    }

    private static Properties readFile() throws IOException {

        Properties props = new Properties();
        try {
            ClassLoader loader = Configuration.class.getClassLoader();
            InputStream in = loader.getResourceAsStream("ccos-payment.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return props;
    }
}