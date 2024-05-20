package com.nedvedd.projectq.view;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class QuizCard extends BorderPane {
    private final Card card;
    private QuizCardType quizCardType;

    public QuizCard(Card card, QuizCardType quizCardType) {
        this.card = card;
        this.quizCardType = quizCardType;
        this.setCenter(getQuizCard());
        this.getStylesheets().add(String.valueOf(Main.class.getResource("styles/darkStyle.css")));
        this.getStyleClass().add("miniCardVBox");
    }

    private Node getQuizCard() {
        VBox miniCardVBox = new VBox();

        Label label = new Label();

        if (this.quizCardType.equals(QuizCardType.QUESTION)) {
            label.setText(card.getQuestion());
        } else {
            label.setText(card.getAnswer());
        }

        label.setStyle(
                "-fx-wrap-text: true;" +
                        "-fx-text-fill: WHITE; -fx-text-alignment: center");
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);

        miniCardVBox.getChildren().addAll(label);
        miniCardVBox.setAlignment(Pos.CENTER);
        miniCardVBox.setPadding(new Insets(5));
        miniCardVBox.setSpacing(5);
        miniCardVBox.setMinHeight(300);
        miniCardVBox.setMaxHeight(300);

        return miniCardVBox;
    }

    public Card getCard() {
        return card;
    }

    public QuizCardType getQuizCardType() {
        return quizCardType;
    }
}
