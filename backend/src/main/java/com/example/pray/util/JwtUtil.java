package cc.sends.pray.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {

    //token过期时间 /s
    private static final long TOKEN_EXPIRED_TIME = 24*60*60*1000;

    //jwt 密钥
    private static final String Jwt_Secret = "sends";
    public static String Jwt_Id = "great";


    public static String createJwt(Map<String, Object> claims, Long time){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date now = new Date(System.currentTimeMillis());

        SecretKey secretKey = generalKey();

        Map header = new HashMap();
        header.put("alg", "HS256");
        header.put("type","JWT");

        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                //.setId(jwtId)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setHeader(header)  //设置header
                .signWith(signatureAlgorithm, secretKey);//设置签名使用的签名算法和签名使用的秘钥
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();

    }

//    public static Claims verifyJwt(String token) {
//        //签名秘钥，和生成的签名的秘钥一模一样
//        SecretKey key = generalKey();
//        Claims claims;
//        try {
//            claims = Jwts.parser()  //得到DefaultJwtParser
//                    .setSigningKey(key)         //设置签名的秘钥
//                    .parseClaimsJws(token).getBody();
//        } catch (Exception e) {
//            claims = null;
//        }//设置需要解析的jwt
//        return claims;
//    }

    public static Object verifyJwt(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        Claims claims;
        try {
            claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)         //设置签名的秘钥
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }//设置需要解析的jwt
        return claims;
    }

    //由字符串生成加密key
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64UrlCodec.BASE64URL.decode(Jwt_Secret);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HS256");
        return key;
    }


    public static String generateToken(String openId, Integer uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("openId",openId);
        map.put("uid", uid);
        return createJwt(map, TOKEN_EXPIRED_TIME);
    }
}
