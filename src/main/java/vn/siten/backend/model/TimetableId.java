package vn.siten.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.siten.backend.model.entity.Student;
import vn.siten.backend.model.entity.Teacher;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class TimetableId implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Student student;
}
