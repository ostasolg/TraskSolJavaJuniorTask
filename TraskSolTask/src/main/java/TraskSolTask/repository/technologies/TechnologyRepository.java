package TraskSolTask.repository.technologies;

import TraskSolTask.model.technologies.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    Optional<Technology> findByTechnologyName(String technologyName);
    Boolean existsByTechnologyName(String technologyName);
}