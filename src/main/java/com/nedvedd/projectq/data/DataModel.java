package com.nedvedd.projectq.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DataModel {
    private final ObservableList<CardFolder> folders = FXCollections.observableArrayList();

    private CardFolder currentFolder;

    public ObservableList<CardFolder> getFolders() {
        return folders;
    }


    public void addFolder(CardFolder cardFolder) {
        folders.add(cardFolder);
    }

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

    public CardFolder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(CardFolder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public void loadData(File file) throws IOException {
        //TODO: import dat z JSONu
    }

    public void saveData(File file) throws IOException {
        //TODO: export dat do JSONu
    }
}
