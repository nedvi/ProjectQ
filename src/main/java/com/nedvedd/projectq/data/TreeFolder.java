package com.nedvedd.projectq.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TreeFolder {

    /** Konstanta property jmena bunky */
    private final StringProperty folderName;

    private final CardFolder cardFolder;

    /** Konstanta property typu bunky */
    public TreeFolder(String folderName, CardFolder cardFolder) {
        this.folderName = new SimpleStringProperty(folderName);
        this.cardFolder = cardFolder;
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

    public CardFolder getCardFolder() {
        return cardFolder;
    }
}
