package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String id;
    private String fullname;
    private Date birthDate;
    private String idCard;
    private String address;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
    private boolean isMale;
    private String className;
    private String avatarName;


    public Student(String fullname, Date birthDate, String idCard, String address, Faculty faculty, boolean isMale, String className) {
        this.fullname = fullname;
        this.birthDate = birthDate;
        this.idCard = idCard;
        this.address = address;
        this.faculty = faculty;
        this.isMale = isMale;
        this.className = className;
    }
}
