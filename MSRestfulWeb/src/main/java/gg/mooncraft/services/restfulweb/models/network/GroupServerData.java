package gg.mooncraft.services.restfulweb.models.network;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record GroupServerData(long totalOnline, @NotNull List<SingleServerData> servers) {
}