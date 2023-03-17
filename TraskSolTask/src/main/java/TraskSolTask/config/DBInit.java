package TraskSolTask.config;

import TraskSolTask.model.knowledge.Knowledge;
import TraskSolTask.model.technologies.Technology;
import TraskSolTask.model.technologies.TechnologyLevel;
import TraskSolTask.model.users.AuthRole;
import TraskSolTask.model.users.User;
import TraskSolTask.repository.knowledge.KnowledgeRepository;
import TraskSolTask.repository.technologies.TechnologyRepository;
import TraskSolTask.repository.users.UserRepository;
import TraskSolTask.service.knowledge.KnowledgeService;
import TraskSolTask.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DBInit implements CommandLineRunner {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;


    @CacheEvict(value = {"technologies", "users", "knowledge"}, allEntries = true)
    @Override
    public void run(String... args) throws Exception {


        // CREATING TECHNOLOGIES

        Technology technology1 = new Technology();
        technology1.setTechnologyName("Spring");
        technology1.setDetail("detail");

        Technology technology2 = new Technology();
        technology2.setTechnologyName("Kafka");
        technology2.setDetail("detail");

        technologyRepository.saveAll(List.of(technology1, technology2));


        // CREATING USERS

        User user = new User();
        user.setFirstName("Jana");
        user.setLastName("Nova");
        user.setPassword("password");
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setTelephoneNumber("23949283849");
        user.setAuthRole(AuthRole.USER);

        User admin = new User();
        admin.setFirstName("Fero");
        admin.setLastName("Smith");
        admin.setPassword("password");
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setTelephoneNumber("345345453453");
        admin.setAuthRole(AuthRole.ADMIN);

        userService.create(user);
        userService.create(admin);


        // ADDING TECHNOLOGIES TO THE USER

        Knowledge knowledge1 = new Knowledge();
        knowledge1.setUser(user);
        knowledge1.setTechnology(technology1);
        knowledge1.setTechnologyLevel(TechnologyLevel.AVERAGE);
        knowledge1.setNote("some note");

        Knowledge knowledge2 = new Knowledge();
        knowledge2.setUser(user);
        knowledge2.setTechnology(technology2);
        knowledge2.setTechnologyLevel(TechnologyLevel.BEGINNER);
        knowledge2.setNote("some note");

        user.setNumberOfTechnologies((short) 2);

        userRepository.save(user);
        knowledgeRepository.saveAll(List.of(knowledge1, knowledge2));

    }
}