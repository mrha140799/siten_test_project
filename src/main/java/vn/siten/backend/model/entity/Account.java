package vn.siten.backend.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6,max = 255)
    private String password;
    private boolean enable;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "account")
    private VerificationToken verificationToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Account() {
    }

    public Account(@NotBlank String username, @NotBlank @Size(min = 6, max = 255) String password,Teacher teacher, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.enable = false;
        this.teacher =teacher;
    }
}
