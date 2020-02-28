package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.siten.backend.model.entity.Account;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByUsername(String username);
    Boolean existsByUsername(String username);
    LinkedList<Account> findByEnable(boolean isEnable);
}
