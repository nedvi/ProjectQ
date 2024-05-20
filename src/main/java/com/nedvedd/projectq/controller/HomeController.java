package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.JSONUtillities;
import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.CardFolder;
import com.nedvedd.projectq.data.DataModel;
import com.nedvedd.projectq.data.TreeFolder;
import com.nedvedd.projectq.view.MiniCard;
import com.nedvedd.projectq.view.TreeFolderFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class HomeController {

    private DataModel dataModel;
    private TreeItem<TreeFolder> currentTreeFolder;
    private int currentColumnIndex = 0;
    private int currentRowIndex = 1;

    private final ObservableList<MiniCard> miniCards = FXCollections.observableArrayList();

    @FXML
    private Label welcomeText;

    @FXML
    private TreeView<TreeFolder> folderTreeView;

    @FXML
    private GridPane gridPane = new GridPane();

    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel;
    }

    @FXML
    protected void switchSceneToCardCreation() throws IOException {
        Main.switchSceneTo(Main.cardCreation);
    }

    public void generateFolderTreeView() {
        folderTreeView.setCellFactory(treeView -> new TreeFolderFactory());

        ContextMenu contextMenu = new ContextMenu();

        // create menuitems
        MenuItem removeCurrentFolderItem = new MenuItem("Remove current folder");


        // add menu items to menu
        contextMenu.getItems().add(removeCurrentFolderItem);

        folderTreeView.setContextMenu(contextMenu);

        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                CardFolder newCardFolder = newValue.getValue().getCardFolder();
                dataModel.setCurrentFolder(newCardFolder);
                currentTreeFolder = newValue;
                removeCurrentFolderItem.setOnAction(e -> removeTreeFolder(currentTreeFolder));
                System.out.println("LOG:\tFolder changed to " + newValue.getValue().getFolderName());
                if (newCardFolder.getCards().isEmpty()) {
                    System.out.println("LOG:\tFolder empty");
                    miniCards.clear();
                    gridPane.getChildren().clear();
                } else {
                    updateMiniCards(newCardFolder);
                }

            }
        });

        folderTreeView.setEditable(true);

        TreeItem<TreeFolder> invisibleRoot = new TreeItem<TreeFolder>(new TreeFolder("Invisible Root", null));
        folderTreeView.setRoot(invisibleRoot);
        folderTreeView.setShowRoot(false);
        folderTreeView.getRoot().setExpanded(true);
    }

    public void addTreeFolder(String folderName) {
        CardFolder newCardFolder = new CardFolder(folderName);
        dataModel.addFolder(newCardFolder);
        TreeItem<TreeFolder> newFolderTI = new TreeItem<>(new TreeFolder(folderName, newCardFolder));
        newFolderTI.getValue().folderNameProperty().bindBidirectional(newCardFolder.folderNameProperty());
        folderTreeView.getRoot().getChildren().add(newFolderTI);
    }

    /**
     * Prida existujici slozku do stromu
     *
     * @param folderName jmeno slozky
     * @param cardFolder instance slozky z datoveho modelu
     */
    public void addTreeFolder(String folderName, CardFolder cardFolder) {
        TreeItem<TreeFolder> newFolderTI = new TreeItem<>(new TreeFolder(folderName, cardFolder));
        newFolderTI.getValue().folderNameProperty().bindBidirectional(cardFolder.folderNameProperty());
        folderTreeView.getRoot().getChildren().add(newFolderTI);
    }

    @FXML
    private void addTreeFolder() {
        addTreeFolder("Nová složka");
    }

    private void removeTreeFolder(TreeItem<TreeFolder> treeFolder) {
        System.out.println("LOG: " + treeFolder.getValue().getFolderName() + " removed");
        dataModel.removeFolder(treeFolder.getValue().getCardFolder());
        folderTreeView.getRoot().getChildren().remove(treeFolder);
        repaintGridPane();
    }

    public void addCurrentCardToGridPane() {
        MiniCard newMiniCard = createMiniCard(dataModel.getCurrentFolder().getCurrentCard());
        miniCards.add(newMiniCard);
        repaintGridPane();
    }

    public void repaintGridPane() {
        gridPane.getChildren().clear();
        int col = 0;
        int row = 0;
        for (MiniCard miniCard : miniCards) {
            gridPane.add(miniCard, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
                gridPane.addRow(row);
            }
        }
    }

    private MiniCard createMiniCard(Card card) {
        MiniCard miniCard = new MiniCard(card);
        miniCard.getStylesheets().add(String.valueOf(Main.class.getResource("styles/darkStyle.css")));
        miniCard.getStyleClass().add("miniCardVBox");

        return miniCard;
    }

    public void updateMiniCards(CardFolder cardFolder) {
        miniCards.clear();
        for (int i = 0; i < cardFolder.getCards().size(); i++) {
            miniCards.add(createMiniCard(cardFolder.getCards().get(i)));
            repaintGridPane();
        }
    }

    public void removeMiniCard(Card card) {
        Iterator<MiniCard> miniCardIterator = miniCards.iterator();
        while (miniCardIterator.hasNext()) {
            MiniCard miniCard = miniCardIterator.next();
            if (miniCard.getCard().equals(card)) {
                miniCardIterator.remove();
                System.out.println("LOG:\tMiniCard removed");
                break;
            }
        }
        repaintGridPane();
    }

    public List<MiniCard> getMiniCards() {
        return miniCards;
    }

    @FXML
    private void saveData() throws IOException {
        JSONUtillities.saveJsonData(dataModel.getFolders());
    }
}