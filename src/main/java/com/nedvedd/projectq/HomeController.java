package com.nedvedd.projectq;

import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeController {

    private DataModel dataModel;

    private int currentColumnIndex = 0;
    private int currentRowIndex = 1;


    @FXML
    private Label welcomeText;


    @FXML
    private GridPane gridPane = new GridPane();


    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel ;
    }

    @FXML
    protected void switchSceneToCardCreation() throws IOException {
        Main.switchSceneTo(Main.cardCreation);
    }

    @FXML
    protected void printCards() {
        System.out.println(dataModel.getCards().toString());
    }

    public void updateGridPane() {

        System.out.println("Pocet sloupcu: " + (currentColumnIndex + 1));
        System.out.println("Pocet radku: " + (currentRowIndex + 1));

        // Pidani prvni minikarty
        if (currentColumnIndex == 0 && currentRowIndex == 1) {
            gridPane.getRowConstraints().get(0).setPrefHeight(100);
        }

        gridPane.add(getMiniCard(), currentColumnIndex, currentRowIndex);

        if (currentColumnIndex == 2) {
            System.out.println("Radek pridan");

            currentColumnIndex = 0;

            currentRowIndex++;
            gridPane.addRow(currentRowIndex);

        } else {
            currentColumnIndex++;
        }


    }

    public Node getMiniCard() {
        VBox miniCardVBox = new VBox();

        Card lastCard = dataModel.getCurrentCard();

        Label questionLabel = new Label(lastCard.getQuestion());
        Label answerLabel = new Label(lastCard.getAnswer());

        questionLabel.setStyle(
                "-fx-wrap-text: true;" +
                "-fx-text-fill: WHITE");
        answerLabel.setStyle(
                "-fx-wrap-text: true;" +
                "-fx-text-fill: WHITE");


        miniCardVBox.getChildren().addAll(questionLabel, answerLabel);
        miniCardVBox.setAlignment(Pos.CENTER);
        miniCardVBox.setPadding(new Insets(5));
        miniCardVBox.setSpacing(5);
        miniCardVBox.setStyle("-fx-background-color: #27498c;");
        miniCardVBox.setMinHeight(300);

        return miniCardVBox;
    }

}