package gg.mooncraft.services.minecraft.bungee.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MSMinecraftProvider {
    
    /*
    Fields
     */
    private static MSMinecraft instance = null;
    
    /*
    Methods
     */
    public static @NotNull MSMinecraft get() {
        MSMinecraft instance = MSMinecraftProvider.instance;
        if (instance == null) throw new IllegalStateException("The API is not ready!");
        return instance;
    }
    
    @ApiStatus.Internal
    static void register(@NotNull MSMinecraft instance) {
        MSMinecraftProvider.instance = instance;
    }
    
    @ApiStatus.Internal
    static void unregister() {
        MSMinecraftProvider.instance = null;
    }
}