package cc.sends.pray.service;

import cc.sends.pray.dao.FavorLogDAO;
import cc.sends.pray.dao.MessageDAO;
import cc.sends.pray.dao.UserDAO;
import cc.sends.pray.Entity.FavorLog;
import cc.sends.pray.Entity.Message;
import cc.sends.pray.dto.UpMessage;
import cc.sends.pray.Entity.User;
import cc.sends.pray.result.Result;
import cc.sends.pray.result.ResultCodeEnum;
import cc.sends.pray.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SubmitService {
    final private MessageDAO messageDAO;
    final private FavorLogDAO favorLogDAO;
    final private UserDAO userDAO;

    @Autowired
    public SubmitService(MessageDAO messageDAO, FavorLogDAO favorLogDAO, UserDAO userDAO) {
        this.messageDAO = messageDAO;
        this.favorLogDAO = favorLogDAO;
        this.userDAO = userDAO;
    }

    public Result submit(User user, UpMessage upMessage){
        if (user.getSubmitNum() >= 3){
            return ResultFactory.buildResult(ResultCodeEnum.WISH_LIMIT, "只可以许三个愿望哦", "");
        }
        Message message = saveMessage(user.getId(), upMessage);
        user.setSubmitNum(user.getSubmitNum() + 1);
        userDAO.save(user);
        MessageService.recentVector.insert(message);
        return ResultFactory.buildResult(ResultCodeEnum.SUCCESS,"许愿成功","");
    }

    public Result favor(String openId, Integer messageId){
        FavorLog favorLog = favorLogDAO.findByOpenIdAndMessageId(openId, messageId);
        if (favorLog == null){
            favorLog = new FavorLog();
            favorLog.setMessageId(messageId);
            favorLog.setOpenId(openId);
            favorLogDAO.save(favorLog);
        }else {
            return ResultFactory.buildResult(ResultCodeEnum.FAVORED, "你已经为该条愿望点过赞啦", "");
        }

        Optional<Message> messageOptional = messageDAO.findById(messageId);
        Message message;
        if (messageOptional.isPresent()){
            message = messageOptional.get();
        }else {
            return ResultFactory.buildResult(ResultCodeEnum.MESSAGE_NOT_FOUND, "该条消息不存在呢", "");
        }

        message.setFavorNum(message.getFavorNum() + 1);
        messageDAO.save(message);

        return ResultFactory.buildResult(ResultCodeEnum.SUCCESS,"点赞成功","");
    }

    private Message saveMessage(Integer uid, UpMessage upMessage){
        Message message = new Message();
        message.setUid(uid);
        message.setFavorNum(0);
        message.setText(upMessage.getMessage());
        message.setColor(upMessage.getColor());
        message.setPicture(upMessage.getPicture());
        Long createTime = System.currentTimeMillis();
        message.setCreateTime(createTime);
        messageDAO.save(message);
        message = messageDAO.findByUidAndCreateTime(uid, createTime);
        return message;
    }
}
