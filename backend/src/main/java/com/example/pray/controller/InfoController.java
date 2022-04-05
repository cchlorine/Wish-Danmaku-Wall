package cc.sends.pray.controller;

import cc.sends.pray.Entity.Message;
import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultCodeEnum;
import cc.sends.pray.result.ResultFactory;
import cc.sends.pray.service.MessageService;
import cc.sends.pray.service.UserService;
import cc.sends.pray.util.IpAddressUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(value = "Info Controller", tags = "获取信息接口")
@RestController
@RequestMapping("/info")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InfoController {
    final private MessageService messageService;
    final private UserService userService;

    @Autowired
    public InfoController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @ApiOperation(value = "获取排行榜", notes = "最受大家欢迎的愿望", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/rank")
    @CrossOrigin
    public Result getRankMessage(@RequestHeader("Token") String token, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        Integer uid = userService.verifyUid(token);
        log.info("info_rank, uid: {}, ip: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest));

        List<Message> messageList = messageService.findTop8Message();
        return ResultFactory.buildSuccessResult(messageList);
    }

    @ApiOperation(value = "获取最新的愿望", notes = "最新的愿望", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/recent")
    @CrossOrigin
    public Result getRecentMessage(@RequestHeader("Token") String token, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        Integer uid = userService.verifyUid(token);
        log.info("info_recent, uid: {}, ip: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest));

        List<Message> messageList = messageService.findRecentMessage();
        return ResultFactory.buildSuccessResult(messageList);
    }

    @ApiOperation(value = "获取随机愿望", notes = "随机的10条愿望", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/random")
    @CrossOrigin
    public Result getRandomMessage(@RequestHeader("Token") String token, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        Integer uid = userService.verifyUid(token);
        log.info("info_random, uid: {}, ip: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest));

        List<Message> messageList = messageService.findRandomMessage();
        return ResultFactory.buildSuccessResult(messageList);
    }

    @ApiOperation(value = "获取我点赞过的愿望", notes = "我点赞过愿望的message_id", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String")
    })
    @GetMapping("/mySubmit")
    @CrossOrigin
    public Result getMySubmitMessage(@RequestHeader("Token") String token, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        Integer uid = userService.verifyUid(token);
        log.info("info_mySubmit, uid: {}, ip: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest));

        List<Integer> messageList = messageService.findMySubmitMessage(openId);
        return ResultFactory.buildSuccessResult(messageList);
    }
}
