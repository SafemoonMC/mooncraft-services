package gg.mooncraft.services.restfulweb.endpoints.post.impl;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.endpoints.models.PunishmentAppeal;
import gg.mooncraft.services.restfulweb.endpoints.post.AuthHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public final class FormPunishmentAppeal extends AuthHandler {
    @Override
    public void handleAuthorized(@NotNull Context ctx) {
        PunishmentAppeal application = ctx.bodyValidator(PunishmentAppeal.class)
                .check(obj -> obj.username.length() < 50 && obj.username.length() > 0, "user")
                .check(obj -> obj.email.length() < 160 && obj.email.length() > 0, "email")
                .check(obj -> obj.discordtag.length() < 60 && obj.discordtag.length() > 0, "discordtag")
                .check(obj -> obj.punishmentReceived.length() < 160 && obj.punishmentReceived.length() > 0, "punishmentReceived")
                .check(obj -> obj.whyWeShouldAppeal.length() < 500 && obj.whyWeShouldAppeal.length() > 0, "whyWeShouldAppeal")
                .get();
        WebhookEmbed webhookEmbed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle("New Punishment Appeal", null))
                .addField(new WebhookEmbed.EmbedField(true, "Username", application.username))
                .addField(new WebhookEmbed.EmbedField(true, "Email", application.email))
                .addField(new WebhookEmbed.EmbedField(true, "Discord Tag", application.discordtag))
                .addField(new WebhookEmbed.EmbedField(true, "Punishment Received", application.punishmentReceived))
                .addField(new WebhookEmbed.EmbedField(false, "Why should your punishment be appealed?", application.whyWeShouldAppeal))
                .build();
        ApplicationBootstrap.getApplication().getDiscord().ifPresent(discord -> discord.getPunishmentAppealWebhook().send(webhookEmbed));
    }
}