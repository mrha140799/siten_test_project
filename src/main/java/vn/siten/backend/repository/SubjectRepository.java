package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Iterable<Subject> findAllByIdNotLike(Long id);

    Subject findByNameIgnoreCase(String name);
}
