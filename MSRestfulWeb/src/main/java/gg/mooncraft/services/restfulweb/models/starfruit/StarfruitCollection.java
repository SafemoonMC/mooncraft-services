package gg.mooncraft.services.restfulweb.models.starfruit;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record StarfruitCollection(@NotNull String name, @NotNull List<StarfruitCollectionItem> items) {
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarfruitCollection that = (StarfruitCollection) o;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}