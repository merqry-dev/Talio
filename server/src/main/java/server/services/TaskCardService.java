package server.services;

import models.TaskCard;
import models.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import server.repositories.TaskCardRepository;
import server.repositories.TaskListRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TaskCardService {
    private final TaskCardRepository taskCardRepository;
    private final TaskListRepository taskListRepository;

    public TaskCardService(TaskCardRepository taskCardRepository, TaskListRepository taskListRepository) {
        this.taskCardRepository = taskCardRepository;
        this.taskListRepository = taskListRepository;
    }

    public List<TaskCard> findAll() {
        return taskCardRepository.findAll();
    }

    public ResponseEntity<TaskCard> getById(@PathVariable("id") Long id) {
        if (!taskCardRepository.existsById(id))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(taskCardRepository.getById(id));
    }

    public ResponseEntity<TaskCard> delete(@PathVariable("id") long id) {
        if (!taskCardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        taskCardRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<TaskCard> update(@PathVariable("id") Long id,
                                           @RequestBody TaskCard newTask) {
        return taskCardRepository.findById(id).map(task -> {
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            return ResponseEntity.ok(taskCardRepository.save(task));
        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Transactional
    public ResponseEntity<TaskCard> add(TaskCard taskCard, Long taskListId) {
        Optional<TaskList> optional = taskListRepository.findById(taskListId);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = optional.get();
        taskList.getTaskCards().add(taskCard);
        taskCard.setTaskList(taskList);
        return ResponseEntity.ok(taskCardRepository.save(taskCard));
    }
}