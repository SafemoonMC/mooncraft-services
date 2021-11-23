package gg.mooncraft.services.restfulweb.endpoints.get;

import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.PlayerData;
import gg.mooncraft.services.restfulweb.models.message.HttpResult;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player implements Handler {
    
    /*
    Constants
     */
    private static final @NotNull Pattern MOJANG_USERNAME_UUID_REGEX = Pattern.compile("^[A-Za-z0-9_-]*$", Pattern.CASE_INSENSITIVE);
    
    /*
    Override Methods
     */
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        if (ApplicationBootstrap.getApplication().getPlayersFactory() == null) {
            ctx.status(HttpCode.SERVICE_UNAVAILABLE);
            ctx.result(Application.GSON.toJson(new HttpResult(false, "The service is unavailable."))).contentType(ContentType.APPLICATION_JSON);
            return;
        }
        String playerParameter = ctx.pathParam("player");
        
        // Check if parameter could be a username or unique id and deny if not
        Matcher matcher = MOJANG_USERNAME_UUID_REGEX.matcher(playerParameter);
        if (!matcher.find()) {
            ctx.status(HttpCode.BAD_REQUEST);
            ctx.result(Application.GSON.toJson(new HttpResult(false, "Invalid parameter [player]."))).contentType(ContentType.APPLICATION_JSON);
            return;
        }
        
        // Get the PlayerData and sends the JSON if any has been found
        CompletableFuture<PlayerData> futurePlayerData = ApplicationBootstrap.getApplication().getPlayersFactory().getPlayerData(playerParameter);
        ctx.future(futurePlayerData, result -> {
            if (result != null) {
                ctx.status(HttpCode.OK);
                ctx.result(Application.GSON.toJson(result)).contentType(ContentType.APPLICATION_JSON);
            } else {
                ctx.status(HttpCode.NOT_FOUND);
                ctx.result(Application.GSON.toJson(new HttpResult(false, "Unknown player."))).contentType(ContentType.APPLICATION_JSON);
            }
        });
    }
}