package gg.mooncraft.services.restfulweb.endpoints.post.impl;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.endpoints.models.ReportStaffMember;
import gg.mooncraft.services.restfulweb.endpoints.post.AuthHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public final class FormReportStaffMember extends AuthHandler {
    @Override
    public void handleAuthorized(@NotNull Context ctx) {
        ReportStaffMember application = ctx.bodyValidator(ReportStaffMember.class)
                .check(obj -> obj.username.length() < 50 && obj.username.length() > 0, "user")
                .check(obj -> obj.email.length() < 160 && obj.email.length() > 0, "email")
                .check(obj -> obj.discordtag.length() < 60 && obj.discordtag.length() > 0, "discordtag")
                .check(obj -> obj.staffMemberUsername.length() < 60 && obj.staffMemberUsername.length() > 0, "staffMembersUsername")
                .check(obj -> obj.whatHappened.length() < 500 && obj.whatHappened.length() > 0, "whatHappened")
                .get();
        WebhookEmbed webhookEmbed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle("New Staff Report", null))
                .addField(new WebhookEmbed.EmbedField(true, "Username", application.username))
                .addField(new WebhookEmbed.EmbedField(true, "Email", application.email))
                .addField(new WebhookEmbed.EmbedField(true, "Discord Tag", application.discordtag))
                .addField(new WebhookEmbed.EmbedField(true, "Staff members username", application.staffMemberUsername))
                .addField(new WebhookEmbed.EmbedField(false, "What happened? (Evidence, Reasoning, Those involved etc)", application.whatHappened))
                .build();
        ApplicationBootstrap.getApplication().getDiscord().ifPresent(discord -> discord.getReportStaffMemberWebhook().send(webhookEmbed));
    }
}