package gg.mooncraft.services.restfulweb;

import lombok.Getter;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Getter
public abstract class AbstractApplication {
    
    /*
    Fields
     */
    private final @NotNull String name;
    private final @NotNull String description;
    private @Nullable CommandLine commandLine;
    
    /*
    Constructor
     */
    public AbstractApplication(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
    }
    
    /*
    Abstract Methods
     */
    
    /**
     * The first lifecycle method: gets called after command line parsing
     */
    public abstract void onLoad() throws Exception;
    
    /**
     * The second lifecycle method: gets called after onLoad method.
     */
    public abstract void onEnable() throws Exception;
    
    /**
     * The final lifectycle method: gets called when the application shutdowns through a controlled process
     */
    public abstract void onDisable();
    
    /**
     * It creates an Options instance with all the available launch options
     *
     * @return options instance
     */
    public abstract @NotNull Options getLaunchOptions();
    
    
    /*
    Methods
     */
    
    /**
     * Starts the application
     *
     * @param args the application launch arguments
     */
    void run(@NotNull String[] args) {
        System.out.println(getName());
        System.out.println(getDescription());
        System.out.println();
        System.out.println("Launch arguments: " + (args.length == 0 ? "none" : String.join(" ", args)));
        System.out.println();
        getLogger().info("Starting up...");
        
        // Register disabling hook
        Thread onDisableHook = new Thread(() -> {
            getLogger().info("Disabling...");
            onDisable();
            getLogger().info("Application shutdown.");
        }, getName());
        Runtime.getRuntime().addShutdownHook(onDisableHook);
        
        // Try to parse arguments
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            this.commandLine = commandLineParser.parse(getLaunchOptions(), args);
        } catch (Exception e) {
            getLogger().error("Launch arguments cannot be parsed: \n {} \n", e.getMessage());
            printHelp();
            shutdown();
            return;
        }
        
        // Call the lifecycle methods
        try {
            getLogger().info("Loading...");
            onLoad();
            getLogger().info("Enabling...");
            onEnable();
        } catch (Exception e) {
            getLogger().error("Error during a lifecycle...");
            e.printStackTrace();
            shutdown();
        }
    }
    
    /**
     * Starts application shutdown process
     */
    void shutdown() {
        getLogger().info("Shutting down the application...");
        System.exit(0);
    }
    
    /**
     * Prints a helpful message
     */
    void printHelp() {
        Options options = getLaunchOptions();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(getName(), "List of all options:", options, "+---------------+", true);
    }
    
    /**
     * An easy way to access the logger
     *
     * @return the logger from the application bootstrap
     */
    protected @NotNull Logger getLogger() {
        return ApplicationBootstrap.getLogger();
    }
    
}