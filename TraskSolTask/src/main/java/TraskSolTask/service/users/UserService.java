package TraskSolTask.service.users;

import TraskSolTask.model.users.User;
import TraskSolTask.repository.knowledge.KnowledgeRepository;
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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KnowledgeRepository knowledgeRepository;


    @Cacheable(value = "users")
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public User getUserByUsername(@NotNull String username) throws NotFoundException {

        Optional<User> userData = userRepository.findByUsername(username);
        if (userData.isPresent()) {
            return userData.get();
        }
        throw NotFoundException.create("User", username);
    }


    @Transactional
    public User getUserById(@NotNull Long id) throws NotFoundException {

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
        user = userRepository.save(user);
        return user;
    }


    @CacheEvict(value = {"users", "knowledge"}, allEntries = true)
    @Transactional
    public User update(@NotNull Long id, @NotNull User newUser) {

        validateUserAccess(id, "data of another user and cannot edit it");

        User oldUser = getUserById(id);

        // checking if user's password is also being updated
        if (newUser.getPassword() != null && newUser.getPassword().length() != 0) {
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        oldUser.setUsername(newUser.getUsername());

        // checking if currently logged-in user has AuthRole Admin
        if (Utils.getCurrentAuthRole().equals(AuthRole.ADMIN)) {
            // editing AuthRole of the user
            oldUser.setAuthRole(newUser.getAuthRole());
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setTelephoneNumber(newUser.getTelephoneNumber());
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());

        return userRepository.save(oldUser);
    }


    @CacheEvict(value = {"users", "knowledge"}, allEntries = true)
    @Transactional
    public void delete(@NotNull Long id) {
        User userToDelete = getUserById(id);

        // deleting all the technology - user relationships with the given user
        knowledgeRepository.deleteAll(knowledgeRepository.findByUser(userToDelete));
        // deleting the user
        userRepository.delete(userToDelete);
    }


    public void validateUserAccess(@NotNull Long id, String errorMessage) throws UserAccessException {

        // checking if currently logged-in user can have access to user's data
        if (!Utils.getCurrent().getId().equals(id) &&
                Utils.getCurrentAuthRole().equals(AuthRole.USER)) {
            // currently logged-in user cannot have access to another user's data
            throw new UserAccessException("User with authentication role USER has no access to "
                    + errorMessage + ".");
        }
    }
}