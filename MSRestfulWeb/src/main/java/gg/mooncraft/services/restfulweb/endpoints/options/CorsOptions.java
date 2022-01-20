package gg.mooncraft.services.restfulweb.endpoints.options;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.endpoints.WebUtilities;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CorsOptions implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        if (!WebUtilities.checkLimit(ctx)) {
            return;
        }
        WebUtilities.enableCors(ctx, "GET, OPTIONS");
    }
}