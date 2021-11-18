package gg.mooncraft.services.minecraft.bungee.config.prefabs;

import gg.mooncraft.services.minecraft.bungee.config.BungeeConfiguration;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public final class CommunityInputOutputPrefab {
    
    /*
    Fields
     */
    private final @NotNull List<InputOutput> inputOutputList;
    
    /*
    Constructor
     */
    public CommunityInputOutputPrefab(@NotNull BungeeConfiguration bungeeConfiguration) {
        this.inputOutputList = new ArrayList<>();
        Configuration configuration = bungeeConfiguration.getConfiguration().getSection("community-input-output");
        for (String key : configuration.getKeys()) {
            String permission = configuration.getString("community-input-output." + key + ".permission", "");
            List<String> message = configuration.getStringList("community-input-output." + key + ".message");
            this.inputOutputList.add(new InputOutput(key.toLowerCase(), permission, message));
        }
    }
    
    /*
    Methods
     */
    public @NotNull Optional<InputOutput> getInputOutput(@NotNull String input) {
        return this.inputOutputList.stream().filter(inputOutput -> inputOutput.command.equalsIgnoreCase(input)).findFirst();
    }
    
    /*
    Records
     */
    public record InputOutput(@NotNull String command, @NotNull String permission, List<String> message) {
        public boolean hasPermission(@NotNull CommandSender commandSender) {
            if (permission.isEmpty() || permission.isBlank()) return true;
            return commandSender.hasPermission(permission);
        }
    }
}