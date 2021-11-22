package gg.mooncraft.services.restfulweb.endpoints.post;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

public abstract class AuthHandler implements Handler {
    
    /*
    Abstract Methods
     */
    public abstract void handleAuthorized(@NotNull Context ctx);
    
    /*
    Override Methods
     */
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authParameter = ctx.queryParam("auth");
        if (authParameter == null) {
            deny(ctx);
            return;
        }
        String authorizedToken = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getProperty("restapi.token")).orElse(null);
        
        // Check if authorized token is correctly set and deny if not
        if (authorizedToken == null || authorizedToken.isEmpty() || authorizedToken.isBlank()) {
            deny(ctx);
            return;
        }
        // Check if authorized token matches parameter and deny if not
        if (!authParameter.equals(authorizedToken)) {
            deny(ctx);
            return;
        }
        
        // Call new context handling method since authorized
        handleAuthorized(ctx);
    }
    
    /*
    Methods
     */
    private void deny(@NotNull Context ctx) {
        ctx.status(HttpCode.UNAUTHORIZED);
        ApplicationBootstrap.getLogger().warn("Unauthorized connection detected from IP: {}", ctx.ip());
    }
}