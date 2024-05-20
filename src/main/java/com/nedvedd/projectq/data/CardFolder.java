package com.nedvedd.projectq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.List;

public class CardFolder {

    public final ObservableList<Card> cards;

    private final ObjectProperty<Card> currentCard;

    private StringProperty folderName;

    public CardFolder(String folderName) {
        cards = FXCollections.observableArrayList();
        currentCard = new SimpleObjectProperty<>(null);
        this.folderName = new SimpleStringProperty(folderName);
    }

    @JsonCreator
    public CardFolder(@JsonProperty("cards") List<Card> cards, @JsonProperty("folderName") String folderName) {
        this.cards = FXCollections.observableArrayList(cards);
        currentCard = new SimpleObjectProperty<>(null);
        this.folderName = new SimpleStringProperty(folderName);
    }


    public ObservableList<Card> getCards() {
        return cards;
    }

    public Card getCurrentCard() {
        return currentCard.get();
    }

    public void setCurrentCard(Card newCurrentCard) {
        currentCard.set(newCurrentCard);
    }

    public String getFolderName() {
        return folderName.get();
    }

    public StringProperty folderNameProperty() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName.set(folderName);
    }

    public ObjectProperty<Card> currentCardProperty() {
        return currentCard;
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
