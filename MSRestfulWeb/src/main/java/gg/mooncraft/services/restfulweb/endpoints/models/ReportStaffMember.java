package gg.mooncraft.services.restfulweb.endpoints.models;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ReportStaffMember {
    public @NotNull String username;
    public @NotNull String email;
    public @NotNull String discordtag;
    public @NotNull String staffMemberUsername;
    public @NotNull String whatHappened;
}
