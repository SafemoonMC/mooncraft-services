package gg.mooncraft.services.restfulweb.endpoints.post;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.endpoints.auth.AuthHandler;
import io.javalin.http.Context;

public class PostVerifyWallet extends AuthHandler {

    @Override
    public boolean handleAuthorized(@NotNull Context ctx) {
        ctx.result("Not implemented yet.");
        return false;
    }
}