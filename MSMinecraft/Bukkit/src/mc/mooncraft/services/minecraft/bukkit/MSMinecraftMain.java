package mc.mooncraft.services.minecraft.bukkit;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import mc.mooncraft.services.minecraft.bukkit.api.ApiRegistrationUtility;
import mc.mooncraft.services.minecraft.bukkit.api.impl.MSMinecraftImpl;
import mc.mooncraft.services.minecraft.bukkit.factories.NetworkCountersFactory;
import mc.mooncraft.services.minecraft.bukkit.papi.PlayersExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class MSMinecraftMain extends JavaPlugin {
    
    /*
    Fields
     */
    private static Gson gson;
    
    private BungeeMessaging bungeeMessaging;
    
    @Getter(value = AccessLevel.NONE)
    private MSMinecraftImpl msmBungeeImpl;
    @Getter(value = AccessLevel.PACKAGE)
    private NetworkCountersFactory networkCountersFactory;
    
    /*
    Override Methods
     */
    @Override
    public void onEnable() {
        // Load instances
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        
        // Load public API
        this.networkCountersFactory = new NetworkCountersFactory();
        this.msmBungeeImpl = new MSMinecraftImpl(this.networkCountersFactory);
        ApiRegistrationUtility.registerProvider(this.msmBungeeImpl);
        
        // Load BungeeMessaging
        this.bungeeMessaging = new BungeeMessaging();
        
        // Load PlaceholderAPI dependency if any
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI has been found. The placeholder %msminecraft_players-[group]% is registering...");
            new PlayersExpansion();
        } else {
            getLogger().info("PlaceholderAPI has not been found. The placeholder %msminecraft_players-[group]% cannot be registered.");
        }
        
        getLogger().info("Enabled!");
    }
    
    @Override
    public void onDisable() {
        ApiRegistrationUtility.unregisterProvider();
        
        if (this.bungeeMessaging != null) this.bungeeMessaging.unload();
        
        getLogger().info("Disabled!");
    }
    
    /*
    Static Methods
     */
    public static @NotNull MSMinecraftMain getInstance() {
        return MSMinecraftMain.getPlugin(MSMinecraftMain.class);
    }
    
    public static @NotNull Gson getGson() {
        return gson;
    }
}