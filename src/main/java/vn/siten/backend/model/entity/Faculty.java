package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "faculties")
@Data
@NoArgsConstructor
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String facultyName;
    private Date createdDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "faculty")
    private List<Subject> subjects = new ArrayList<>();


    public Faculty(@NotBlank String facultyName, Date createdDate) {
        this.facultyName = facultyName;
        this.createdDate = createdDate;
    }
}
