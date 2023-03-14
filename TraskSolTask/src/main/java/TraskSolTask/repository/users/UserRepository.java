package TraskSolTask.repository.users;

import TraskSolTask.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByFirstNameAndLastNameAndEmail(
    @NotBlank @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters.") String firstName,
    @NotBlank @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters.") String lastName,
    @NotBlank @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters.") @Email String email);

    Boolean existsByUsername(String username);
}