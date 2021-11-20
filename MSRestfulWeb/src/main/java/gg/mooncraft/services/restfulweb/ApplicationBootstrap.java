package gg.mooncraft.services.restfulweb;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrap class for the application.
 */
public final class ApplicationBootstrap {
    
    /*
    Constants
     */
    @Getter
    private static final @NotNull Logger logger = LoggerFactory.getLogger("Main");
    
    /**
     * Application entry point.
     *
     * @param args application command line arguments
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("MSRestfulWeb");
        
        Application application = new Application("MSRestfulWeb", "This is the MoonCraft RESTful Web Service");
        application.run(args);
    }
}