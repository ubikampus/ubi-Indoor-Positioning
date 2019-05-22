package fi.helsinki.btls.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * CLass to handle accessing propertiess files and for storing properties as key-value pairs.
 */
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

    /**
     * Initialize object with filename.
     *
     * @param filename filename of the properties file.
     */
    public void openPropertiesFile(String filename) {
        file = filename;
    }

    /**
     * Loads all the properties from the file declared earlier or throws exception if not success.
     */
    public void loadProperties() {
        try {
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException("Properties file doesn't exists!");
        }
    }

    /**
     * Loads propertiess from declared file or gives exception if failed.
     *
     * @param filename name of the properties file.
     */
    public void loadProperties(String filename) {
        openPropertiesFile(filename);
        loadProperties();
    }

    /**
     * Get specific property from file loaeded.
     *
     * @param key key of the property.
     *
     * @return value that match key.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get all properties.
     *
     * @return map of properties as key-value pair.
     */
    public Map<String, String> getAllProperties() {
        return properties.entrySet().stream().collect(Collectors.toMap(c -> c.getKey().toString(), c -> c.getValue().toString(), (a, b) -> b));
    }

    /**
     * Save property.
     *
     * @param key key of the property.
     * @param value value of the property.
     */
    public void saveProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Persist data of the properties file that was added through save method and throws exception if not possible.
     *
     * @see PropertiesHandler#saveProperty(String, String)
     */
    public void persistProperties() {
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (Exception e) {
            throw new RuntimeException("Properties file doesn't exists!");
        }
    }

    /**
     * Saves changes in properties file and then closes + forgets it and throws exception if failed.
     */
    public void closePropertiesFile() {
        persistProperties();
        file = null;
        properties.clear();
    }
}
