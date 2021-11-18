package gg.mooncraft.services.minecraft.bungee.utilities;

import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ResourceUtility {
    
    @SuppressWarnings("all")
    public static @Nullable File loadResource(@NotNull Plugin plugin, @NotNull String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) return null;
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists() && resourceFile.createNewFile()) {
                try (InputStream in = plugin.getResourceAsStream(resource); OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
    
    public static @Nullable Configuration loadConfig(@NotNull File file) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}