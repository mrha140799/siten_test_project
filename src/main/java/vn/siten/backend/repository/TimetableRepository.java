package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.TimetableId;
import vn.siten.backend.model.entity.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, TimetableId> {
    boolean existsByTimetableId_Student_Id(Long id);
    Timetable findByTimetableId_Student_IdAndTimetableId_Teacher_Subject_Id(Long studentId, Long subjectId);
}
