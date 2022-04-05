package cc.sends.pray.controller;

import cc.sends.pray.Entity.*;
import cc.sends.pray.config.App;
import cc.sends.pray.dto.AssessTokenJson;
import cc.sends.pray.dto.Code;
import cc.sends.pray.dto.URLInfo;
import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultFactory;
import cc.sends.pray.service.UserService;
import cc.sends.pray.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(value = "Login Controller" ,tags = "登录授权接口")
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {
    final private UserService userService;
    private App app;

    @Autowired
    public LoginController(UserService userService, App app) {
        this.userService = userService;
        this.app = app;
    }

    @ApiOperation(value = "获取token", notes = "通过微信code来登录，获取token", produces = "application/json")
    @PostMapping("/login")
    @CrossOrigin
    public Result loginByWechatCode(@RequestBody Code code, HttpServletRequest httpServletRequest) throws JSONException {
        // wx login
//        String assessTokenString = "";
//        try {
//            assessTokenString = HttpUtil.doGet(UrlUtil.wechatLogin(App.getAppId(),App.getAppSecret(),code.getCode()), null);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println(assessTokenString);
//        ObjectMapper assessTokenMapper = new ObjectMapper();
//        AssessTokenJson assessTokenJson;
//        try{
//            assessTokenJson = assessTokenMapper.readValue(assessTokenString, AssessTokenJson.class);
//        } catch (Exception e){
//            JSONObject error = new JSONObject(assessTokenString);
//            return ResultFactory.buildResult((int) error.get("errcode"),(String) error.get("errmsg"),"");
//        }

//        without wx
        AssessTokenJson assessTokenJson = new AssessTokenJson();
        assessTokenJson.setOpenid(code.getCode());

        String ipAddress = IpAddressUtil.getIpAddress(httpServletRequest);
        User user = userService.findByOpenId(assessTokenJson.getOpenid());
        if (user == null){
            user = new User();
            user.setOpenid(assessTokenJson.getOpenid());
            user.setSubmitNum(0);
            user.setInitialized(0);
            userService.saveUser(user);
        }
        user = userService.findByOpenId(assessTokenJson.getOpenid());
        String token = JwtUtil.generateToken(assessTokenJson.getOpenid(), user.getId());
        log.info("login, uid:{}, ip: {}", user.getId(), ipAddress);
        return ResultFactory.buildSuccessResult(token);
    }

    @ApiOperation(value = "获取jsSDK", notes = "获取jsSDK, https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html#62", produces = "application/json")
    @PostMapping("/jssdk")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urlInfo", value = "urlInfo", paramType = "body", required = true, dataType = "URLInfo")
    })
    @CrossOrigin
    public Result getJSSDK(@RequestBody URLInfo urlInfo){
        String nonce = StringUtil.getRandomString(13);
        long timestamp = System.currentTimeMillis() / 1000;
        String url = urlInfo.getUrl();
        String signature = SHA1.getSHA1(nonce, app.getJsApiTicket(),String.valueOf(timestamp), url);
        Map<String, Object> data = new HashMap<>();
        data.put("appId", app.getAppId());
        data.put("timestamp", timestamp);
        data.put("nonceStr", nonce);
        data.put("signature", signature);
        return ResultFactory.buildSuccessResult(data);
    }
}
