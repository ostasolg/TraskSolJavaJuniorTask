package TraskSolTask.service.technologies;

import TraskSolTask.exceptions.NotFoundException;
import TraskSolTask.model.knowledge.Knowledge;
import TraskSolTask.model.technologies.Technology;
import TraskSolTask.repository.knowledge.KnowledgeRepository;
import TraskSolTask.repository.technologies.TechnologyRepository;
import TraskSolTask.service.knowledge.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private KnowledgeRepository knowledgeRepository;


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


    @CacheEvict(value = {"technologies", "knowledge"}, allEntries = true)
    @Transactional
    public Technology update(@NotNull Long id, @NotNull Technology newTechnology) {

        Technology oldTechnology = getTechnologyById(id);

        Objects.requireNonNull(newTechnology.getTechnologyName());
        oldTechnology.setTechnologyName(newTechnology.getTechnologyName());
        oldTechnology.setDetail(newTechnology.getDetail());
        return technologyRepository.save(oldTechnology);
    }


    @CacheEvict(value = {"technologies", "knowledge", "users"}, allEntries = true)
    @Transactional
    public void delete(@NotNull Long id) {
        Technology technologyToDelete = getTechnologyById(id);

        // deleting all the technology - user relationships with the given technology
        Set<Knowledge> allKnowledge = knowledgeRepository.findByTechnology(technologyToDelete);
        allKnowledge.forEach(knowledge -> knowledgeService.delete(knowledge.getId()));

        // deleting the technology
        technologyRepository.delete(technologyToDelete);
    }
}