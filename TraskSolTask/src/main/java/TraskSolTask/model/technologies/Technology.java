package TraskSolTask.model.technologies;

import TraskSolTask.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technologies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "technologyName")
})
public class Technology extends AbstractEntity {

    @NotBlank
    @Size(min = 1, max = 50, message = "Technology name must be between 1 and 50 characters.")
    private String technologyName;

    @NotBlank
    @Size(min = 1, max = 100, message = "Technology note must be between 1 and 100 characters.")
    private String note;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Technology level must be specified and cannot be null.")
    private TechnologyLevel technologyLevel;

    private String detail;
}
