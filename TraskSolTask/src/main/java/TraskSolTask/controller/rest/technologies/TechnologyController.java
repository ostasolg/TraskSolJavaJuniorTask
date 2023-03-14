package TraskSolTask.controller.rest.technologies;

import TraskSolTask.model.technologies.Technology;
import TraskSolTask.service.technologies.TechnologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {


    @Autowired
    private TechnologyService technologyService;

    private static final Logger LOG = LoggerFactory.getLogger(TechnologyController.class);


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTechnologies() {

        List<Technology> technologies = technologyService.getAllTechnologies();
        if (technologies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(technologies, HttpStatus.OK);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTechnologyById(@PathVariable("id") Long id) {

        return new ResponseEntity<>(technologyService.getTechnologyById(id), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTechnology(@Valid @RequestBody Technology technology) {

        technology = technologyService.create(technology);
        LOG.debug("Technology {} was successfully created.", technology);
        return new ResponseEntity<>(technology, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTechnology(@PathVariable Long id, @RequestBody Technology newTechnology) {

        Technology updatedTechnology = technologyService.update(id, newTechnology);
        LOG.debug("Updated technology {}.", updatedTechnology);
        return new ResponseEntity<>(updatedTechnology, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTechnology(@PathVariable("id") Long id) {

        technologyService.delete(id);
        LOG.debug("Technology with id {} deleted.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}