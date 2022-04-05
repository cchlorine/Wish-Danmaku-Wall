package cc.sends.pray.Entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Integer messageId;

    @Column(name = "text")
    private String text;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "favor_num")
    private Integer favorNum;

    @Column(name = "color")
    private Integer color;

    @Column(name = "picture")
    private Integer picture;

    @Column(name = "create_time")
    private Long createTime;
}
