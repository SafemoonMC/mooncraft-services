package gg.mooncraft.services.minecraft.bungee.database;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.Credentials;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class BungeeDatabaseUtilities {
    @NotNull
    public static Credentials fromBukkitConfig(@NotNull Configuration configuration) {
        String username = configuration.getString("username", "username");
        String password = configuration.getString("password", "password");
        String database = configuration.getString("database", "database");
        String hostname = configuration.getString("hostname", "localhost");
        String port = configuration.getString("port", "3306");
        
        int maximumPoolSize = configuration.getInt("pool-settings.maximum-pool-size", 10);
        int minimumIdle = configuration.getInt("pool-settings.maximum-idle", 10);
        int maximumLifetime = configuration.getInt("pool-settings.maximum-lifetime", 1800000);
        int keepaliveTime = configuration.getInt("pool-settings.keepalive-time", 0);
        int connectionTimeout = configuration.getInt("pool-settings.connection-timeout", 5000);
        
        Map<String, String> propertiesMap = new HashMap<>();
        if (configuration.contains("pool-settings.properties")) {
            for (String key : configuration.getSection("pool-settings.properties").getKeys()) {
                propertiesMap.put(key, configuration.getString("pool-settings.properties." + key));
            }
        }
        
        return new Credentials(hostname, port, database, username, password, maximumPoolSize, minimumIdle, maximumLifetime, keepaliveTime, connectionTimeout, propertiesMap);
    }
}