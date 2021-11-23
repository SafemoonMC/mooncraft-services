package gg.mooncraft.services.restfulweb.utilities;

import gg.mooncraft.services.restfulweb.properties.PropertiesWrapper;
import me.eduardwayland.mooncraft.waylander.database.Credentials;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MySQLUtilities {
    
    @NotNull
    public static Credentials fromProperties(@NotNull PropertiesWrapper propertiesWrapper) {
        String username = propertiesWrapper.getProperty("mysql.username");
        String password = propertiesWrapper.getProperty("mysql.password");
        String hostname = propertiesWrapper.getProperty("mysql.hostname");
        String port = propertiesWrapper.getProperty("mysql.port", "3306");
        
        int maximumPoolSize = propertiesWrapper.getPropertyInteger("mysql.pool-settings.maximum-pool-size", 10);
        int minimumIdle = propertiesWrapper.getPropertyInteger("mysql.pool-settings.minimum-idle", 10);
        int maximumLifetime = propertiesWrapper.getPropertyInteger("mysql.pool-settings.maximum-lifetime", 1800000);
        int keepaliveTime = propertiesWrapper.getPropertyInteger("mysql.pool-settings.keepalive-time", 0);
        int connectionTimeout = propertiesWrapper.getPropertyInteger("mysql.pool-settings.connection-timeout", 5000);
        
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesWrapper.stringPropertyNames().stream().filter(key -> key.startsWith("mysql.pool-settings.properties")).forEach(key -> {
            String value = propertiesWrapper.getProperty(key);
            if (value == null) return;
            propertiesMap.put(key.replaceAll("mysql.pool-settings.properties.", ""), value);
        });
        return new Credentials(hostname, port, "", username, password, maximumPoolSize, minimumIdle, maximumLifetime, keepaliveTime, connectionTimeout, propertiesMap);
    }
}