package gg.mooncraft.services.restfulweb.models;

import gg.mooncraft.services.restfulweb.daos.BedwarsDAO;
import gg.mooncraft.services.restfulweb.daos.PlayerDAO;
import gg.mooncraft.services.restfulweb.daos.PrisonDAO;
import gg.mooncraft.services.restfulweb.daos.StarfruitDAO;
import gg.mooncraft.services.restfulweb.models.player.PlayerLoginData;
import lombok.AllArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public final class PlayerData implements EntityParent<PlayerData> {
    
    /*
    Fields
     */
    private final @NotNull UUID uuid;
    private final @NotNull String name;
    private final @NotNull PlayerLoginData playerLoginData;
    private @NotNull String rank;
    private @NotNull BedwarsData bedwarsData;
    private @NotNull OGPrisonData ogPrisonData;
    private @NotNull OPPrisonData opPrisonData;
    private @NotNull StarfruitData starfruitData;
    
    /*
    Constructor
     */
    public PlayerData(@NotNull UUID uniqueId, @NotNull String username, @NotNull PlayerLoginData playerLoginData) {
        this.uuid = uniqueId;
        this.name = username;
        this.playerLoginData = playerLoginData;
    }
    
    /*
    Override Methods
     */
    @Override
    public CompletableFuture<PlayerData> withChildren() {
        CompletableFuture<Void> rankCompletableFuture = PlayerDAO.getPlayerRank(this.uuid).thenAccept(futureData -> this.rank = futureData);
        CompletableFuture<Void> bedwarsDataCompletableFuture = BedwarsDAO.loadBedwarsData(this.uuid).thenAccept(futureData -> this.bedwarsData = futureData);
        CompletableFuture<Void> ogPrisonDataCompletableFuture = PrisonDAO.loadOGPrisonData(this.uuid).thenAccept(futureData -> this.ogPrisonData = futureData);
        CompletableFuture<Void> opPrisonDataCompletableFuture = PrisonDAO.loadOPPrisonData(this.uuid).thenAccept(futureData -> this.opPrisonData = futureData);
        CompletableFuture<Void> starfruitDataCompletableFuture = StarfruitDAO.loadStarfruitData(this.uuid).thenAccept(futureData -> this.starfruitData = futureData);
        
        
        return CompletableFuture.allOf(rankCompletableFuture, bedwarsDataCompletableFuture, ogPrisonDataCompletableFuture, opPrisonDataCompletableFuture, starfruitDataCompletableFuture).thenApply(v -> this);
    }
}