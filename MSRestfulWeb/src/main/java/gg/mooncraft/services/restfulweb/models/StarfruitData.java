package gg.mooncraft.services.restfulweb.models;

import gg.mooncraft.services.restfulweb.models.starfruit.StarfruitCollection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StarfruitData {
    
    /*
    Fields
     */
    public final @NotNull Map<String, Integer> stats;
    public final @NotNull List<StarfruitCollection> collections;
    
    /*
    Constructor
     */
    public StarfruitData() {
        this(new HashMap<>(), new ArrayList<>());
    }
    
    public StarfruitData(@NotNull Map<String, Integer> stats, @NotNull List<StarfruitCollection> collections) {
        this.stats = stats;
        this.collections = collections;
    }
}