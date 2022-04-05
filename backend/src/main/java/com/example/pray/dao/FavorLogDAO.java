package cc.sends.pray.dao;

import cc.sends.pray.Entity.FavorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorLogDAO extends JpaRepository<FavorLog, Integer> {
    FavorLog findByOpenIdAndMessageId(String openId, Integer messageId);

    List<FavorLog> findByOpenId(String openId);
}
