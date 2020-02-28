package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.siten.backend.model.entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Iterable<Faculty> findAllByIdNotLike(Long id);

    Faculty findByFacultyNameIgnoreCase(String name);

    boolean existsByFacultyNameIgnoreCase(String name);
}
