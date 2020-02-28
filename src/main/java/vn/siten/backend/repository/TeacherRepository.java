package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.siten.backend.model.entity.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByAccount_Username (String username);
    Iterable<Teacher> findAllByAccount_Username(String username);
    Teacher findBySubject_Id(Long id);
    boolean existsById(Long id);
    Iterable<Teacher> findAllByAccount_UsernameNotLike(String username);

}
