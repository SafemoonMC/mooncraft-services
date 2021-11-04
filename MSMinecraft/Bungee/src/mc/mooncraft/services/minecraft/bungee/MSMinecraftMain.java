package mc.mooncraft.services.minecraft.bungee;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import mc.mooncraft.services.minecraft.bungee.api.ApiRegistrationUtility;
import mc.mooncraft.services.minecraft.bungee.api.impl.MSMinecraftImpl;
import mc.mooncraft.services.minecraft.bungee.config.BungeeConfiguration;
import mc.mooncraft.services.minecraft.bungee.factories.NetworkCountersFactory;
import mc.mooncraft.services.minecraft.bungee.factories.NetworkPlayersFactory;
import mc.mooncraft.services.minecraft.bungee.handlers.ServerListeners;
import mc.mooncraft.services.minecraft.bungee.utilities.BungeeRedisUtilities;
import mc.mooncraft.services.minecraft.core.JedisManager;
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
    private JedisManager jedisManager;
    
    private MSMinecraftImpl mcsBungeeImpl;
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
        
        // Load JedisManager
        if (!loadJedisManager()) {
            onDisable();
            return;
        }
        
        // Load public API
        this.networkPlayersFactory = new NetworkPlayersFactory();
        this.networkCountersFactory = new NetworkCountersFactory();
        this.mcsBungeeImpl = new MSMinecraftImpl(this.networkPlayersFactory, this.networkCountersFactory);
        ApiRegistrationUtility.registerProvider(this.mcsBungeeImpl);
        
        // Load listeners
        new ServerListeners();
        
        getLogger().info("Enabled!");
    }
    
    @Override
    public void onDisable() {
        if (this.jedisManager != null) this.jedisManager.stop();
        ApiRegistrationUtility.unregisterProvider();
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
            if (username == null || password == null) throw new IllegalStateException("complete credentials are missing from redis configuration section");
            Configuration sectionPoolSettings = sectionRedis.getSection("pool-settings");
            if (sectionPoolSettings == null) throw new IllegalStateException("pool-settings section is missing from redis configuration section");
            JedisPoolConfig jedisPoolConfig = BungeeRedisUtilities.parsePoolConfig(sectionPoolSettings);
            
            this.jedisManager = new JedisManager(jedisPoolConfig, hostAndPort, username, password);
        } catch (Exception e) {
            getLogger().warning(e.getMessage());
            return false;
        }
        return true;
    }
}