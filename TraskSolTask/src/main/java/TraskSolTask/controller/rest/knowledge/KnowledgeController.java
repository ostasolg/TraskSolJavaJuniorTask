package TraskSolTask.controller.rest.knowledge;

import TraskSolTask.model.knowledge.Knowledge;
import TraskSolTask.service.knowledge.KnowledgeService;
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
@RequestMapping("/api/knowledge")
public class KnowledgeController {


    @Autowired
    private KnowledgeService knowledgeService;

    private static final Logger LOG = LoggerFactory.getLogger(KnowledgeController.class);


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllKnowledge() {

        List<Knowledge> knowledge = knowledgeService.getAllKnowledge();
        if (knowledge.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(knowledge, HttpStatus.OK);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getKnowledgeById(@PathVariable("id") Long id) {

        return new ResponseEntity<>(knowledgeService.getKnowledgeById(id), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createKnowledge(@Valid @RequestBody Knowledge knowledge) {

        knowledge = knowledgeService.create(knowledge);
        LOG.debug("Knowledge {} was successfully created.", knowledge);
        return new ResponseEntity<>(knowledge, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKnowledge(@PathVariable Long id, @RequestBody Knowledge newKnowledge) {

        Knowledge updatedKnowledge = knowledgeService.update(id, newKnowledge);
        LOG.debug("Updated knowledge {}.", updatedKnowledge);
        return new ResponseEntity<>(updatedKnowledge, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteKnowledge(@PathVariable("id") Long id) {

        knowledgeService.delete(id);
        LOG.debug("Knowledge with id {} deleted.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}