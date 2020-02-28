package vn.siten.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.entity.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    Iterable<Student> findAllByClassName(String classname);

    @Query(value = "select s from Student s  where  (:id is null  or s.id =:id)and (:className is null  or s.className = :className)")
    Page<Student> findStudentByIdAndClassName(@Param("id") Long id, @Param("className") String className, Pageable pageable);
}
