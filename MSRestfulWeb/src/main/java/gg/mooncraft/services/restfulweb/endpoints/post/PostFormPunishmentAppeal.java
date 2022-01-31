package gg.mooncraft.services.restfulweb.endpoints.post;

import org.jetbrains.annotations.NotNull;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.endpoints.WebUtilities;
import gg.mooncraft.services.restfulweb.endpoints.auth.AuthHandler;
import gg.mooncraft.services.restfulweb.models.form.PunishmentAppeal;
import io.javalin.core.validation.BodyValidator;
import io.javalin.http.Context;

public final class PostFormPunishmentAppeal extends AuthHandler {
    @Override
    public boolean handleAuthorized(@NotNull Context ctx) {
        BodyValidator<PunishmentAppeal> bodyValidator = ctx.bodyValidator(PunishmentAppeal.class)
                .check(obj -> obj.username.length() < 50 && obj.username.length() > 0, "check fail: user")
                .check(obj -> obj.email.length() < 160 && obj.email.length() > 0, "check fail: email")
                .check(obj -> obj.discordtag.length() < 60 && obj.discordtag.length() > 0, "check fail: discordtag")
                .check(obj -> obj.punishmentReceived.length() < 160 && obj.punishmentReceived.length() > 0, "check fail: punishmentReceived")
                .check(obj -> obj.whyWeShouldAppeal.length() < 500 && obj.whyWeShouldAppeal.length() > 0, "check fail: whyWeShouldAppeal");
        if (!WebUtilities.checkRequest(ctx, bodyValidator)) {
            return false;
        }
        PunishmentAppeal application = bodyValidator.get();
        WebhookEmbed webhookEmbed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle("New Punishment Appeal", null))
                .addField(new WebhookEmbed.EmbedField(true, "Username", application.username))
                .addField(new WebhookEmbed.EmbedField(true, "Email", application.email))
                .addField(new WebhookEmbed.EmbedField(true, "Discord Tag", application.discordtag))
                .addField(new WebhookEmbed.EmbedField(true, "Punishment Received", application.punishmentReceived))
                .addField(new WebhookEmbed.EmbedField(false, "Why should your punishment be appealed?", application.whyWeShouldAppeal))
                .build();
        ApplicationBootstrap.getApplication().getDiscord().ifPresent(discord -> discord.getPunishmentAppealWebhook().send(webhookEmbed));
        return true;
    }
}