/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;

import mocks.TestBoardRepository;
import mocks.TestTaskListRepository;
import models.Board;
import models.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.controllers.TaskListController;
import server.services.TaskListService;


public class TaskListControllerTest {

    private TaskListController sut;
    private Board board;
    private final List<TaskList> tasks = new ArrayList<>();

    @BeforeEach
    public void setup() {
        board = new Board();
        TestBoardRepository board_repo = new TestBoardRepository();
        board_repo.save(board);

        TestTaskListRepository repo = new TestTaskListRepository();
        TaskListService service = new TaskListService(repo, board_repo);
        sut = new TaskListController(repo, service);

        for(int i = 0; i < 2; i++){
            tasks.add(new TaskList());
            repo.save(tasks.get(i));
        }

    }

    @Test
    public void addTaskListSuccess() {
        var actual = sut.add(new TaskList("null", board), board.getId());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void addTaskListSuccess2() {
        var actual = sut.add(new TaskList(null, board), board.getId());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getIdTest() {

        assertSame(tasks.get(0), sut.findById(tasks.get(0).getId()).getBody());

    }

    @Test
    public void getIdTestFail() {
        assertEquals(BAD_REQUEST, sut.findById(2L).getStatusCode());
    }

    @Test
    public void getAllTest() {
        List<TaskList> test_list = sut.getAll();

        for(int i = 0; i < 1; i++){
            assertEquals(test_list.get(i), tasks.get(i));
        }
    }

    @Test
    public void updateTest() {
        TaskList updated = tasks.get(0);
        updated.setName("updated");
        ResponseEntity<TaskList> response=sut.findById(updated.getId());
        assertNotNull(response.getBody());
        assertEquals(updated.getName(), response.getBody().getName());
    }
    @Test
    public void updateTestFalse() {
        sut.update(0L,tasks.get(1));
        assertEquals(tasks.get(1),sut.getAll().get(0));
    }


    @Test
    public void deleteTest() {
        sut.delete(0L);
        assertFalse(sut.getAll().contains(tasks.get(0)));
    }
    @Test
    public void deleteTestFalse() {
        ResponseEntity<TaskList> response=sut.delete(5L);
        assertEquals(BAD_REQUEST,response.getStatusCode());
    }


}