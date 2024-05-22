package com.nedvedd.projectq.model;

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

/**
 * Trida reprezentujici datovou strukturu slozky karet
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class CardFolder {

    /** Seznam karet ve slozce */
    public final ObservableList<Card> cards;

    /** Aktualni karta */
    private final ObjectProperty<Card> currentCard;

    /** Nazev slozky (musi byt public, jinak failne operace s .json souborem)*/
    public final StringProperty folderName;

    /**
     * Konstruktor pro vytvoreni nove instance slozky karet
     *
     * @param folderName jmeno slozky
     */
    public CardFolder(String folderName) {
        cards = FXCollections.observableArrayList();
        currentCard = new SimpleObjectProperty<>(null);
        this.folderName = new SimpleStringProperty(folderName);
    }

    /**
     * Konstruktor pro vytvoreni instance slozky z .json souboru
     *
     * @param cards seznam karet
     * @param folderName jmeno slozky
     */
    @JsonCreator
    public CardFolder(@JsonProperty("cards") List<Card> cards, @JsonProperty("folderName") String folderName) {
        this.cards = FXCollections.observableArrayList(cards);
        currentCard = new SimpleObjectProperty<>(null);
        this.folderName = new SimpleStringProperty(folderName);
    }

    /**
     * Prida kartu do slozky
     *
     * @param card pridavana karta
     */
    public void addCard(Card card) {
        cards.add(card);
        setCurrentCard(card);
    }

    /**
     * Odstrani kartu ze slozky
     *
     * @param card karta k odstraneni
     */
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

    /**
     * @return seznam karet
     */
    public ObservableList<Card> getCards() {
        return cards;
    }

    /**
     * @return aktualni karta
     */
    public Card getCurrentCard() {
        return currentCard.get();
    }

    /**
     * @param newCurrentCard aktualni karta
     */
    public void setCurrentCard(Card newCurrentCard) {
        currentCard.set(newCurrentCard);
    }

    /**
     * @return nazev slozky
     */
    public String getFolderName() {
        return folderName.get();
    }

    /**
     * @return StringProperty s nazvem slozky
     */
    public StringProperty folderNameProperty() {
        return folderName;
    }
}
