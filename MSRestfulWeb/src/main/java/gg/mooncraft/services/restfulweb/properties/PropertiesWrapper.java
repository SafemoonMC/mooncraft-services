package gg.mooncraft.services.restfulweb.properties;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesWrapper extends Properties {
    
    /*
    Constructor
     */
    public PropertiesWrapper() {
        super();
    }
    
    public PropertiesWrapper(@NotNull File propertiesFile) throws FileNotFoundException {
        super();
        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            load(inputStream);
        } catch (Exception e) {
            throw new FileNotFoundException("Property file '" + propertiesFile.getName() + "' not found.");
        }
    }
    
    public PropertiesWrapper(@NotNull String resourcesPath) throws FileNotFoundException {
        super();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcesPath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file '" + resourcesPath + "' not found.");
            }
            load(inputStream);
        } catch (Exception e) {
            throw new FileNotFoundException("Property file '" + resourcesPath + "' not found.");
        }
    }
    
    /*
    Methods
     */
    public void putAll(@NotNull Properties properties, @NotNull String section) {
        properties.forEach((k, v) -> {
            if (!(k instanceof String key)) {
                throw new IllegalArgumentException("The properties instance doesn't have only string keys.");
            }
            put(section + "." + key, v);
        });
    }
    
    public int getPropertyInteger(@NotNull String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty() || value.trim().isBlank()) return defaultValue;
        return Integer.parseInt(value.trim());
    }
}