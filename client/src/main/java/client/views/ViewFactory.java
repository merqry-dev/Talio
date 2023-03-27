package client.views;

import client.controllers.*;
import com.google.inject.Injector;
import javafx.scene.Parent;
import javafx.util.Pair;
import models.Board;

public class ViewFactory {
    private final MyFXML FXML;

    public ViewFactory(Injector injector) {
        FXML = new MyFXML(injector);
    }

    public Pair<BoardController, Parent> createBoard(Board board) {
        return FXML.load(BoardController.class, "/client/board.fxml", board);
    }

    public Pair<StartingPageController, Parent> createStartingPage() {
        return FXML.load(StartingPageController.class, "/client/startingPage.fxml");
    }

    public Pair<ClientMenuController, Parent> createClientMenu(Board board) {
        return FXML.load(ClientMenuController.class, "/client/clientMenu.fxml", board);
    }

    public Pair<AddBoardController, Parent> createAddBoard() {
        return FXML.load(AddBoardController.class, "/client/addBoard.fxml");
    }

    public Pair<EditBoardController, Parent> createEditBoard(Board board) {
        return FXML.load(EditBoardController.class, "/client/editBoard.fxml", board);
    }

    public Pair<ExtendedCardController, Parent> createLogin() {
        return FXML.load(ExtendedCardController.class, "/client/loginPage.fxml");
    }

    public Pair<ClientOverviewController, Parent> createClientOverview(Board board) {
        return FXML.load(ClientOverviewController.class, "/client/clientOverview.fxml", board);
    }

    public Pair<TaskListController, Parent> createTaskList(Long id) {
        return FXML.load(TaskListController.class, "/client/taskList.fxml", id);
    }

    public Pair<ExtendedCardController, Parent> createCard(Long card_id) {
        return FXML.load(ExtendedCardController.class, "/client/card.fxml", card_id);
    }

    public Pair<MinimizedCardController, Parent> createMinimizedCard(Long card_id) {
        return FXML.load(MinimizedCardController.class, "/client/minimizedCard.fxml", card_id);
    }

    public Pair<AddTaskListController, Parent> createAddTaskList(Long board_id) {
        return FXML.load(AddTaskListController.class, "/client/addTaskList.fxml", board_id);
    }
}
