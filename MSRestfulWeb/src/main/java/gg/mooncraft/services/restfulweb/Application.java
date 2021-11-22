package gg.mooncraft.services.restfulweb;

import gg.mooncraft.services.restfulweb.mysql.MySQLUtilities;
import gg.mooncraft.services.restfulweb.properties.PropertiesWrapper;
import gg.mooncraft.services.restfulweb.redis.JedisManager;
import gg.mooncraft.services.restfulweb.redis.JedisUtilities;
import gg.mooncraft.services.restfulweb.scheduler.AppScheduler;
import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class Application extends AbstractApplication {
    
    /*
    Constants
     */
    private static final String LAUNCH_PROPERTIES_FILE = "launch.properties";
    
    /*
    Fields
     */
    private @Nullable AppScheduler appScheduler;
    private @Nullable Database database;
    private @Nullable JedisManager jedisManager;
    
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
    public void onEnable() throws FileNotFoundException {
        if (getCommandLine() == null) {
            shutdown();
            return;
        }
        
        // Load properties either from command line or file
        PropertiesWrapper propertiesWrapper;
        if (getCommandLine().hasOption("NF")) {
            Properties mysqlProperties = getCommandLine().getOptionProperties("M");
            Properties redisProperties = getCommandLine().getOptionProperties("R");
            
            propertiesWrapper = new PropertiesWrapper();
            propertiesWrapper.putAll(mysqlProperties, "mysql");
            propertiesWrapper.putAll(redisProperties, "redis");
        } else {
            File file = new File(LAUNCH_PROPERTIES_FILE);
            propertiesWrapper = new PropertiesWrapper(file);
        }
        
        // Setup Waylander library
        this.appScheduler = new AppScheduler();
        Credentials credentials = MySQLUtilities.fromProperties(propertiesWrapper);
        this.database = Database.builder().identifier(getName()).scheduler(appScheduler).connectionFactory(new MariaDBConnectionFactory(getName(), credentials)).statistics().build();
        
        // Setup Redis
        getLogger().info(propertiesWrapper.getProperty("redis.username") + " - " + propertiesWrapper.getProperty("redis.password"));
        this.jedisManager = new JedisManager(JedisUtilities.parsePoolConfig(propertiesWrapper), JedisUtilities.parseHostAndPort(propertiesWrapper), propertiesWrapper.getProperty("redis.username"), propertiesWrapper.getProperty("redis.password"));
        
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
    
}