package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;

public class CardViewController {

    private DataModel dataModel;
    private Card card;

    private boolean rotated = false;

    private final double ROTATION_DURATION_MS = 250;

    @FXML
    private VBox cardVB;
    @FXML
    private TextField nameTF;

    @FXML
    private Label headingLabel;

    @FXML
    private Label questionAnswerLabel;

    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    public CardViewController() {
    }

    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel ;
    }

    public void setCard(Card card) {
        this.card = card;
        questionAnswerLabel.setText(card.getQuestion());

        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        if (indexOfCurrentCard == dataModel.getCurrentFolder().getCards().size() - 1) {
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
        }

        if (indexOfCurrentCard == 0) {
            previousButton.setDisable(true);
        } else {
            previousButton.setDisable(false);
        }
    }

    public void back() throws IOException {
        Main.switchSceneTo(Main.home);
        if (rotated)
            rotateOnCardClick();
    }

    @FXML
    private void deleteCurrentCard() throws IOException {
        HomeController homeController = Main.home.getController();
        homeController.removeMiniCard(card);
        dataModel.getCurrentFolder().removeCard(card);
        back();
    }

    @FXML
    private void nextCard() {
        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        Card nextCard = dataModel.getCurrentFolder().getCards().get(indexOfCurrentCard + 1);
        setCard(nextCard);
        if (rotated)
            rotateOnCardClick();

    }

    @FXML
    private void previousCard() {
        int indexOfCurrentCard = dataModel.getCurrentFolder().getCards().indexOf(card);
        Card previousCard = dataModel.getCurrentFolder().getCards().get(indexOfCurrentCard - 1);
        setCard(previousCard);
        if (rotated)
            rotateOnCardClick();
    }

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
}
