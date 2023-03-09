package server.services;

import models.Board;
import models.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import server.repositories.BoardRepository;
import server.repositories.TaskListRepository;

import java.util.Optional;

@Service
public class TaskListService {
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;

    public TaskListService(TaskListRepository taskListRepository, BoardRepository boardRepository) {
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public ResponseEntity<TaskList> update(@PathVariable("id") Long id,
                                           @RequestBody TaskList newTaskList) {
        return taskListRepository.findById(id).map(list -> {
            list.setName(newTaskList.getName());
            list.setTaskCards(newTaskList.getTaskCards());
            return ResponseEntity.ok(taskListRepository.save(list));
        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Transactional
    public ResponseEntity<TaskList> add(TaskList taskList, Long boardId) {
        Optional<Board> optional=boardRepository.findById(boardId);
        if(optional.isEmpty())
            return ResponseEntity.badRequest().build();
        Board board=optional.get();
        board.getTaskLists().add(taskList);
        taskList.setBoard(board);
        return ResponseEntity.ok(taskListRepository.save(taskList));
    }
}