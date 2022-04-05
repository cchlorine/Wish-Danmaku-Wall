package cc.sends.pray.controller;

import cc.sends.pray.dto.MessageId;
import cc.sends.pray.dto.UpMessage;
import cc.sends.pray.Entity.User;
import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultCodeEnum;
import cc.sends.pray.result.ResultFactory;
import cc.sends.pray.service.SubmitService;
import cc.sends.pray.service.UserService;
import cc.sends.pray.util.IpAddressUtil;
import cc.sends.pray.util.CertainUtil;
import cc.sends.pray.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(value = "Submit Controller", tags = "提交信息接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubmitController {
    final private SubmitService submitService;
    final private UserService userService;

    @Autowired
    public SubmitController(SubmitService submitService, UserService userService) {
        this.submitService = submitService;
        this.userService = userService;
    }

    @ApiOperation(value = "上传我的愿望", notes = "上传一条我的愿望", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "upMessage", value = "愿望文本", paramType = "body", required = true, dataType = "UpMessage")
    })
    @PostMapping("/submit")
    @CrossOrigin
    public Result submit(@RequestHeader("Token") String token, @RequestBody UpMessage upMessage, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        Integer uid = userService.verifyUid(token);
        log.info("submit, uid:{}, ip: {}, message: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest), upMessage.getMessage());
        if (uid == null) {
            return ResultFactory.buildResult(ResultCodeEnum.USER_NOT_FOUND, "找不到该用户", "");
        }

        User user = userService.findByOpenId(openId);

        if (user == null){
            return ResultFactory.buildResult(ResultCodeEnum.USER_NOT_FOUND, "找不到该用户", "");
        }

        if (StringUtil.isEmpty(upMessage.getMessage())){
            return ResultFactory.buildResult(ResultCodeEnum.WISH_EMPTY, "愿望不可以为空", "");
        }

        if (CertainUtil.contains(upMessage.getMessage())){
            return ResultFactory.buildResult(ResultCodeEnum.ILLEGAL, "愿望中包含敏感词", "");
        }

        if (upMessage.getMessage().length() > 20){
            return ResultFactory.buildResult(ResultCodeEnum.WISH_LENGTH_ERROR, "输入的文本长度不可以超过20个字", "");
        }

        return submitService.submit(user, upMessage);
    }

    @ApiOperation(value = "点赞", notes = "为制定消息点赞", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "Token", paramType = "header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "messageId", value = "点赞的愿望id", paramType = "body", required = true, dataType = "MessageId")
    })
    @PostMapping("/favor")
    public Result favor(@RequestHeader("Token") String token, @RequestBody MessageId messageId, HttpServletRequest httpServletRequest){
        if (!userService.preHandle(token)){
            return ResultFactory.buildResult(ResultCodeEnum.TOKEN_ERROR,"Token出错","");
        }
        String openId = userService.verifyOpenId(token);
        Integer uid = userService.verifyUid(token);
        log.info("favor, uid:{}, ip: {}, favor_message: {}", uid, IpAddressUtil.getIpAddress(httpServletRequest), messageId.getId());

        return submitService.favor(openId, messageId.getId());
    }
}
