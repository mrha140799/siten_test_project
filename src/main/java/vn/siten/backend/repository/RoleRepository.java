package vn.siten.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.siten.backend.model.entity.Role;
import vn.siten.backend.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
