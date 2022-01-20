package gg.mooncraft.services.minecraft.bungee;

import lombok.AccessLevel;
import lombok.Getter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;
import me.eduardwayland.mooncraft.waylander.database.scheme.db.NormalDatabaseScheme;
import me.eduardwayland.mooncraft.waylander.database.scheme.file.NormalSchemeFile;
import me.eduardwayland.mooncraft.waylander.scheduler.Scheduler;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.minecraft.bungee.api.ApiRegistrationUtility;
import gg.mooncraft.services.minecraft.bungee.api.impl.MSMinecraftImpl;
import gg.mooncraft.services.minecraft.bungee.commands.IOCommand;
import gg.mooncraft.services.minecraft.bungee.commands.VerifyCommand;
import gg.mooncraft.services.minecraft.bungee.config.BungeeConfiguration;
import gg.mooncraft.services.minecraft.bungee.config.ConfigurationPrefabs;
import gg.mooncraft.services.minecraft.bungee.database.BungeeDatabaseUtilities;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkCountersFactory;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkPlayersFactory;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkServersFactory;
import gg.mooncraft.services.minecraft.bungee.handlers.CommunityListeners;
import gg.mooncraft.services.minecraft.bungee.handlers.ServerListeners;
import gg.mooncraft.services.minecraft.bungee.scheduler.BungeeScheduler;
import gg.mooncraft.services.minecraft.bungee.utilities.BungeeRedisUtilities;
import gg.mooncraft.services.minecraft.bungee.utilities.IOUtils;
import gg.mooncraft.services.minecraft.core.JedisManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class MSMinecraftMain extends Plugin {

    /*
    Fields
     */
    private static MSMinecraftMain instance;
    private static Gson gson;

    private BungeeConfiguration bungeeConfiguration;
    private ConfigurationPrefabs configurationPrefabs;

    private Database database;
    private Scheduler scheduler;
    private JedisManager jedisManager;

    private BungeeMessaging bungeeMessaging;

    @Getter(value = AccessLevel.NONE)
    private MSMinecraftImpl msmBungeeImpl;
    private NetworkPlayersFactory networkPlayersFactory;
    private NetworkServersFactory networkServersFactory;
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

        // Load Waylander libraries and JedisManager
        try {
            this.scheduler = new BungeeScheduler();

            if (!loadDatabase()) {
                getLogger().warning("Database connection cannot be estabilished.");
                return;
            }
            if (!loadJedisManager()) {
                getLogger().warning("Redis communication cannot be estabilished.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            onDisable();
            return;
        }

        // Load BungeeMessaging
        this.bungeeMessaging = new BungeeMessaging();

        // Load public API
        this.networkPlayersFactory = new NetworkPlayersFactory();
        this.networkCountersFactory = new NetworkCountersFactory();
        this.networkServersFactory = new NetworkServersFactory();

        this.msmBungeeImpl = new MSMinecraftImpl(this.networkPlayersFactory, this.networkServersFactory, this.networkCountersFactory);
        ApiRegistrationUtility.registerProvider(this.msmBungeeImpl);

        // Load listeners
        new ServerListeners();
        new CommunityListeners();

        // Load commands
        new VerifyCommand();
        getConfigurationPrefabs().getCommunityInputOutputPrefab().getInputOutputList().forEach(inputOutput -> new IOCommand(inputOutput.command(), inputOutput.permission()));

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
    boolean loadDatabase() throws IOException {
        Configuration configuration = getBungeeConfiguration().getConfiguration();
        if (!configuration.contains("mysql")) {
            getLogger().warning("mysql section is not into the config.yml!");
            return false;
        }

        // Load input stream
        InputStream inputStream = getResourceAsStream("msminecraft-db.scheme");
        if (inputStream == null) {
            getLogger().info("msminecraft-db.scheme is not inside the jar.");
            return false;
        }

        // Create temporary file
        File temporaryFile = new File(getDataFolder(), "msminecraft-db.scheme");
        if (!temporaryFile.exists() && !temporaryFile.createNewFile()) {
            getLogger().info("The temporary file msminecraft-db.scheme cannot be created.");
            return false;
        }

        // Load output stream
        FileOutputStream outputStream = new FileOutputStream(temporaryFile);

        // Copy input to output
        IOUtils.copy(inputStream, outputStream);

        // Close streams
        inputStream.close();
        outputStream.close();

        // Parse database scheme and delete temporary file
        NormalDatabaseScheme normalDatabaseScheme = new NormalSchemeFile(temporaryFile).parse();
        if (!temporaryFile.delete()) {
            getLogger().info("The temporary file msminecraft-db.scheme cannot be deleted. You could ignore this warning!");
        }

        Credentials credentials = BungeeDatabaseUtilities.fromBukkitConfig(configuration.getSection("mysql"));

        // Setup database
        this.database = Database.builder()
                .identifier(getDescription().getName())
                .scheduler(scheduler)
                .databaseScheme(normalDatabaseScheme)
                .connectionFactory(new MariaDBConnectionFactory(getDescription().getName(), credentials))
                .statistics()
                .build();
        return true;
    }

    boolean loadJedisManager() {
        try {
            Configuration sectionRedis = getBungeeConfiguration().getConfiguration().getSection("redis");
            if (sectionRedis == null)
                throw new IllegalStateException("redis section is missing from config.yml");
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