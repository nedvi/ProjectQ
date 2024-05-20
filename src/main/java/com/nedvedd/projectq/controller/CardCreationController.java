package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class CardCreationController {

    private DataModel dataModel;

    @FXML
    public TextArea cardQuestion;

    @FXML
    public TextArea cardAnswer;

    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel ;
    }

    @FXML
    protected void saveCard() throws IOException {
        Card card = new Card(cardQuestion.getText(), cardAnswer.getText());
        System.out.println(card);

        dataModel.getCurrentFolder().addCard(card);

        HomeController controller = Main.home.getController();
        controller.addCurrentCardToGridPane();

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
}
