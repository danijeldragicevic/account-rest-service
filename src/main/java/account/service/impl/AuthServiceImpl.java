package account.service.impl;

import account.model.User;
import account.repository.IAuthRepository;
import account.security.impl.UserDetailsImpl;
import account.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IAuthRepository repository;

    @Transactional
    @Override
    public User saveUser(User user) {
        User newUser = repository.save(user);

        return newUser;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = repository.findUserByEmailIgnoreCase(email);

        return user;
    }

    @Transactional
    @Override
    public Optional<User> deleteUserByEmail(String email) {
        Optional<User> deletedUser = repository.deleteUserByEmailIgnoreCase(email);

        return deletedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = repository.findUserByUsernameIgnoreCase(username);

        return new UserDetailsImpl(user.get());
    }

    @Override
    public HashSet<String> getBreachedPasswords() {
        HashSet<String> breachedPsswords = new HashSet<String>();
        breachedPsswords.add("PasswordForJanuary");
        breachedPsswords.add("PasswordForFebruary");
        breachedPsswords.add("PasswordForMarch");
        breachedPsswords.add("PasswordForApril");
        breachedPsswords.add("PasswordForMay");
        breachedPsswords.add("PasswordForJune");
        breachedPsswords.add("PasswordForJuly");
        breachedPsswords.add("PasswordForAugust");
        breachedPsswords.add("PasswordForSeptember");
        breachedPsswords.add("PasswordForOctober");
        breachedPsswords.add("PasswordForNovember");
        breachedPsswords.add("PasswordForDecember");

        return breachedPsswords;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = repository.findAll();

        return users;
    }
}
