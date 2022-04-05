package cc.sends.pray.dao;

import cc.sends.pray.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByOpenid(String openId);
}
