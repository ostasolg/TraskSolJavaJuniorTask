package TraskSolTask.service.technologies;

import TraskSolTask.exceptions.NotFoundException;
import TraskSolTask.model.technologies.Technology;
import TraskSolTask.repository.technologies.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;


    @Cacheable(value = "technologies")
    @Transactional
    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }


    @Transactional
    public Technology getTechnologyById(@NotNull Long id) throws NotFoundException {
        Optional<Technology> technologyData = technologyRepository.findById(id);
        if (technologyData.isPresent()) {
            return technologyData.get();
        }
        throw NotFoundException.create("Technology", id);
    }


    @CacheEvict(value = {"technologies"}, allEntries = true)
    @Transactional
    public Technology create(@NotNull Technology technology) {
        return technologyRepository.save(technology);
    }


    @CacheEvict(value = {"technologies"}, allEntries = true)
    @Transactional
    public Technology update(@NotNull Long id, @NotNull Technology newTechnology) {

        Technology oldTechnology = getTechnologyById(id);

        Objects.requireNonNull(newTechnology.getTechnologyName());
        Objects.requireNonNull(newTechnology.getTechnologyLevel());
        Objects.requireNonNull(newTechnology.getNote());
        oldTechnology.setTechnologyName(newTechnology.getTechnologyName());
        oldTechnology.setTechnologyLevel(newTechnology.getTechnologyLevel());
        oldTechnology.setNote(newTechnology.getNote());
        oldTechnology.setDetail(newTechnology.getDetail());
        return technologyRepository.save(oldTechnology);
    }


    @CacheEvict(value = {"technologies"}, allEntries = true)
    @Transactional
    public void delete(@NotNull Long id) {
        Technology technologyToDelete = getTechnologyById(id);
        technologyRepository.delete(technologyToDelete);
    }
}