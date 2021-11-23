package gg.mooncraft.services.restfulweb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.mooncraft.services.restfulweb.discord.Discord;
import gg.mooncraft.services.restfulweb.endpoints.RestPaths;
import gg.mooncraft.services.restfulweb.factories.PlayersFactory;
import gg.mooncraft.services.restfulweb.factories.ServersFactory;
import gg.mooncraft.services.restfulweb.gson.RecordTypeAdapterFactory;
import gg.mooncraft.services.restfulweb.mysql.MySQLUtilities;
import gg.mooncraft.services.restfulweb.properties.PropertiesWrapper;
import gg.mooncraft.services.restfulweb.redis.JedisManager;
import gg.mooncraft.services.restfulweb.redis.JedisUtilities;
import gg.mooncraft.services.restfulweb.scheduler.AppScheduler;
import io.javalin.Javalin;
import lombok.Getter;
import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

@Getter
public final class Application extends AbstractApplication {
    
    /*
    Constants
     */
    public static final @NotNull Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final @NotNull Gson GSON_REDIS = new GsonBuilder().registerTypeAdapterFactory(new RecordTypeAdapterFactory()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
    private static final @NotNull String LAUNCH_PROPERTIES_FILE = "launch.properties";
    
    /*
    Fields
     */
    private @Nullable PropertiesWrapper propertiesWrapper;
    
    private @Nullable AppScheduler appScheduler;
    private @Nullable Database database;
    
    private @Nullable JedisManager jedisManager;
    
    private @Nullable Javalin javalin;
    private @Nullable Discord discord;
    
    private @Nullable PlayersFactory playersFactory;
    private @Nullable ServersFactory serversFactory;
    
    /*
    Constructor
     */
    public Application(@NotNull String name, @NotNull String description) {
        super(name, description);
    }
    
    /*
    Override Methods
     */
    @Override
    public void onLoad() {
        if (getCommandLine() == null) {
            shutdown();
            return;
        }
        
        // Check if application can run with the launch options
        if (getCommandLine().hasOption("nofile") && (!getCommandLine().hasOption("redis") || !getCommandLine().hasOption("mysql"))) {
            getLogger().error("You cannot have -NF without -M and -R launch options.");
            shutdown();
            return;
        }
        
        // Get LAUNCH_PROPERTIES_FILE and create at root
        if (!getCommandLine().hasOption("nofile")) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(LAUNCH_PROPERTIES_FILE)) {
                if (inputStream != null) {
                    Path path = Paths.get(LAUNCH_PROPERTIES_FILE);
                    if (!Files.exists(path)) {
                        long bytes = Files.copy(inputStream, path);
                        getLogger().info("{} ({}KiB) has been created at the current path.", LAUNCH_PROPERTIES_FILE, bytes / 1024);
                    }
                } else {
                    getLogger().error("Property file '{}' not found.", LAUNCH_PROPERTIES_FILE);
                }
            } catch (Exception e) {
                getLogger().error("Property file '{}' cannot be created. Error: {}", LAUNCH_PROPERTIES_FILE, e);
                if ((!getCommandLine().hasOption("redis") || !getCommandLine().hasOption("mysql"))) {
                    shutdown();
                    return;
                }
            }
        }
        
        getLogger().info("Application has been loaded.");
    }
    
    @Override
    public void onEnable() throws Exception {
        if (getCommandLine() == null) {
            shutdown();
            return;
        }
        
        // Load properties either from command line or file
        if (getCommandLine().hasOption("NF")) {
            Properties mysqlProperties = getCommandLine().getOptionProperties("M");
            Properties redisProperties = getCommandLine().getOptionProperties("R");
            
            this.propertiesWrapper = new PropertiesWrapper();
            this.propertiesWrapper.putAll(mysqlProperties, "mysql");
            this.propertiesWrapper.putAll(redisProperties, "redis");
        } else {
            File file = new File(LAUNCH_PROPERTIES_FILE);
            this.propertiesWrapper = new PropertiesWrapper(file);
        }
        
        // Setup Waylander library
        this.appScheduler = new AppScheduler();
        Credentials credentials = MySQLUtilities.fromProperties(propertiesWrapper);
        this.database = Database.builder().identifier(getName()).scheduler(appScheduler).connectionFactory(new MariaDBConnectionFactory(getName(), credentials)).statistics().build();
        
        // Setup Redis
        getLogger().info(propertiesWrapper.getProperty("redis.username") + " - " + propertiesWrapper.getProperty("redis.password"));
        this.jedisManager = new JedisManager(JedisUtilities.parsePoolConfig(propertiesWrapper), JedisUtilities.parseHostAndPort(propertiesWrapper), propertiesWrapper.getProperty("redis.username"), propertiesWrapper.getProperty("redis.password"));
        
        // Setup Discord
        this.discord = new Discord();
        
        // Setup Javalin
        this.javalin = Javalin.create();
        this.javalin._conf.showJavalinBanner = false;
        this.javalin.start(propertiesWrapper.getPropertyInteger("restapi.port", 5000));
        
        registerRequestHandlers();
        
        // Setup factories
        this.playersFactory = new PlayersFactory();
        this.serversFactory = new ServersFactory();
        
        getLogger().info("Application has been enabled.");
    }
    
    @Override
    public void onDisable() {
        if (this.database != null) this.database.shutdown();
        if (this.jedisManager != null) this.jedisManager.stop();
        if (this.appScheduler != null) {
            this.appScheduler.shutdownExecutor();
            this.appScheduler.shutdownScheduler();
        }
        if (this.javalin != null) this.javalin.stop();
        
        getLogger().info("Application has been disabled.");
    }
    
    @Override
    public @NotNull Options getLaunchOptions() {
        Option mysqlProperties = Option.builder("M").longOpt("mysql")
                .numberOfArgs(4).valueSeparator()
                .argName("[property]=[value]")
                .desc("use value for given property. Available properties: username, password, hostname, port")
                .build();
        Option redisProperties = Option.builder("R").longOpt("redis")
                .numberOfArgs(4).valueSeparator()
                .argName("[property]=[value]")
                .desc("use value for given property. Available properties: username, password, hostname, port")
                .build();
        Option noFileProperties = Option.builder("NF").longOpt("nofile")
                .desc("ignores launch.properties file creation")
                .build();
        Options options = new Options();
        options.addOption(mysqlProperties);
        options.addOption(redisProperties);
        options.addOption(noFileProperties);
        return options;
    }
    
    /*
    Methods
     */
    private void registerRequestHandlers() {
        if (this.javalin == null) return;
        for (RestPaths restPaths : RestPaths.values()) {
            switch (restPaths.getRequestType()) {
                case GET -> this.javalin.get(restPaths.getPath(), restPaths.getHandler());
                case POST -> this.javalin.post(restPaths.getPath(), restPaths.getHandler());
            }
        }
    }
    
    public @NotNull Optional<Discord> getDiscord() {
        return Optional.ofNullable(this.discord);
    }
    
    public @NotNull Optional<PropertiesWrapper> getProperties() {
        return Optional.ofNullable(this.propertiesWrapper);
    }
}