package cc.sends.pray.service;

import cc.sends.pray.Entity.FavorLog;
import cc.sends.pray.dao.FavorLogDAO;
import cc.sends.pray.dao.MessageDAO;
import cc.sends.pray.Entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    final private MessageDAO messageDAO;
    final private FavorLogDAO favorLogDAO;
    static RecentVector recentVector;

    @Autowired
    public MessageService(MessageDAO messageDAO, FavorLogDAO favorLogDAO) {
        this.messageDAO = messageDAO;
        this.favorLogDAO = favorLogDAO;
        List<Message> messageList = messageDAO.findFirst8ByOrderByCreateTimeDesc();
        recentVector = new RecentVector(messageList);
    }

    public List<Message> findTop8Message(){
        return messageDAO.findFirst8ByOrderByFavorNumDesc();
    }

    public List<Message> findMyMessage(Integer uid){
        return messageDAO.findAllByUid(uid);
    }

    public List<Message> findRecentMessage(){
        return recentVector.getMessageList();
    }

    public List<Message> findRandomMessage(){
        return messageDAO.findRandom();
    }

    public List<Integer> findMySubmitMessage(String openId){
        List<FavorLog> favorLogList = favorLogDAO.findByOpenId(openId);
        List<Integer> messageList = new ArrayList<>();
        for (FavorLog favorLog: favorLogList){
            messageList.add(favorLog.getMessageId());
        }
        return messageList;
    }

    static class RecentVector {
        private List<Message> messageList;
        private int maxSize = 8;
        private int index = maxSize - 1;

        RecentVector(List<Message> messageList) {
            this.messageList = new ArrayList<>(maxSize);
            this.messageList.addAll(messageList);
        }

        synchronized void insert(Message message){
            messageList.set(index, message);
            index = (index + 1) % maxSize;
        }

        List<Message> getMessageList(){
            return messageList;
        }
    }
}
