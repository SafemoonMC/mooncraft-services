package gg.mooncraft.services.restfulweb.daos.bedwars.prestige;

import org.jetbrains.annotations.NotNull;

public final class Prestiges {

    /*
    Fields
     */
    private static final Prestige[] PRESTIGES = {
            new Prestige("STONE"),
            new Prestige("IRON"),
            new Prestige("GOLD"),
            new Prestige("DIAMOND"),
            new Prestige("EMERALD"),
            new Prestige("SAPPHIRE"),
            new Prestige("BLITZ"),
            new Prestige("CRYSTAL"),
            new Prestige("WARRIOR"),
            new Prestige("ROYAL WARRIOR"),
            new Prestige("RAINBOW"),
            new Prestige("IRON PRIME"),
            new Prestige("GOLD PRIME"),
            new Prestige("DIAMOND PRIME"),
            new Prestige("EMERALD PRIME"),
            new Prestige("SAPPHIRE PRIME"),
            new Prestige("BLITZ PRIME"),
            new Prestige("CRYSTAL PRIME"),
            new Prestige("WARRIOR PRIME"),
            new Prestige("ROYAL PRIME"),
            new Prestige("TWILIGHT PRIME"),
            new Prestige("DIMENSIONAL"),
            new Prestige("DAWNBREAKER"),
            new Prestige("DUSTBREAKER"),
            new Prestige("CELESTIAL I"),
            new Prestige("CELESTIAL II"),
            new Prestige("CELESTIAL III"),
            new Prestige("CELESTIAL IV"),
            new Prestige("CELESTIAL V"),
            new Prestige("DRAGON MASTER"),
            new Prestige("DRAGON KING"),
    };

    /*
    Methods
     */
    public static int getPrestigesCount() {
        return PRESTIGES.length - 1;
    }

    public static @NotNull Prestige getLowestPrestige() {
        return PRESTIGES[0];
    }

    public static @NotNull Prestige getHighestPrestige() {
        return PRESTIGES[PRESTIGES.length - 1];
    }

    public static @NotNull Prestige getPrestige(int prestige) {
        if (prestige < 0) return getLowestPrestige();
        if (prestige >= PRESTIGES.length) return getHighestPrestige();
        return PRESTIGES[prestige];
    }
}