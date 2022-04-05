package cc.sends.pray.dao;

import cc.sends.pray.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageDAO extends JpaRepository<Message, Integer> {
    List<Message> findFirst8ByOrderByFavorNumDesc();

    List<Message> findAllByUid(Integer uid);

    List<Message> findFirst8ByOrderByCreateTimeDesc();

    @Query(value = "SELECT * FROM message ORDER BY RAND() LIMIT 30", nativeQuery = true)
    List<Message> findRandom();

    Message findByUidAndCreateTime(Integer uid, Long createTime);
}
