package pi.focus.server.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.focus.server.core.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByLogin(String login);
}
