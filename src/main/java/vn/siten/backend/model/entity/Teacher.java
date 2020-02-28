package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private Date birthdate;
    private String idCard;
    private String address;
    private String email;
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    private boolean isMale;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "teacher", fetch = FetchType.LAZY)
    private Account account;

    private String className;

    public Teacher(String fullname, Date birthdate, String idCard, String address, String email, Faculty faculty, Subject subject, boolean isMale, String className) {
        this.email = email;
        this.fullname = fullname;
        this.birthdate = birthdate;
        this.idCard = idCard;
        this.address = address;
        this.faculty = faculty;
        this.subject = subject;
        this.isMale = isMale;
        this.className = className;
    }
}
