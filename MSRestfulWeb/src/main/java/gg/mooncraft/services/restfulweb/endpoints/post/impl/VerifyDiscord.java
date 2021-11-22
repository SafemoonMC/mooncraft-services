package gg.mooncraft.services.restfulweb.endpoints.post.impl;

import gg.mooncraft.services.restfulweb.endpoints.post.AuthHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class VerifyDiscord extends AuthHandler {
    
    @Override
    public void handleAuthorized(@NotNull Context ctx) {
        ctx.result("Not implemented yet.");
    }
}