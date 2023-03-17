package TraskSolTask.model.users;

import TraskSolTask.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@JsonIgnoreProperties(value={ "password"}, allowSetters = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users",  uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email"}),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "telephoneNumber")
})
public class User extends AbstractEntity {

    @NotBlank
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters.")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters.")
    private String lastName;

    @NotBlank
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters.")
    @Email
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Authorisation role must be specified and cannot be null.")
    private AuthRole authRole;

    @OrderBy
    @NotBlank
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters.")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "Password must be between 5 and 120 characters.")
    @Size(min = 5, max = 120, message = "Password must be between 5 and 120 characters.")
    private String password;

    private String telephoneNumber;

    @NotNull
    private short numberOfTechnologies = 0;
}