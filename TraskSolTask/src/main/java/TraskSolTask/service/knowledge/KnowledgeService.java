package TraskSolTask.service.knowledge;

import TraskSolTask.exceptions.NotFoundException;
import TraskSolTask.model.knowledge.Knowledge;
import TraskSolTask.repository.knowledge.KnowledgeRepository;
import TraskSolTask.repository.users.UserRepository;
import TraskSolTask.service.technologies.TechnologyService;
import TraskSolTask.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TechnologyService technologyService;


    @Cacheable(value = "knowledge")
    @Transactional
    public List<Knowledge> getAllKnowledge() {
        return knowledgeRepository.findAll();
    }


    @Transactional
    public Knowledge getKnowledgeById(@NotNull Long id) throws NotFoundException {

        Optional<Knowledge> knowledgeData = knowledgeRepository.findById(id);
        if (knowledgeData.isPresent()) {
            return knowledgeData.get();
        }
        throw NotFoundException.create("Knowledge", id);
    }


    @CacheEvict(value = {"knowledge"}, allEntries = true)
    @Transactional
    public Knowledge create(@NotNull Knowledge knowledge) {

        // checking if currently logged-in user is authorized to add the technology
        // currently logged-in user with AuthRole User can only add technologies to his/her own account
        userService.validateUserAccess(knowledge.getUser().getId(),
                "data of another user and cannot edit it");

        // checking availability for technology
        if (!canKnowledgeBeAdded(knowledge)) {
            // technology cannot be added to the user
            throw new ValidationException("Each user can have a maximum of 10 technologies.");
        }

        // technology can be added to the user

        // setting correct user instance
        knowledge.setUser(userService.getUserById(knowledge.getUser().getId()));
        // setting correct technology instance
        knowledge.setTechnology(technologyService.getTechnologyById(knowledge.getTechnology().getId()));

        // changing the number of the user's technologies
        knowledge.getUser().setNumberOfTechnologies((short) (knowledge.getUser().getNumberOfTechnologies() + 1));

        userRepository.save(knowledge.getUser());
        knowledge = knowledgeRepository.save(knowledge);
        return knowledge;
    }


    @CacheEvict(value = {"knowledge"}, allEntries = true)
    @Transactional
    public Knowledge update(@NotNull Long id, @NotNull Knowledge newKnowledge) {

        Knowledge oldKnowledge = getKnowledgeById(id);

        // checking if currently logged-in user is authorized to edit the data
        // currently logged-in user with AuthRole User can only edit technologies of his/her own account
        userService.validateUserAccess(oldKnowledge.getUser().getId(),
                "data of another user and cannot edit it");

        // in case users are not the same
        if (!Objects.equals(oldKnowledge.getUser().getId(), newKnowledge.getUser().getId())) {

            // checking if currently logged-in user is authorized to edit the data
            userService.validateUserAccess(newKnowledge.getUser().getId(),
                    "data of another user and cannot edit it");

            // checking new user's availability for technology
            if (!canKnowledgeBeAdded(newKnowledge)) {
                // technology cannot be added to the new user
                throw new ValidationException("Each user can have a maximum of 10 technologies.");
            }

            // technology can be added to the new user

            // setting correct user instances
            oldKnowledge.setUser(userService.getUserById(oldKnowledge.getUser().getId()));
            newKnowledge.setUser(userService.getUserById(newKnowledge.getUser().getId()));

            // changing the number of technologies for both users
            newKnowledge.getUser().setNumberOfTechnologies(
                    (short) (newKnowledge.getUser().getNumberOfTechnologies() + 1));
            oldKnowledge.getUser().setNumberOfTechnologies(
                    (short) (oldKnowledge.getUser().getNumberOfTechnologies() - 1));

            userRepository.save(oldKnowledge.getUser());
            userRepository.save(newKnowledge.getUser());

            oldKnowledge.setUser(newKnowledge.getUser());
        }

        // setting correct technology instance
        newKnowledge.setTechnology(technologyService.getTechnologyById(newKnowledge.getTechnology().getId()));

        oldKnowledge.setTechnology(newKnowledge.getTechnology());
        oldKnowledge.setNote(newKnowledge.getNote());
        oldKnowledge.setTechnologyLevel(newKnowledge.getTechnologyLevel());

        return knowledgeRepository.save(oldKnowledge);
    }


    @CacheEvict(value = {"knowledge", "users"}, allEntries = true)
    @Transactional
    public void delete(@NotNull Long id) {

        Knowledge knowledgeToDelete = getKnowledgeById(id);

        // checking if currently logged-in user is authorized to delete the technology
        // currently logged-in user with AuthRole User can only delete technologies from his/her own account
        userService.validateUserAccess(knowledgeToDelete.getUser().getId(),
                "data of another user and cannot edit it");

        // changing the number of the user's technologies
        knowledgeToDelete.getUser().setNumberOfTechnologies(
                (short) (knowledgeToDelete.getUser().getNumberOfTechnologies() - 1));

        userRepository.save(knowledgeToDelete.getUser());
        knowledgeRepository.delete(knowledgeToDelete);
    }

    public boolean canKnowledgeBeAdded(Knowledge knowledge) {

        // each user can have a maximum of 10 technologies
        return knowledge.getUser() != null && knowledge.getUser().getNumberOfTechnologies() + 1 <= 10;
    }

}
