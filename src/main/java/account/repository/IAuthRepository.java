package account.repository;

import account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findUserByEmailIgnoreCase(String email);

    Optional<User> deleteUserByEmailIgnoreCase(String email);

    Optional<User> findUserByUsernameIgnoreCase(String username);
}
