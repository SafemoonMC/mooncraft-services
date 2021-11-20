package gg.mooncraft.services.restfulweb;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;

public final class Application extends AbstractApplication {
    
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
    
    }
    
    @Override
    public void onEnable() {
    
    }
    
    @Override
    public void onDisable() {
    
    }
    
    @Override
    public @NotNull Options getLaunchOptions() {
        Option mysqlProperties = Option.builder("M").longOpt("mysql")
                .required()
                .numberOfArgs(4).valueSeparator()
                .argName("[property]=[value]")
                .desc("use value for given property. Available properties: username, password, hostname, port")
                .build();
        Option redisProperties = Option.builder("R").longOpt("redis")
                .required()
                .numberOfArgs(4).valueSeparator()
                .argName("[property]=[value]")
                .desc("use value for given property. Available properties: username, password, hostname, port")
                .build();
        Options options = new Options();
        options.addOption(mysqlProperties);
        options.addOption(redisProperties);
        return options;
    }
}