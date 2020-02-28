package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.siten.backend.model.PointId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "points")
@Data
@NoArgsConstructor
public class Point {
    @Id
    private PointId id;
    private float point;

    public Point(PointId id, float point) {
        this.id = id;
        this.point = point;
    }
}
