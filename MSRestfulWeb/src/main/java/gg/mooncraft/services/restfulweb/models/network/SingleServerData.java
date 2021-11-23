package gg.mooncraft.services.restfulweb.models.network;

import org.jetbrains.annotations.NotNull;

public record SingleServerData(@NotNull String name, long playerCount) {
}