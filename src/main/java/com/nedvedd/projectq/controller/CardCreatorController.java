package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.model.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

/**
 * Controller pro tvorbu novych/editaci existujicich karet
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class CardCreatorController extends AController {

    /** Boolean hodnota pro rozeznani modu editace/tvorby karty */
    private boolean editing = false;

    /** Reference na textove pole pro zneni otazky v GUI */
    @FXML
    private TextArea cardQuestion;

    /** Reference na textove pole pro zneni odpovedi v GUI */
    @FXML
    private TextArea cardAnswer;

    /**
     * Akce pro tlacitko ulozeni karty
     *
     * @throws IOException vyjimka pri nenactenem HomeLoaderu
     */
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
            System.out.println("LOG:\tNew " + card);

            dataModel.getCurrentFolder().addCard(card);
            homeController.addCurrentCardToGridPane();
        }

        Main.switchSceneTo(Main.home);
        emptyInput();
    }

    /**
     * Akce pro tlacitko zruseni tvorby/editace karty
     *
     * @throws IOException vyjimka pri nenactenem HomeLoaderu
     */
    @FXML
    private void cancel() throws IOException {
        Main.switchSceneTo(Main.home);
        emptyInput();
        editing = false;
    }

    /**
     * Vymazani textovych poli po dokonceni akce
     */
    private void emptyInput() {
        cardQuestion.setText("");
        cardAnswer.setText("");
    }

    /**
     * Nastavi boolean hodnotu pro rozeznani rezimu mezi tvorbou novr karty/editace karty existujici
     *
     * @param editing true pro editaci, false pro tvorbu karty
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     * Pri editaci nacte aktualni kartu z datoveho modelu
     */
    public void loadCurrentCard() {
        cardQuestion.setText(dataModel.getCurrentFolder().getCurrentCard().getQuestion());
        cardAnswer.setText(dataModel.getCurrentFolder().getCurrentCard().getAnswer());
    }
}
