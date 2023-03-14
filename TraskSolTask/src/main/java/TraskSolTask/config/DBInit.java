package TraskSolTask.config;

import TraskSolTask.model.technologies.Technology;
import TraskSolTask.model.technologies.TechnologyLevel;
import TraskSolTask.model.users.AuthRole;
import TraskSolTask.model.users.User;
import TraskSolTask.repository.technologies.TechnologyRepository;
import TraskSolTask.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;


@Service
public class DBInit implements CommandLineRunner {


    @Autowired
    UserService userService;
    @Autowired
    TechnologyRepository technologyRepository;


    @CacheEvict(value = {"technologies", "users"}, allEntries = true)
    @Override
    public void run(String... args) throws Exception {


        // CREATE TECHNOLOGIES

        Technology technology1 = new Technology();
        technology1.setTechnologyName("Spring");
        technology1.setDetail("detail");
        technology1.setNote("Some note");
        technology1.setTechnologyLevel(TechnologyLevel.AVERAGE);

        Technology technology2 = new Technology();
        technology2.setTechnologyName("Kafka");
        technology2.setDetail("detail");
        technology2.setNote("Some note");
        technology2.setTechnologyLevel(TechnologyLevel.BEGINNER);


        technologyRepository.saveAll(List.of(technology1, technology2));


        // CREATE USERS

        User user = new User();
        user.setFirstName("Jana");
        user.setLastName("Nova");
        user.setPassword("password");
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setTelephoneNumber("23949283849");
        user.setAuthRole(AuthRole.USER);
        user.setTechnologies(Set.of(technology1, technology2));

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
    }
}