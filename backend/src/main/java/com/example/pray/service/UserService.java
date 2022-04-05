package cc.sends.pray.service;

import cc.sends.pray.dao.UserDAO;
import cc.sends.pray.Entity.User;
import cc.sends.pray.result.Result;
import cc.sends.pray.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User findByOpenId(String openId){
        return userDAO.findByOpenid(openId);
    }

    public void saveUser(User user){
        userDAO.save(user);
    }

    public boolean preHandle(String token){
        if (token != null){
            Object flag = JwtUtil.verifyJwt(token);
            if(flag != null){
                return true;
            }
        }
        return false;
    }

    public String verifyOpenId(String token){
        if (JwtUtil.verifyJwt(token) != null) {
            Claims claims = (Claims) JwtUtil.verifyJwt(token);
            return (String) claims.get("openId");
        }
        return null;
    }

    public Integer verifyUid(String token){
        if (JwtUtil.verifyJwt(token) != null) {
            Claims claims = (Claims) JwtUtil.verifyJwt(token);
            try {
                Integer uid = (Integer) claims.get("uid");
                return uid;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void setInitialized(User user){
        user.setInitialized(1);
        userDAO.save(user);
    }
}
