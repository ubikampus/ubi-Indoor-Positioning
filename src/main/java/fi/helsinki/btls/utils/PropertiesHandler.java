package fi.helsinki.btls.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesHandler {
    private Properties properties;
    private String file;

    public PropertiesHandler() {
        properties = new Properties();
    }

    public PropertiesHandler(String filename) {
        properties = new Properties();
        loadProperties(filename);
    }

    public void openPropertiesFile(String filename) {
        file = filename;
    }

    public void loadProperties() {
        try {
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException("Properties file doesn't exists!");
        }
    }

    public void loadProperties(String filename) {
        openPropertiesFile(filename);
        loadProperties();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Map<String, String> getAllProperties() {
        return properties.entrySet().stream().collect(Collectors.toMap(c -> c.getKey().toString(), c -> c.getValue().toString(), (a, b) -> b));
    }

    public void saveProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void persistProperties() {
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (Exception e) {
            throw new RuntimeException("Properties file doesn't exists!");
        }
    }

    public void closePropertiesFile() {
        persistProperties();
        file = null;
        properties.clear();
    }
}
