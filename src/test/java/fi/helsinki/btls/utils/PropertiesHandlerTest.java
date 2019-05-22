package fi.helsinki.btls.utils;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.io.*;
import java.util.Map;

/**
 * Tests for properties file handler class.
 */
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

    @Test
    public void cantLoadPropertiesFromFileThatDoesntExists() {
        PropertiesHandler handler = new PropertiesHandler();
        String key = "key";
        String value = "value";

        insertConfig(key, value);

        try {
            handler.loadProperties("ThisShouldntBe.properties");
            fail();
        } catch (Exception e) {
            assertEquals("Properties file doesn't exists!", e.getMessage());
        }
    }

    @Test
    public void canSaveNewProperty() {
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 5; i++) {
            insertConfig(key + i, value + i);
        }

        PropertiesHandler handler = new PropertiesHandler(tempConfig.getAbsolutePath());
        Map<String, String> allProperties = handler.getAllProperties();
        assertEquals(5, allProperties.size());

        handler.saveProperty("newOne", "6th");
        Map<String, String> updated = handler.getAllProperties();
        assertEquals(6, updated.size());
        assertEquals("6th", updated.get("newOne"));
    }

    @Test
    public void canSaveMultipleNewProperty() {
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 5; i++) {
            insertConfig(key + i, value + i);
        }

        PropertiesHandler handler = new PropertiesHandler(tempConfig.getAbsolutePath());
        Map<String, String> allProperties = handler.getAllProperties();
        assertEquals(5, allProperties.size());

        handler.saveProperty("newOne", "6th");
        handler.saveProperty("newOne2", "7th");
        handler.saveProperty("newOne3", "8th");

        Map<String, String> updated = handler.getAllProperties();
        assertEquals(8, updated.size());
        assertEquals("6th", updated.get("newOne"));
        assertEquals("8th", updated.get("newOne3"));
    }

    @Test
    public void saveDoesntPersistProperty() {
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 5; i++) {
            insertConfig(key + i, value + i);
        }

        PropertiesHandler handler = new PropertiesHandler(tempConfig.getAbsolutePath());
        Map<String, String> allProperties = handler.getAllProperties();
        assertEquals(5, allProperties.size());

        handler.saveProperty("newOne", "6th");
        handler.saveProperty("lel", "3th");

        checkFileSize(5);
    }

    @Test
    public void newOnesCanBePersist() {
        String key = "key";
        String value = "value";

        for (int i = 1; i <= 5; i++) {
            insertConfig(key + i, value + i);
        }

        PropertiesHandler handler = new PropertiesHandler(tempConfig.getAbsolutePath());
        Map<String, String> allProperties = handler.getAllProperties();
        assertEquals(5, allProperties.size());

        handler.saveProperty("newOne", "6th");
        handler.saveProperty("lel", "3th");
        handler.persistProperties();

        checkFileSize(7 + 1);
    }

    @Test
    public void cantPersistToFileThatDoesntExists() {
        PropertiesHandler handler = new PropertiesHandler();
        handler.saveProperty("hah", "hah");
        handler.saveProperty("notWorking", "notWorking");

        try {
            handler.persistProperties();
            fail();
        } catch (Exception e) {
            assertEquals("Properties file doesn't exists!", e.getMessage());
        }
    }

    @Test
    public void cantClosePropertiesFile() {
        PropertiesHandler handler = new PropertiesHandler(tempConfig.getAbsolutePath());
        handler.saveProperty("hah", "hah");
        handler.saveProperty("notWorking", "notWorking");

        handler.closePropertiesFile();
        checkFileSize(2 + 1);

        assertEquals(0, handler.getAllProperties().size());
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

    private void checkFileSize(int shouldBe) {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tempConfig.getAbsolutePath()));
            while (reader.readLine() != null) {
                lines++;
            }
            reader.close();
        } catch (Exception e) {
            fail();
        }

        assertEquals(shouldBe, lines);
    }
}
