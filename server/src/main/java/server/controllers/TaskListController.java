package server.controllers;

import java.util.List;
import java.util.Optional;

import models.TaskCard;
import models.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.repositories.TaskListRepository;
import server.services.TaskListService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/tasklists")
public class TaskListController {

    private final TaskListRepository taskListRepository;
    private final TaskListService taskListService;

    /**
     * Constructor Method
     * @param taskListRepository The injected taskListRepository of the object
     * @param taskListService The injected taskListService of the object
     */
    public TaskListController(TaskListRepository taskListRepository,
                              TaskListService taskListService) {

        this.taskListRepository = taskListRepository;
        this.taskListService = taskListService;
    }

    /**
     * Fetch all method
     * @return  All the existing taskLists
     */
    @GetMapping("")
    public List<TaskList> getAll() {
        return taskListRepository.findAll();
    }

    /**
     * Find by id method
     * @param id The id of the wanted taskList
     * @return A response based on the existence of the taskList
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") Long id) {

        Optional<TaskList> taskList=taskListRepository.findById(id);
        return taskList.map(ResponseEntity::ok).
                            orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Get task cards ids of the task list
     * @param id The id of the task list containing the task cards
     * @return A list of the task cards belonging to the list
     */
    @GetMapping("/{id}/taskcards")
    public ResponseEntity<List<Long>> getTaskCardsId(@PathVariable Long id){
        Optional<TaskList> taskList=taskListRepository.findById(id);
        if(taskList.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(taskListRepository.getTaskCardsId(id));
    }

    /**
     * Get task cards of the task list
     *
     * @param id The id of the task list containing the task cards
     * @return A list of the task cards belonging to the list
     */
    @GetMapping("/taskCards/{id}")
    public ResponseEntity<List<TaskCard>> getTaskCard(@PathVariable Long id) {
        Optional<TaskList> taskList = taskListRepository.findById(id);
        return taskList.map(value -> ResponseEntity.ok(value.getTaskCards())).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Add a new taskList method
     * @param taskList The wanted taskList
     * @param boardId The board it belongs to
     * @return A response based on the existence of the board
     */
    @PostMapping("")
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList,
                                        @PathParam("boardId") Long boardId) {

        return taskListService.add(taskList,boardId);
    }

    /**
     * Update a taskList method
     * @param id The current taskList
     * @param newTaskList The new taskList
     * @return A response based on the existence of the taskList
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskList> update(@PathVariable("id") Long id,
                                           @RequestBody TaskList newTaskList) {

        return taskListService.update(id,newTaskList);
    }

    /**
     * Delete a taskList method
     * @param id The id of the corresponding taskList
     * @return A response based on the existence of the taskList
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskList> delete(@PathVariable("id") Long id) {

        if (!taskListRepository.existsById(id))
            return ResponseEntity.badRequest().build();
        taskListRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}