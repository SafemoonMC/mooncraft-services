package gg.mooncraft.services.restfulweb.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class OGPrisonData {
    public int blocksBroken, prestige = 0;
    public @NotNull String currentMine = "A";
}
