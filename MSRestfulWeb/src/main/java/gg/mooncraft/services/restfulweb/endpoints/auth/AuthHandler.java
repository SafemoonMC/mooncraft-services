package gg.mooncraft.services.restfulweb.endpoints.auth;

import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.message.HttpResult;
import io.javalin.http.ContentType;
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
        ctx.result(Application.GSON.toJson(new HttpResult(false, "Authentication is required."))).contentType(ContentType.APPLICATION_JSON);
        ApplicationBootstrap.getLogger().warn("Unauthorized connection detected from IP: {}", ctx.ip());
    }
}