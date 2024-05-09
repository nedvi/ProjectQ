package com.nedvedd.projectq;

import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CardCreationController {

    private DataModel dataModel;

    @FXML
    public TextField cardName;

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

        Card card = new Card(cardName.getText(), cardQuestion.getText(), cardAnswer.getText());
        System.out.println(card);

        dataModel.getCards().add(card);
        dataModel.setCurrentCard(card);

        HomeController controller = Main.home.getController();
        controller.updateGridPane();

        Main.switchSceneTo(Main.home);
    }
}
