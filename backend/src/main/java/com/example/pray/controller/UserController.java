package cc.sends.pray.controller;

import cc.sends.pray.Entity.Message;
import cc.sends.pray.Entity.User;
import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultCodeEnum;
import cc.sends.pray.result.ResultFactory;
import cc.sends.pray.service.MessageService;
import cc.sends.pray.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Api(value = "Info Controller", tags = "获取用户信息接口")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    final private UserService userService;
    final private MessageService messageService;

    @Autowired
    public UserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @ApiOperation(value = "用户initialized", notes = "initialized", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @PostMapping("/initialized")
    @CrossOrigin
    public Result initialized(@RequestHeader("Token") String token){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        User user = userService.findByOpenId(openId);
        if (user == null) {
            return ResultFactory.buildResult(ResultCodeEnum.USER_NOT_FOUND, "找不到该用户", "");
        }
        log.info("initialized, uid:{}", user.getId());
        userService.setInitialized(user);
        return ResultFactory.buildSuccessResult("初始化成功");
    }

    @ApiOperation(value = "用户基本信息", notes = "initialized与已许愿次数", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/info")
    @CrossOrigin
    public Result info(@RequestHeader("Token") String token){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        User user = userService.findByOpenId(openId);
        log.info("userInfo, uid:{}", user.getId());

        Map<String, Object> data = new TreeMap<>();
        data.put("uid", user.getId());
        data.put("initialized", user.getInitialized());
        data.put("submitNum", user.getSubmitNum());
        return ResultFactory.buildSuccessResult(data);
    }

    @ApiOperation(value = "用户自身的愿望", notes = "自身的愿望", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/wish")
    @CrossOrigin
    public Result wish(@RequestHeader("Token") String token){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        User user = userService.findByOpenId(openId);
        log.info("userWish, uid:{}", user.getId());

        List<Message> messageList = messageService.findMyMessage(user.getId());
        return ResultFactory.buildSuccessResult(messageList);
    }
}
