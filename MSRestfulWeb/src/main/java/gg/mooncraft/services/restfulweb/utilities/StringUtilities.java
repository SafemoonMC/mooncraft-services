package gg.mooncraft.services.restfulweb.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtilities {
    
    public static boolean hasLetters(String text) {
        return text != null && !text.isEmpty() && !text.isBlank();
    }
    
    public static @Nullable UUID parseUniqueId(@NotNull String literalUniqueId) {
        try {
            return UUID.fromString(literalUniqueId);
        } catch (Exception e) {
            return null;
        }
    }
}