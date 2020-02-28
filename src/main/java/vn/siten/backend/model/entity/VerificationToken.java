package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date createdDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public void setToken(String token) {
        this.createdDate = new Date();
        this.token = token;
    }

    public VerificationToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.createdDate = new Date();
    }
}
