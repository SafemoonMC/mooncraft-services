package gg.mooncraft.services.minecraft.bungee.database.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CommunityUserHistory {
    
    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private final @NotNull String username;
    private final @NotNull Timestamp changeTimestamp;
    
    /*
    Override Methods
     */
    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return "CommunityUserHistory{" +
                "uniqueId=" + uniqueId +
                ", username='" + username + '\'' +
                ", changeTimestamp=" + changeTimestamp +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityUserHistory that = (CommunityUserHistory) o;
        return getUniqueId().equals(that.getUniqueId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}