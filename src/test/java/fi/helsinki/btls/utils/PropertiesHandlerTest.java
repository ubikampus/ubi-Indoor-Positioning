package fi.helsinki.btls.utils;

import java.io.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.util.Scanner;

import static org.junit.Assert.*;

public class PropertiesHandlerTest {
    private File tempConfig;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        try {
            this.tempConfig = tempFolder.newFile("test.properties");
            this.tempConfig.setWritable(true);
        } catch (IOException ex) {
            throw new AssertionError("creating file failed!");
        }
    }

    @Test
    public void canOpenAndReadProperty() {
        PropertiesHandler handler = new PropertiesHandler();
        String key = "key";
        String value = "value";

        insertConfig(key, value);

        handler.loadProperties(tempConfig.getAbsolutePath());
        assertEquals(value, handler.getProperty(key));
    }

    @Test
    public void canOpenAndReadMultipleProperties() {
        PropertiesHandler handler = new PropertiesHandler();
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 10; i++) {
            insertConfig(key + i, value + i);
        }

        handler.loadProperties(tempConfig.getAbsolutePath());
        assertEquals((value + "1"), handler.getProperty(key + "1"));
        assertEquals((value + "7"), handler.getProperty(key + "7"));
        assertEquals((value + "10"), handler.getProperty(key + "10"));
        assertEquals((value + "4"), handler.getProperty(key + "4"));
    }

    @Test
    public void canOpenAndReadAllProperties() {
        PropertiesHandler handler = new PropertiesHandler();
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 4; i++) {
            insertConfig(key + i, value + i);
        }

        handler.loadProperties(tempConfig.getAbsolutePath());
        Map<String, String> allProperties = handler.getAllProperties();

        assertEquals(4, allProperties.size());
        assertEquals((value + "1"), allProperties.get(key + "1"));
        assertEquals((value + "3"), allProperties.get(key + "3"));
    }

    @After
    public void restore() {
        if (tempConfig != null) {
            this.tempConfig.delete();
        }
    }

    private void insertConfig(String key, String value) {
        FileWriter writer;

        try {
            writer = new FileWriter(this.tempConfig, true);
            writer.write(key + "=" + value + "\n");
            writer.close();
        } catch (IOException ex) {
            throw new AssertionError("writing to file failed!");
        }
    }
}