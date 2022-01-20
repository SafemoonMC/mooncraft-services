package gg.mooncraft.services.restfulweb.endpoints.get;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.endpoints.WebUtilities;
import gg.mooncraft.services.restfulweb.models.message.HttpResult;
import gg.mooncraft.services.restfulweb.models.network.SingleServerData;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GetServersEach implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        if (!WebUtilities.checkLimit(ctx)) {
            return;
        }
        WebUtilities.enableCors(ctx, "GET, OPTIONS");

        if (ApplicationBootstrap.getApplication().getServersFactory() == null) {
            ctx.status(HttpCode.SERVICE_UNAVAILABLE);
            ctx.result(Application.GSON.toJson(new HttpResult(false, "The service is unavailable."))).contentType(ContentType.APPLICATION_JSON);
            return;
        }

        CompletableFuture<NetworkCounters> futureNetworkServers = ApplicationBootstrap.getApplication().getServersFactory().getNetworkCounters();
        ctx.future(futureNetworkServers, result -> {
            if (result != null) {
                NetworkCounters networkCounters = (NetworkCounters) result;
                List<SingleServerData> list = networkCounters.getServersCounterMap()
                        .entrySet()
                        .stream()
                        .map(entry -> new SingleServerData(entry.getKey(), entry.getValue()))
                        .sorted((o1, o2) -> o1.name().compareToIgnoreCase(o2.name()))
                        .collect(Collectors.toList());

                ctx.status(HttpCode.OK);
                ctx.result(Application.GSON.toJson(list)).contentType(ContentType.APPLICATION_JSON);
            } else {
                ctx.status(HttpCode.NOT_FOUND);
                ctx.result(Application.GSON.toJson(new HttpResult(false, "Unknown network status."))).contentType(ContentType.APPLICATION_JSON);
            }
        });
    }
}
