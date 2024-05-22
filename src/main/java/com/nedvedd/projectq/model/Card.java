package com.nedvedd.projectq.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Trida reprezentujici datovou strukturu karty
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class Card {

    /** StringProperty pro zneni otazky */
    private final StringProperty question;

    /** StringProperty pro zneni odpovedi */
    private final StringProperty answer;

    /**
     * Konstruktor pro vytvoreni nove instance karty.
     * Anotace @JsonCreator a @JsonPrperty slouzi pro praci s knihovnou Jackson,
     * ktera s pomoci custom tridy JsonUtilities nacita a uklada data uzivatele do .json souboru
     *
     * @param question zneni otazky
     * @param answer zneni odpovedi
     */
    @JsonCreator
    public Card(@JsonProperty("question")String question, @JsonProperty("answer")String answer) {
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
    }

    /**
     * @return zneni otazky
     */
    public String getQuestion() {
        return question.get();
    }

    /**
     * @param question zneni otazky
     */
    public void setQuestion(String question) {
        this.question.set(question);
    }

    /**
     * @return zneni odpovedi
     */
    public String getAnswer() {
        return answer.get();
    }

    /**
     * @param answer zneni odpovedi
     */
    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    /**
     * @return informace o karte
     */
    @Override
    public String toString() {
        return "Card { " +
                "question: " + question.get() +
                ", answer: " + answer.get() +
                " }";
    }
}
