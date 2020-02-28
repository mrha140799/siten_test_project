package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int creditNumber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
    private List<Teacher> teachers = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultyId")
    private Faculty faculty;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.subject", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();

    public Subject(String name, int creditNumber, Faculty faculty) {
        this.name = name;
        this.creditNumber = creditNumber;
        this.faculty = faculty;
    }
}
