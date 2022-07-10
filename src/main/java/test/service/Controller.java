package test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
public class Controller {
    @Autowired
    private Repository repository;

    @GetMapping("/find/{id}")
    public Mono<DbData> find(@PathVariable String id) {
        return repository.findById(id);
    }
}
