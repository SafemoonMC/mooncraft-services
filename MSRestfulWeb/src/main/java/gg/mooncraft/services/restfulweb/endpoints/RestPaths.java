package gg.mooncraft.services.restfulweb.endpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;

import gg.mooncraft.services.restfulweb.endpoints.get.GetPlayer;
import gg.mooncraft.services.restfulweb.endpoints.get.GetServersEach;
import gg.mooncraft.services.restfulweb.endpoints.get.GetServersGroup;
import gg.mooncraft.services.restfulweb.endpoints.options.CorsOptions;
import gg.mooncraft.services.restfulweb.endpoints.post.PostFormPunishmentAppeal;
import gg.mooncraft.services.restfulweb.endpoints.post.PostFormReportStaffMember;
import gg.mooncraft.services.restfulweb.endpoints.post.PostFormStaffApplication;
import gg.mooncraft.services.restfulweb.endpoints.post.PostVerifyDiscord;
import gg.mooncraft.services.restfulweb.endpoints.post.PostVerifyWallet;
import io.javalin.http.Handler;

@Getter
@AllArgsConstructor
public enum RestPaths {

    PLAYER("/player/{player}", RequestType.GET, new GetPlayer()),
    SERVERS("/servers", RequestType.GET, new GetServersGroup()),
    SERVERS_ALL("/servers/all", RequestType.GET, new GetServersEach()),
    VERIFY_WALLET("/verify/wallet", RequestType.POST, new PostVerifyWallet()),
    VERIFY_DISCORD("/verify/discord", RequestType.POST, new PostVerifyDiscord()),
    FORM_STAFF_APPLICATION("/form/staff-application", RequestType.POST, new PostFormStaffApplication()),
    FORM_PUNISHMENT_APPEAL("/form/punishment-appeal", RequestType.POST, new PostFormPunishmentAppeal()),
    FORM_REPORT_STAFF_MEMBER("/form/report-staff-member", RequestType.POST, new PostFormReportStaffMember()),
    FORM_STAFF_APPLICATION_OPTIONS("/form/staff-application", RequestType.OPTIONS, new CorsOptions()),
    FORM_PUNISHMENT_APPEAL_OPTIONS("/form/punishment-appeal", RequestType.OPTIONS, new CorsOptions()),
    FORM_REPORT_STAFF_MEMBER_OPTIONS("/form/report-staff-member", RequestType.OPTIONS, new CorsOptions());

    /*
    Fields
     */
    private final String path;
    private final RequestType requestType;
    private final Handler handler;


    /*
    Inner
     */
    public enum RequestType {
        GET, POST, OPTIONS
    }
}