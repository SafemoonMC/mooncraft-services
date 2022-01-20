package gg.mooncraft.services.restfulweb.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class BedwarsData {

    public int kills, deaths, wins, losses, finalKills, bedsBroken, ironLooted, goldLooted, diamondsLooted, emeraldsLooted = 0;
    public @NotNull String prestige;
}