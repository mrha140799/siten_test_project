package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.PointId;
import vn.siten.backend.model.entity.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, PointId> {
}
