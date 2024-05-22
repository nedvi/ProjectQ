package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.model.Card;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller pro zobrazeni konkretni karty
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class CardViewController extends AController {

    /** Atribut karty z datoveho modelu */
    private Card card;

    /** Boolean hodnota pro kontrolu, zda je karta v GUI otocena na odpoved */
    private boolean rotated = false;

    /** Hodnota poloviny doby trvani animace otoceni karty */
    private final double ROTATION_DURATION_MS = 250;

    /** Reference na VBox karty v GUI */
    @FXML
    private VBox cardVB;

    /** Reference na Label obsahujici nadpis ("Otazka"/"Odpoved") v GUI */
    @FXML
    private Label headingLabel;

    /** Reference na Label obsahujici zneni otazky/odpovedi v GUI */
    @FXML
    private Label questionAnswerLabel;

    /** Reference na tlacitko prepnuti na predchozi kartu v GUI */
    @FXML
    private Button previousButton;

    /** Reference na tlacitko prepnuti na nasledujici kartu v GUI */
    @FXML
    private Button nextButton;

    /**
     * Po rozkliknuti konkretni karty z home-view se tato karta nastavi na aktualni
     *
     * @param card instance aktualni karty
     */
    public void setCard(Card card) {
        this.card = card;
        dataModel.getCurrentFolder().setCurrentCard(card);
        questionAnswerLabel.setText(card.getQuestion());

        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        nextButton.setDisable(indexOfCurrentCard == dataModel.getCurrentFolder().getCards().size() - 1);

        previousButton.setDisable(indexOfCurrentCard == 0);
    }

    /**
     * Akce prepnuti sceny zpet na home-view.
     * V pripade, ze karta zustala otocena na odpoved, otoci ji po prepnuti na home-view na otazku
     *
     * @throws IOException vyjimka pri nenactenem HomeLoaderu
     */
    @FXML
    private void back() throws IOException {
        Main.switchSceneTo(Main.home);
        if (rotated)
            rotateOnCardClick();
    }

    /**
     * Spusti animaci rotace karty
     *
     * @param rotateBack true pro otoceni karty zpet na otazku, jinak false
     */
    private void triggerRotationProcess(boolean rotateBack) {
        double startAngle1;
        double stopAngle1;
        double startAngle2;
        double stopAngle2;

        String headingText;
        String questionAnswerText;

        if (rotateBack) {
            startAngle1 = 180;
            stopAngle1 = 90;
            startAngle2 = 90;
            stopAngle2 = 0;
            headingText = "Otázka";
            questionAnswerText = card.getQuestion();
        } else  {
            startAngle1 = 0;
            stopAngle1 = 90;
            startAngle2 = 90;
            stopAngle2 = 180;
            headingText = "Odpověď";
            questionAnswerText = card.getAnswer();
        }

        RotateTransition rotateTransitionFirstHalf = new RotateTransition(Duration.millis(ROTATION_DURATION_MS), cardVB);
        rotateTransitionFirstHalf.setAxis(Rotate.Y_AXIS);
        rotateTransitionFirstHalf.setFromAngle(startAngle1);
        rotateTransitionFirstHalf.setToAngle(stopAngle1);

        rotateTransitionFirstHalf.setOnFinished(e -> {
            headingLabel.setRotationAxis(Rotate.Y_AXIS);
            questionAnswerLabel.setRotationAxis(Rotate.Y_AXIS);
            headingLabel.setRotate(stopAngle2);
            questionAnswerLabel.setRotate(stopAngle2);
            headingLabel.setText(headingText);
            questionAnswerLabel.setText(questionAnswerText);

            RotateTransition rotateTransitionSecondHalf = new RotateTransition(Duration.millis(ROTATION_DURATION_MS), cardVB);
            rotateTransitionSecondHalf.setAxis(Rotate.Y_AXIS);
            rotateTransitionSecondHalf.setFromAngle(startAngle2);
            rotateTransitionSecondHalf.setToAngle(stopAngle2);
            rotateTransitionSecondHalf.play();
        });
        rotateTransitionFirstHalf.play();
    }

    /**
     * Akce pro tlacitko editace karty -> prepne na card-creator-view
     *
     * @throws IOException vyjimka pri nenactenem CardCreatorLoaderu
     */
    @FXML
    private void editCurrentCard() throws IOException {
        CardCreatorController cardCreatorController = Main.cardCreator.getController();
        cardCreatorController.setEditing(true);
        cardCreatorController.loadCurrentCard();
        Main.switchSceneTo(Main.cardCreator);
        if (rotated)
            rotateOnCardClick();
    }

    /**
     * Akce pro tlacitko smazani aktualni karty
     *
     * @throws IOException vyjimka pri nenactenem HomeControlleru
     */
    @FXML
    private void deleteCurrentCard() throws IOException {
        HomeController homeController = Main.home.getController();
        homeController.removeMiniCard(card);
        dataModel.getCurrentFolder().removeCard(card);
        back();
    }

    /**
     * Akce pro tlacitko prenuti na dalsi kartu
     */
    @FXML
    private void nextCard() {
        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        Card nextCard = dataModel.getCurrentFolder().getCards().get(indexOfCurrentCard + 1);
        setCard(nextCard);
        if (rotated)
            rotateOnCardClick();
    }

    /**
     * Akce pro tlacitko prenuti na predchozi kartu
     */
    @FXML
    private void previousCard() {
        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        Card previousCard = dataModel.getCurrentFolder().getCards().get(indexOfCurrentCard - 1);
        setCard(previousCard);
        if (rotated)
            rotateOnCardClick();
    }

    /**
     * Akce vyvolani rotace pri nakliknuti karty
     */
    @FXML
    private void rotateOnCardClick() {
        System.out.println("LOG:\trotateOnCardClick\ttriggered");

        if (!rotated) {
            triggerRotationProcess(false);
            rotated = true;
        } else {
            triggerRotationProcess(true);
            rotated = false;
        }
    }
}
