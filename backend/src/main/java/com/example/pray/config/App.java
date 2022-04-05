package cc.sends.pray.config;

import cc.sends.pray.util.HttpUtil;
import cc.sends.pray.util.UrlUtil;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class App {
    @Value("${app.id}")
    private String appId;

    @Value("${app.secret}")
    private String appSecret;

    private String accessToken = "";
    private String jsApiTicket = "";

    private long accessTokenExpired = 0L;
    private long jsApiTicketExpired = 0L;

    private void refreshToken(){
        System.out.println(appId);
        System.out.println(appSecret);
        long nowTime = System.currentTimeMillis();
        if (nowTime <= accessTokenExpired && nowTime <= jsApiTicketExpired){
            return;
        }

        String assessTokenString = "";
        try {
            assessTokenString = HttpUtil.doGet(UrlUtil.accessToken(appId,appSecret), null);
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            JSONObject assessTokenJson = new JSONObject(assessTokenString);
            accessToken = (String)assessTokenJson.get("access_token");
            int expiresIn = (int) assessTokenJson.get("expires_in");
            accessTokenExpired = System.currentTimeMillis() + (expiresIn-100) * 1000;
        } catch (Exception e){
            System.out.println(assessTokenString);
            e.printStackTrace();
        }

        String jsApiString = "";
        try {
            jsApiString = HttpUtil.doGet(UrlUtil.jsApiTicket(accessToken), null);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject jsApiJson = new JSONObject(jsApiString);
            jsApiTicket = (String)jsApiJson.get("ticket");
            int expiresIn = (int) jsApiJson.get("expires_in");
            jsApiTicketExpired = System.currentTimeMillis() + (expiresIn-100) * 1000;
        }catch (Exception e){
            System.out.println(jsApiString);
            e.printStackTrace();
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getAccessToken() {
        refreshToken();
        return accessToken;
    }

    public String getJsApiTicket() {
        refreshToken();
        return jsApiTicket;
    }
}
