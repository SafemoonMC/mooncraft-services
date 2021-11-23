package gg.mooncraft.services.restfulweb.endpoints;

import gg.mooncraft.services.restfulweb.endpoints.get.GetPlayer;
import gg.mooncraft.services.restfulweb.endpoints.get.GetServersEach;
import gg.mooncraft.services.restfulweb.endpoints.get.GetServersGroup;
import gg.mooncraft.services.restfulweb.endpoints.post.*;
import io.javalin.http.Handler;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    FORM_REPORT_STAFF_MEMBER("/form/report-staff-member", RequestType.POST, new PostFormReportStaffMember());
    
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
        GET, POST
    }
}