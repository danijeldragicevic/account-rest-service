package account.service;

import account.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface IAuthService extends UserDetailsService {
    User saveUser(User user);

    List<User> getAllUsers();

    Optional<User> findUserByEmail(String email);

    Optional<User> deleteUserByEmail(String email);

    HashSet<String> getBreachedPasswords();
}
