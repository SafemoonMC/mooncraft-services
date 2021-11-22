package gg.mooncraft.services.restfulweb.models.starfruit;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record StarfruitCollectionItem(@NotNull String name, int amount) {
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarfruitCollectionItem that = (StarfruitCollectionItem) o;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}