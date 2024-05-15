package com.nedvedd.projectq.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class TreeFolder {

    /** Konstanta property jmena bunky */
    private final StringProperty folderName;

    private List<Card> cards = new ArrayList<>();

    /** Konstanta property typu bunky */
    public TreeFolder(String folderName) {
        this.folderName = new SimpleStringProperty(folderName);
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


}
