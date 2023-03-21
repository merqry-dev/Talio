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
package client.controllers;

import client.views.ViewFactory;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Board;
import models.TaskList;

public class MainCtrl {

    private Stage primaryStage;
    private Stage addBoardStage;
    private Stage cardStage;

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginPage();
    }

    public void showLoginPage() {
        var loginPage = ViewFactory.createLogin();
        primaryStage.setScene(new Scene(loginPage.getValue()));
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    public void showTaskList(TaskList selectedItem) {
        var taskList = ViewFactory.createTaskList(selectedItem.getId());
        primaryStage.setScene(new Scene(taskList.getValue()));
        primaryStage.setTitle("TaskList");
        primaryStage.show();
    }

    public void showAddBoardPage() {
        var addBoard = ViewFactory.createAddBoard();
        addBoardStage = new Stage();
        addBoardStage.setScene(new Scene(addBoard.getValue()));
        addBoardStage.setTitle("Add Board");
        addBoardStage.initModality(Modality.APPLICATION_MODAL);
        addBoardStage.showAndWait();
    }

    public void showBoard(Board selectedItem) {
        var board = ViewFactory.createBoard(selectedItem.getId());
        primaryStage.setScene(new Scene(board.getValue()));
        primaryStage.setTitle("Board");
        primaryStage.show();
    }

    public void showCard(Long card_id) {
        var card= ViewFactory.createCard(card_id);
        cardStage=new Stage();
        cardStage.setScene(new Scene(card.getValue()));
        cardStage.setTitle("Card Details");
        cardStage.initModality(Modality.APPLICATION_MODAL);
        cardStage.showAndWait();
    }

    public void closeCard() {
        if(cardStage!=null)
            cardStage.close();
    }

    public void showClientOverview(Long boardId) {
        var clientOverview = ViewFactory.createClientOverview(boardId);
        primaryStage.setScene(new Scene(clientOverview.getValue()));
        primaryStage.setTitle("Client Overview");
        primaryStage.setOnCloseRequest(event -> {
            if(clientOverview.getKey()!=null)
                clientOverview.getKey().closePolling();
        });
        primaryStage.show();
    }

    public void closeAddBoard() {
        if(addBoardStage!=null)
            addBoardStage.close();
    }
}
