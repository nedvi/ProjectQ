package com.nedvedd.projectq.view;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.model.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Trida pro vytvareni kvizovych umistovanych na GridPane u kvizoveho rezimu
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class QuizCard extends BorderPane {

    /** Karta (datova struktura) */
    private final Card card;

    /** Typ kvizove karty (otazka/odpoved) */
    private QuizCardType quizCardType;

    /**
     * Konstruktor pro vytvoreni nove instance kvizove karty
     *
     * @param card karta (datova struktura)
     * @param quizCardType typ kvizove karty (otazka/odpoved)
     */
    public QuizCard(Card card, QuizCardType quizCardType) {
        this.card = card;
        this.quizCardType = quizCardType;
        this.setCenter(createQuizCard());
        this.getStylesheets().add(String.valueOf(Main.class.getResource("styles/darkStyle.css")));
        this.getStyleClass().add("miniCardVBox");
    }

    /**
     * Vytvori novou kvizovou kartu
     *
     * @return nova kvizova karta
     */
    private Node createQuizCard() {
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

    /**
     * @return karta (datova struktura)
     */
    public Card getCard() {
        return card;
    }

    /**
     * @return typ kvizove karty (otazka/odpoved)
     */
    public QuizCardType getQuizCardType() {
        return quizCardType;
    }
}
