package com.nedvedd.projectq.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DataModel {
    private final ObservableList<Card> cards = FXCollections.observableArrayList();

    private final ObjectProperty<Card> currentCard = new SimpleObjectProperty<>(null);

    public Card getCurrentCard() {
        return currentCard.get();
    }

    public ObjectProperty<Card> currentCardProperty() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard.set(currentCard);
    }

    public ObservableList<Card> getCards() {
        return cards;
    }

    public void loadData(File file) throws IOException {
        //TODO: import dat z JSONu
    }

    public void saveData(File file) throws IOException {
        //TODO: export dat do JSONu
    }

    public void addCard(Card card) {
        cards.add(card);
        setCurrentCard(card);
    }

    public void removeCard(Card card) {
        Iterator<Card> cardIterator = cards.iterator();
        while (cardIterator.hasNext()) {
            Card nextCard = cardIterator.next();
            if (nextCard.equals(card)) {
                cardIterator.remove();
                break;
            }
        }
    }
}
