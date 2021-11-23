package gg.mooncraft.services.restfulweb.endpoints.post;

import gg.mooncraft.services.restfulweb.endpoints.auth.AuthHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class PostVerifyWallet extends AuthHandler {
    
    @Override
    public void handleAuthorized(@NotNull Context ctx) {
        ctx.result("Not implemented yet.");
    }
}