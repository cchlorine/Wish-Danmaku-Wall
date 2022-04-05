package cc.sends.pray.util;

public class UrlUtil {

    public static String wechatLogin(String access_token, String openid){
        return "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
    }

    public static String wechatLogin(String appid, String secret, String code){
        return  "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
    }

    public static String accessToken(String appid, String secret){
        return "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
    }

    public static String jsApiTicket(String accessToken){
        return "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";
    }
}
