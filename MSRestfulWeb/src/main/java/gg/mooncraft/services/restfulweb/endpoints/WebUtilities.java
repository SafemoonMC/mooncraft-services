package gg.mooncraft.services.restfulweb.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.models.message.HttpResult;
import io.javalin.core.util.Header;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.util.NaiveRateLimit;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class WebUtilities {

    public static void enableCors(@NotNull Context context, @NotNull String methods) {
        context.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "https://mooncraft.gg");
        context.header(Header.ACCESS_CONTROL_ALLOW_METHODS, methods);
        context.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "*");
    }

    public static boolean checkLimit(@NotNull Context context) {
        try {
            NaiveRateLimit.requestPerTimeUnit(context, 6, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            context.status(HttpCode.TOO_MANY_REQUESTS);
            context.result(Application.GSON.toJson(new HttpResult(false, "Too many requests in a minute!"))).contentType(ContentType.APPLICATION_JSON);
            return false;
        }
    }
}