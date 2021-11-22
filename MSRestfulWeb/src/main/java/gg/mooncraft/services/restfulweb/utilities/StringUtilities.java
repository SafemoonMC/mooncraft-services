package gg.mooncraft.services.restfulweb.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtilities {
    
    public static boolean hasLetters(String text) {
        return text != null && !text.isEmpty() && !text.isBlank();
    }
}