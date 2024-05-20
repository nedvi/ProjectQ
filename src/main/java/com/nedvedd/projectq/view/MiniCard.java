package com.nedvedd.projectq.view;

import com.nedvedd.projectq.controller.CardViewController;
import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;

public class MiniCard extends BorderPane {

    private final Card card;

    public MiniCard(Card card) {
        this.card = card;
        this.setCenter(getMiniCard());
    }

    private Node getMiniCard() {
        VBox miniCardVBox = new VBox();

        Label questionLabel = new Label(card.getQuestion());
        Label answerLabel = new Label(card.getAnswer());

        questionLabel.setStyle(
                "-fx-wrap-text: true;" +
                        "-fx-text-fill: WHITE; -fx-text-alignment: center");
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setMaxWidth(Double.MAX_VALUE);

        answerLabel.setStyle(
                "-fx-wrap-text: true;" +
                        "-fx-text-fill: WHITE; -fx-text-alignment: center");
        answerLabel.setAlignment(Pos.CENTER);
        answerLabel.setMaxWidth(Double.MAX_VALUE);

        Line line = new Line(-75, 0, 75, 0);

        miniCardVBox.getChildren().addAll(questionLabel, line, answerLabel);
        miniCardVBox.setAlignment(Pos.CENTER);
        miniCardVBox.setPadding(new Insets(5));
        miniCardVBox.setSpacing(5);
        miniCardVBox.setMinHeight(300);
        miniCardVBox.setMaxHeight(300);

        miniCardVBox.setOnMouseClicked(event -> {
            try {
                CardViewController cardViewController = Main.card.getController();
                cardViewController.setCard(card);
                Main.switchSceneTo(Main.card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        return miniCardVBox;
    }

    public Card getCard() {
        return card;
    }
}
