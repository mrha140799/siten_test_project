package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    boolean existsByAccount_Username(String username);
    VerificationToken findByToken(String token);
    VerificationToken findByAccount_Username(String username);
}
