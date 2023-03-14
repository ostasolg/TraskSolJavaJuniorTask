package TraskSolTask.service.users;

import TraskSolTask.model.users.User;
import TraskSolTask.repository.users.UserRepository;
import TraskSolTask.exceptions.NotFoundException;
import TraskSolTask.exceptions.UserAccessException;
import TraskSolTask.model.users.AuthRole;
import TraskSolTask.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Cacheable(value = "users")
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public User getUserByUsername(@NotNull String username) throws NotFoundException {

        Optional<User> userData = userRepository.findByUsername(username);
        if (userData.isPresent()) {
            User user = userData.get();
            validateUserAccess(user.getId(), "data of another user");
            return user;
        }
        throw NotFoundException.create("User", username);
    }


    @Transactional
    public User getUserById(@NotNull Long id) throws NotFoundException {

        validateUserAccess(id, "data of another user");
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            return userData.get();
        }
        throw NotFoundException.create("User", id);
    }


    @CacheEvict(value = {"users"}, allEntries = true)
    @Transactional
    public User create(@NotNull User user) {
        String newPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);
        if (user.getTechnologies() != null && user.getTechnologies().size() > 10) {
            throw new ValidationException("A user can have a maximum of 10 technologies.");
        }
        user = userRepository.save(user);
        return user;
    }


    @CacheEvict(value = {"users"}, allEntries = true)
    @Transactional
    public User update(@NotNull Long id, @NotNull User newUser) {

        validateUserAccess(id, "data of another user and cannot edit it");
        User oldUser = getUserById(id);

        oldUser.setUsername(newUser.getUsername());
        oldUser.setAuthRole(newUser.getAuthRole());

        if (newUser.getPassword() != null && newUser.getPassword().length() != 0) {
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        oldUser.setEmail(newUser.getEmail());
        oldUser.setTelephoneNumber(newUser.getTelephoneNumber());
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());

        if (newUser.getTechnologies() != null && newUser.getTechnologies().size() <= 10) {
            oldUser.setTechnologies(newUser.getTechnologies());
        }

        return userRepository.save(oldUser);
    }


    @CacheEvict(value = {"users"}, allEntries = true)
    @Transactional
    public void delete(@NotNull Long id) {
        User userToDelete = getUserById(id);
        userRepository.delete(userToDelete);
    }


    public void validateUserAccess(@NotNull Long id, String errorMessage) throws UserAccessException {

        if (!Utils.getCurrent().getId().equals(id) &&
                Utils.getCurrentAuthRole().equals(AuthRole.USER)) {
            throw new UserAccessException("User with authentication role USER has no access to "
                    + errorMessage + ".");
        }
    }
}