package gg.mooncraft.services.restfulweb.endpoints;

import io.javalin.http.Handler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RestPaths {
    
    PLAYER("/player/{player}", RequestType.GET, null),
    SERVERS("/servers", RequestType.GET, null),
    SERVERS_ALL("/servers/all", RequestType.GET, null),
    VERIFY_WALLET("/verify/wallet", RequestType.POST, ctx -> ctx.result("Not implemented yet.")),
    VERIFY_DISCORD("/verify/discord", RequestType.POST, ctx -> ctx.result("Not implemented yet.")),
    FORM_STAFF_APPLICATION("/form/staff-application", RequestType.POST, null),
    FORM_PUNISHMENT_APPEAL("/form/punishment-appeal", RequestType.POST, null),
    FORM_REPORT_STAFF_MEMBER("/form/report-staff-member", RequestType.POST, null);
    
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