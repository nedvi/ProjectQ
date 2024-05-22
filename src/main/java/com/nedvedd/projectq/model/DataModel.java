package com.nedvedd.projectq.model;

import com.nedvedd.projectq.view.QuizCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;

/**
 * Primarni trida modelu, ktera zajistuje pristup k datovym strukturam
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class DataModel {

    /** Seznam slozek */
    private final ObservableList<CardFolder> folders = FXCollections.observableArrayList();

    /** Pole o velikosti 2, slouzici pro ukladani aktualne vybranych karet (max 2) v kvizovem rezimu */
    private final QuizCard[] currentPairOfQuizCards = new QuizCard[2];

    /** Reference na aktualne vybranou slozku */
    private CardFolder currentFolder;

    /**
     * @return vrati pole s aktualnim vyberem karet v kvizovem rezimu
     */
    public QuizCard[] getCurrentPairOfQuizCards() {
        return currentPairOfQuizCards;
    }

    /**
     * @return seznam slozek
     */
    public ObservableList<CardFolder> getFolders() {
        return folders;
    }

    /**
     * Prida novou slozku z parametru
     *
     * @param cardFolder slozka k pridani
     */
    public void addFolder(CardFolder cardFolder) {
        folders.add(cardFolder);
    }

    /**
     * Odstrani slozku z parametru
     *
     * @param cardFolder slozka k odstraneni
     */
    public void removeFolder(CardFolder cardFolder) {
        Iterator<CardFolder> cardFolderIterator = folders.iterator();
        while (cardFolderIterator.hasNext()) {
            CardFolder nextFolder = cardFolderIterator.next();
            if (nextFolder.equals(cardFolder)) {
                cardFolderIterator.remove();
                break;
            }
        }
    }

    /**
     * @return aktualne vybrana slozka
     */
    public CardFolder getCurrentFolder() {
        return currentFolder;
    }

    /**
     * @param currentFolder aktualne vybrana slozka
     */
    public void setCurrentFolder(CardFolder currentFolder) {
        this.currentFolder = currentFolder;
    }
}
