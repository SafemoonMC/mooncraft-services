package gg.mooncraft.services.restfulweb.endpoints.post;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.form.StaffApplication;
import gg.mooncraft.services.restfulweb.endpoints.auth.AuthHandler;
import io.javalin.core.util.Header;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public final class PostFormStaffApplication extends AuthHandler {
    @Override
    public void handleAuthorized(@NotNull Context ctx) {
        ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        StaffApplication application = ctx.bodyValidator(StaffApplication.class)
                .check(obj -> obj.username.length() < 50 && obj.username.length() > 0, "user")
                .check(obj -> obj.email.length() < 160 && obj.email.length() > 0, "email")
                .check(obj -> obj.discordtag.length() < 60 && obj.discordtag.length() > 0, "discordtag")
                .check(obj -> obj.availability.length() < 160 && obj.availability.length() > 0, "availability")
                .check(obj -> obj.previousExperience.length() < 160 && obj.previousExperience.length() > 0, "previousExperience")
                .check(obj -> obj.age.length() < 50 && obj.age.length() > 0, "age")
                .check(obj -> obj.timeplayed.length() < 160 && obj.timeplayed.length() > 0, "timeplayed")
                .check(obj -> obj.staffOnAnotherServer.length() < 160 && obj.staffOnAnotherServer.length() > 0, "staffOnAnotherServer")
                .check(obj -> obj.strengthsWeaknesses.length() < 500 && obj.strengthsWeaknesses.length() > 0, "strengthsWeaknesses")
                .check(obj -> obj.whyShouldYouBeSelected.length() < 500 && obj.whyShouldYouBeSelected.length() > 0, "whyShouldYouBeSelected")
                .get();
        WebhookEmbed webhookEmbed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle("New Staff Application", null))
                .addField(new WebhookEmbed.EmbedField(true, "Username", application.username))
                .addField(new WebhookEmbed.EmbedField(true, "Email", application.email))
                .addField(new WebhookEmbed.EmbedField(true, "Discord Tag", application.discordtag))
                .addField(new WebhookEmbed.EmbedField(true, "Availability", application.availability))
                .addField(new WebhookEmbed.EmbedField(true, "Previous experience", application.previousExperience))
                .addField(new WebhookEmbed.EmbedField(true, "Age", application.age))
                .addField(new WebhookEmbed.EmbedField(true, "Time played", application.timeplayed))
                .addField(new WebhookEmbed.EmbedField(true, "Staff on another server?", application.staffOnAnotherServer))
                .addField(new WebhookEmbed.EmbedField(true, "Strengths & weaknesses?", application.strengthsWeaknesses))
                .addField(new WebhookEmbed.EmbedField(true, "Why should you be selected?", application.whyShouldYouBeSelected))
                .build();
        ApplicationBootstrap.getApplication().getDiscord().ifPresent(discord -> discord.getStaffApplicationWebhook().send(webhookEmbed));
    }
}