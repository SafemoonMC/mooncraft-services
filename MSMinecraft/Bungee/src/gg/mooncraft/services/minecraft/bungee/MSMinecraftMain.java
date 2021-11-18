package gg.mooncraft.services.minecraft.bungee;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.mooncraft.services.minecraft.bungee.api.ApiRegistrationUtility;
import gg.mooncraft.services.minecraft.bungee.api.impl.MSMinecraftImpl;
import gg.mooncraft.services.minecraft.bungee.config.BungeeConfiguration;
import gg.mooncraft.services.minecraft.bungee.config.ConfigurationPrefabs;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkCountersFactory;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkPlayersFactory;
import gg.mooncraft.services.minecraft.bungee.handlers.ServerListeners;
import gg.mooncraft.services.minecraft.bungee.utilities.BungeeRedisUtilities;
import gg.mooncraft.services.minecraft.core.JedisManager;
import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class MSMinecraftMain extends Plugin {
    
    /*
    Fields
     */
    private static MSMinecraftMain instance;
    private static Gson gson;
    
    private BungeeConfiguration bungeeConfiguration;
    private ConfigurationPrefabs configurationPrefabs;
    
    private JedisManager jedisManager;
    
    private BungeeMessaging bungeeMessaging;
    
    @Getter(value = AccessLevel.NONE)
    private MSMinecraftImpl msmBungeeImpl;
    private NetworkPlayersFactory networkPlayersFactory;
    private NetworkCountersFactory networkCountersFactory;
    
    /*
    Override Methods
     */
    @Override
    public void onEnable() {
        // Load instances
        instance = this;
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        
        // Load configuration
        this.bungeeConfiguration = new BungeeConfiguration(this);
        this.configurationPrefabs = new ConfigurationPrefabs(this.bungeeConfiguration);
        
        // Load JedisManager
        if (!loadJedisManager()) {
            onDisable();
            return;
        }
        
        // Load BungeeMessaging
        this.bungeeMessaging = new BungeeMessaging();
        
        // Load public API
        this.networkPlayersFactory = new NetworkPlayersFactory();
        this.networkCountersFactory = new NetworkCountersFactory();
        this.msmBungeeImpl = new MSMinecraftImpl(this.networkPlayersFactory, this.networkCountersFactory);
        ApiRegistrationUtility.registerProvider(this.msmBungeeImpl);
        
        // Load listeners
        new ServerListeners();
        
        getLogger().info("Enabled!");
    }
    
    @Override
    public void onDisable() {
        if (this.jedisManager != null) this.jedisManager.stop();
        ApiRegistrationUtility.unregisterProvider();
        
        if (this.bungeeMessaging != null) this.bungeeMessaging.unload();
        
        getLogger().info("Disabled!");
    }
    
    /*
    Static Methods
     */
    public static @NotNull MSMinecraftMain getInstance() {
        return instance;
    }
    
    public static @NotNull Gson getGson() {
        return gson;
    }
    
    /*
    Methods
     */
    boolean loadJedisManager() {
        try {
            Configuration sectionRedis = getBungeeConfiguration().getConfiguration().getSection("redis");
            if (sectionRedis == null) throw new IllegalStateException("redis section is missing from config.yml");
            HostAndPort hostAndPort = BungeeRedisUtilities.parseHostAndPort(sectionRedis);
            String username = sectionRedis.getString("username");
            String password = sectionRedis.getString("password");
            if (username == null || password == null)
                throw new IllegalStateException("complete credentials are missing from redis configuration section");
            Configuration sectionPoolSettings = sectionRedis.getSection("pool-settings");
            if (sectionPoolSettings == null)
                throw new IllegalStateException("pool-settings section is missing from redis configuration section");
            JedisPoolConfig jedisPoolConfig = BungeeRedisUtilities.parsePoolConfig(sectionPoolSettings);
            
            this.jedisManager = new JedisManager(jedisPoolConfig, hostAndPort, username, password);
        } catch (Exception e) {
            getLogger().warning(e.getMessage());
            return false;
        }
        return true;
    }
}