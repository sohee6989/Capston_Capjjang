package capston.capston_spring.repository;

import capston.capston_spring.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByName(String name);
    boolean existsByEmail(String email);

    @Override
    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByName(String name);
    Optional<AppUser> findByEmail(String email);

}
