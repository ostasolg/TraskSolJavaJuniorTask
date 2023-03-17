package TraskSolTask.repository.knowledge;

import TraskSolTask.model.knowledge.Knowledge;
import TraskSolTask.model.technologies.Technology;
import TraskSolTask.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

     Set<Knowledge> findByTechnology(@NotNull(message = "Technology must be specified.") Technology technology);
    Set<Knowledge> findByUser(@NotNull(message = "User must be specified.") User user);
}