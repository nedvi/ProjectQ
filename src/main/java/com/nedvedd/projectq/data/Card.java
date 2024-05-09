package com.nedvedd.projectq.data;

import javafx.beans.property.SimpleStringProperty;

public class Card {

    private SimpleStringProperty name;

    private SimpleStringProperty question;
    private SimpleStringProperty answer;

    public Card(String name, String question, String answer) {
        this.name = new SimpleStringProperty(name);
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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
                "name=" + name +
                ", question=" + question +
                ", answer=" + answer +
                '}';
    }
}
