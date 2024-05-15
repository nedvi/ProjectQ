package com.nedvedd.projectq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;

public class Card {

    private SimpleStringProperty question;
    private SimpleStringProperty answer;

    @JsonCreator
    public Card(@JsonProperty("question")String question, @JsonProperty("answer")String answer) {
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
    }

    public String getQuestion() {
        return question.get();
    }

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    @Override
    public String toString() {
        return "Card{" +
                ", question=" + question +
                ", answer=" + answer +
                '}';
    }
}
