package com.nedvedd.projectq.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Pomocna trida reprezentujici slozku karet v TreeView.
 * Implementovano kvuli spravnemu zobrazovani nazvu slozek v TreeView.
 * Pouziva se jako typovy parametr pri vytvareni instanci TreeItemu -> TreeItem<TreeFolder>.
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class TreeFolder {

    /** Jmeno slozky (stromove bunky) */
    private final StringProperty folderName;

    /** Slozka karet (datova struktura) */
    private final CardFolder cardFolder;

    /**
     * Konstruktor pro vytvoreni nove instance slozky v TreeView
     *
     * @param folderName jmeno slozky
     * @param cardFolder reference na slozku (datovou strukturu)
     */
    public TreeFolder(String folderName, CardFolder cardFolder) {
        this.folderName = new SimpleStringProperty(folderName);
        this.cardFolder = cardFolder;
    }

    /**
     * @return jmeno slozky
     */
    public String getFolderName() {
        return folderName.get();
    }

    /**
     * @return property jmena slozky
     */
    public StringProperty folderNameProperty() {
        return folderName;
    }

    /**
     * @param folderName jmeno slozky
     */
    public void setFolderName(String folderName) {
        this.folderName.set(folderName);
    }

    /**
     * @return reference na slozku (datovou strukturu)
     */
    public CardFolder getCardFolder() {
        return cardFolder;
    }
}
