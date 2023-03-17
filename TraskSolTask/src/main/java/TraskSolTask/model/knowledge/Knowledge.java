package TraskSolTask.model.knowledge;

import TraskSolTask.model.AbstractEntity;
import TraskSolTask.model.technologies.Technology;
import TraskSolTask.model.technologies.TechnologyLevel;
import TraskSolTask.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class represents a relationship between User and Technology entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "knowledge", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "technology_id"})
})
public class Knowledge extends AbstractEntity {

    @ManyToOne
    @NotNull(message = "User must be specified.")
    private User user;

    @ManyToOne
    @NotNull(message = "Technology must be specified.")
    private Technology technology;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Technology level must be specified and cannot be null.")
    private TechnologyLevel technologyLevel;

    @NotBlank
    @Size(min = 1, max = 100, message = "Technology note must be between 1 and 100 characters.")
    private String note;

}
