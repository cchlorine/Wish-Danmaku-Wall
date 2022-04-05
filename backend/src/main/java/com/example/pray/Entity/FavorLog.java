package cc.sends.pray.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity(name = "favor_log")
public class FavorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer id;

    @Column(name = "openid")
    private String openId;

    @Column(name = "message_id")
    private Integer messageId;
}
