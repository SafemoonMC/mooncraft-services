package gg.mooncraft.services.restfulweb.discord;

import club.minnced.discord.webhook.WebhookClient;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.utilities.StringUtilities;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Discord {
    
    /*
    Fields
     */
    private final @NotNull WebhookClient punishmentAppealWebhook;
    private final @NotNull WebhookClient reportStaffMemberWebhook;
    private final @NotNull WebhookClient staffApplicationWebhook;
    
    /*
    Constructor
     */
    public Discord() {
        String staffApplicationWebhook = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getProperty("discord.staff-application-webhook")).orElse(null);
        String punishmentAppealWebhook = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getProperty("discord.punishment-appeal-webhook")).orElse(null);
        String reportStaffMemberWebhook = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getProperty("discord.report-staff-member-webhook")).orElse(null);
        
        // Check if properties are correct
        if (!StringUtilities.hasLetters(staffApplicationWebhook)) {
            throw new IllegalArgumentException("The property discord.staff-application-webhook is not valid.");
        }
        if (!StringUtilities.hasLetters(punishmentAppealWebhook)) {
            throw new IllegalArgumentException("The property discord.punishment-appeal-webhook is not valid.");
        }
        if (!StringUtilities.hasLetters(reportStaffMemberWebhook)) {
            throw new IllegalArgumentException("The property discord.report-staff-member-webhook is not valid.");
        }
        
        // Initialize Webhooks
        this.staffApplicationWebhook = WebhookClient.withUrl(staffApplicationWebhook);
        this.punishmentAppealWebhook = WebhookClient.withUrl(punishmentAppealWebhook);
        this.reportStaffMemberWebhook = WebhookClient.withUrl(reportStaffMemberWebhook);
    }
}