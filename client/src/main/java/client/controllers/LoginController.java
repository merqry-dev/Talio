package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.Board;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private Text invalid_text;
    @FXML
    private Button new_board_button;
    @FXML
    private Button join_board_button;
    @FXML
    private TextField code_input;
    @FXML
    private ListView<Board> boards;

    @Inject
    public LoginController(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invalid_text.setVisible(false);
        boards.setItems(FXCollections.observableArrayList(serverUtils.getBoards()));
        boards.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Board> call(ListView<Board> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Board item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }
                        setGraphic(new Label(item.getName()));
                    }
                };
            }
        });
        boards.setOnMouseClicked(event -> {
            Long clickedBoardID = boards.getSelectionModel().getSelectedItem().getId();
            mainCtrl.showClientOverview(clickedBoardID);
        });
        join_board_button.setOnAction(event -> {
            try {
                Long id = Long.parseLong(code_input.getText());
                if(!serverUtils.existsBoardById(id))
                    invalid_text.setVisible(true);
                else
                    mainCtrl.showClientOverview(serverUtils.getBoard(id).getId());
            }catch (NumberFormatException e){
                invalid_text.setVisible(true);
            }
        });
        new_board_button.setOnAction(event -> mainCtrl.showAddBoardPage());
    }
}
