package server.controllers;

import java.util.List;

import models.Tasklist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.repositories.TasklistRepository;

public class TasklistController {

    private final TasklistRepository repo;

    public TasklistController(TasklistRepository repo) {
        this.repo = repo;
    }

    //Draft method with multiboard support. b_id is Multiboard ID. In the Tasklist DB model the ID of the Multiboard it belongs to
    //should also be an attribute
    //    @GetMapping(path = { "", "/{b_id}/" })
    //    public List<Tasklist> getAll() {
    //        return repo.findById(b_id).get();
    //    }

    @GetMapping(path = { "", "/" })
    public List<Tasklist> getAll() {
        return repo.findAll();
    }

    //Draft method with multiboard support.
    //    @GetMapping("/{b_id}/{id}") //not interested in b_id here. I don't know if this will work
    //    public ResponseEntity<Tasklist> getById(@PathVariable("id") long id) {
    //        if (id < 0 || !repo.existsById(id)) {
    //            return ResponseEntity.badRequest().build();
    //        }
    //        return ResponseEntity.ok(repo.findById(id).get());
    //    }

    @GetMapping("/{id}") //not interested in b_id here. I don't know if this will work
    public ResponseEntity<Tasklist> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/{b_id}/" })
    public ResponseEntity<Tasklist> add(@RequestBody Tasklist list) {

        if (list.name == null) {
            return ResponseEntity.badRequest().build();
        }

        Tasklist saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(path = { "", "/{b_id}/" })
    public ResponseEntity<Tasklist> delete(@PathVariable("id") long id) {

        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = { "", "/{b_id}/" })
    public ResponseEntity<Tasklist> update(@PathVariable("id") long id, @RequestBody Tasklist list) {

        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        repo.save(list);
        return ResponseEntity.ok().build();
    }
}