package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class CardCreationController extends AController {
    @FXML
    private TextArea cardQuestion;

    @FXML
    private TextArea cardAnswer;

    private boolean editing = false;

    @FXML
    private void saveCard() throws IOException {
        HomeController homeController = Main.home.getController();

        if (editing) {  // pouze editace karty
            dataModel.getCurrentFolder().getCurrentCard().setQuestion(cardQuestion.getText());
            dataModel.getCurrentFolder().getCurrentCard().setAnswer(cardAnswer.getText());
            homeController.updateMiniCards(dataModel.getCurrentFolder());
            editing = false;
        } else {    // vytvoreni nove karty
            Card card = new Card(cardQuestion.getText(), cardAnswer.getText());
            System.out.println(card);

            dataModel.getCurrentFolder().addCard(card);
            homeController.addCurrentCardToGridPane();
        }

        Main.switchSceneTo(Main.home);
        emptyInput();
    }

    @FXML
    private void cancel() throws IOException {
        Main.switchSceneTo(Main.home);
        emptyInput();
    }

    private void emptyInput() {
        cardQuestion.setText("");
        cardAnswer.setText("");
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public void loadCurrentCard() {
        cardQuestion.setText(dataModel.getCurrentFolder().getCurrentCard().getQuestion());
        cardAnswer.setText(dataModel.getCurrentFolder().getCurrentCard().getAnswer());
    }
}
