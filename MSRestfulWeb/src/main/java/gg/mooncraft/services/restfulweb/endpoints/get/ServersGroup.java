package gg.mooncraft.services.restfulweb.endpoints.get;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.datamodels.NetworkServers;
import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.message.HttpResult;
import gg.mooncraft.services.restfulweb.models.network.GroupServerData;
import gg.mooncraft.services.restfulweb.models.network.SingleServerData;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServersGroup implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        if (ApplicationBootstrap.getApplication().getServersFactory() == null) {
            ctx.status(HttpCode.SERVICE_UNAVAILABLE);
            ctx.result(Application.GSON.toJson(new HttpResult(false, "The service is unavailable."))).contentType(ContentType.APPLICATION_JSON);
            return;
        }
        
        // Get NetworkServers and NetworkCounters instance if any and create new objects
        CompletableFuture<GroupServerData> future = CompletableFuture.supplyAsync(() -> {
            NetworkServers networkServers = ApplicationBootstrap.getApplication().getServersFactory().getNetworkServers().join();
            NetworkCounters networkCounters = ApplicationBootstrap.getApplication().getServersFactory().getNetworkCounters().join();
            
            if (networkServers == null || networkCounters == null) return null;
            
            List<SingleServerData> list = new ArrayList<>();
            networkServers.getServerGroupMap().forEach((k, v) -> {
                long amount = v.getServerList().stream().mapToLong(networkCounters::getServerCounter).sum();
                list.add(new SingleServerData(v.getDisplay(), amount));
            });
            return new GroupServerData(networkCounters.getTotalOnlinePlayers(), list);
        });
        
        ctx.future(future, result -> {
            if (result != null) {
                ctx.status(HttpCode.OK);
                ctx.result(Application.GSON.toJson(result)).contentType(ContentType.APPLICATION_JSON);
            } else {
                ctx.status(HttpCode.NOT_FOUND);
                ctx.result(Application.GSON.toJson(new HttpResult(false, "Unknown network status."))).contentType(ContentType.APPLICATION_JSON);
            }
        });
    }
}
