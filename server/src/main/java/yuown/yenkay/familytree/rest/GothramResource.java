package yuown.yenkay.familytree.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuown.yenkay.familytree.model.Gothram;
import yuown.yenkay.familytree.repos.GothramRepository;

@RestController
@RequestMapping("/api/gothram")
public class GothramResource {

    private static final Logger logger = LoggerFactory.getLogger(GothramResource.class);

    @Autowired
    private GothramRepository gothramRepository;

    @PostMapping
    @PutMapping
    public ResponseEntity<Gothram> save(@RequestBody Gothram gothram) {
        ResponseEntity<Gothram> response = null;
        try {
            Gothram saved = gothramRepository.save(gothram);
            response = new ResponseEntity<Gothram>(saved, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<Gothram>(HttpStatus.BAD_REQUEST);
            response.getHeaders().add("reason", e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/{gothram}")
    public ResponseEntity<Gothram> delete(@PathVariable("gothram") Long gothram) {
        ResponseEntity<Gothram> response = null;
        try {
            gothramRepository.deleteById(gothram);
            response = new ResponseEntity<Gothram>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<Gothram>(HttpStatus.BAD_REQUEST);
            response.getHeaders().add("reason", e.getMessage());
        }
        return response;
    }

    @GetMapping
    public Page<Gothram> getAll(Pageable p) {
        Page<Gothram> all = gothramRepository.findAll(p);
        return all;
    }
}
