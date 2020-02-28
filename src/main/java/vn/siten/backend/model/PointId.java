package vn.siten.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.siten.backend.model.entity.Student;
import vn.siten.backend.model.entity.Subject;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class PointId implements Serializable {
    @ManyToOne
    private Subject subject;
    @ManyToOne
    private Student student;

}
