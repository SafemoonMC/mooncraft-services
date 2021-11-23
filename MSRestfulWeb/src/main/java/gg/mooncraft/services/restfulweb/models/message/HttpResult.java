package gg.mooncraft.services.restfulweb.models.message;

import org.jetbrains.annotations.NotNull;

public final record HttpResult(boolean success, @NotNull String reason) {
}