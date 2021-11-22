package gg.mooncraft.services.restfulweb.endpoints.models;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public final class StaffApplication {
    public @NotNull String username;
    public @NotNull String email;
    public @NotNull String discordtag;
    public @NotNull String availability;
    public @NotNull String previousExperience;
    public @NotNull String age;
    public @NotNull String timeplayed;
    public @NotNull String staffOnAnotherServer;
    public @NotNull String strengthsWeaknesses;
    public @NotNull String whyShouldYouBeSelected;
}
