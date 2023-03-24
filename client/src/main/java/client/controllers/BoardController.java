package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import models.Board;

import java.net.URL;
import java.util.*;

public class BoardController implements Initializable {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Long boardId;
    private Map<Long, Parent> cache;
    private Timer timer;
    private final List<TaskListController> taskListControllers = new ArrayList<>();
    private final StringProperty nameProperty = new SimpleStringProperty();
    @FXML
    private FlowPane board_parent;
    @FXML
    private Button addList_button;
    @FXML
    private AnchorPane overlay;

    @Inject
    public BoardController(ServerUtils serverUtils, MainCtrl mainCtrl, Long boardId) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.boardId = boardId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        overlay.setVisible(false);

        board_parent.setHgap(30);
        board_parent.setVgap(30);

        cache = new HashMap<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 500);

        addList_button.setOnMouseClicked(event -> {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            overlay.setPrefWidth(bounds.getWidth());
            overlay.setPrefHeight(bounds.getHeight());
            overlay.setVisible(true);
            mainCtrl.showAddTaskListPage(boardId);
            overlay.setVisible(false);
        });
    }

    private void update() {
        try {
            Board board = serverUtils.getBoard(boardId);
            List<Long> taskListsId = serverUtils.getTaskListsId(boardId);

            nameProperty.set(board.getName());

            List<Parent> list = new ArrayList<>();

            for (var id : taskListsId) {
                if (!cache.containsKey(id)) {
                    var taskListPair = mainCtrl.createTaskList(id);

                    taskListControllers.add(taskListPair.getKey());
                    cache.put(id, taskListPair.getValue());
                }
                list.add(cache.get(id));
            }
            Platform.runLater(() -> board_parent.getChildren().setAll(list));
        } catch (WebApplicationException e) {
            closePolling();

            Platform.runLater(mainCtrl::showLoginPage);
        }
    }

    public void closePolling() {
        timer.cancel();
        for (TaskListController taskListController : taskListControllers)
            if (taskListController != null)
                taskListController.closePolling();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }
}
