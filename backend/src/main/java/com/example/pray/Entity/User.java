package cc.sends.pray.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer id;

    @Column(name = "openid")
    private String openid;

    @Column(name = "submit_num")
    private Integer submitNum;

    @Column(name = "initialized")
    private Integer initialized;
}
