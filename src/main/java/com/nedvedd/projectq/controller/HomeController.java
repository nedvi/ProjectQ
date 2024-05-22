package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.utillities.JsonUtillities;
import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.model.Card;
import com.nedvedd.projectq.model.CardFolder;
import com.nedvedd.projectq.model.TreeFolder;
import com.nedvedd.projectq.view.Config;
import com.nedvedd.projectq.view.MiniCard;
import com.nedvedd.projectq.view.TreeFolderFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Iterator;

/**
 * Controller pro zobrazeni domovske stranky s kartami
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class HomeController extends AController {

    /** Atribut aktualni slozky ve strome slozek */
    private TreeItem<TreeFolder> currentTreeFolder;

    /** Seznam minikaret zobrazovanych na domovske strence */
    private final ObservableList<MiniCard> miniCards = FXCollections.observableArrayList();

    /** Reference na strom slozek v GUI */
    @FXML
    private TreeView<TreeFolder> folderTreeView;

    /** Reference na GridPane s minikartami v GUI */
    @FXML
    private GridPane gridPane = new GridPane();

    /**
     * Akce pro tlacitko prepnuti na tvorbu nove karty
     *
     * @throws IOException vyjimka pri nenactenem CardCreatorLoaderu
     */
    @FXML
    protected void switchSceneToCardCreation() throws IOException {
        Main.switchSceneTo(Main.cardCreator);
    }

    /**
     * Akce pro tlactitko zahajeni kvizu
     *
     * @throws IOException vyjimka pri nenactenem QuizLoaderu
     */
    @FXML
    protected void startQuiz() throws IOException {
        QuizController quizController = Main.quiz.getController();
        quizController.startQuiz();
        Main.switchSceneTo(Main.quiz);
    }

    /**
     * Vygeneruje strom slozek v TreeView v GUI
     */
    public void generateFolderTreeView() {
        MenuItem removeCurrentFolderItem = new MenuItem("Remove current folder");

        ContextMenu contextMenu = new ContextMenu();
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

        TreeItem<TreeFolder> invisibleRoot = new TreeItem<>(new TreeFolder("Invisible Root", null));
        folderTreeView.setRoot(invisibleRoot);
        folderTreeView.setShowRoot(false);
        folderTreeView.getRoot().setExpanded(true);
        folderTreeView.setEditable(true);
        folderTreeView.setCellFactory(treeView -> new TreeFolderFactory());
    }

    /**
     * Prida novou slozku do stromu
     *
     * @param folderName jmeno slozky
     */
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

    /**
     * Akce pro tlacitko pridani nove slozky.
     * Prida slozku novou slozku s explicitne nastavenym nazvem "Nova slozka" (da se nasledne editovat diky custom tride TreeFolderFactory)
     */
    @FXML
    private void addTreeFolder() {
        addTreeFolder("Nová složka");
    }

    /**
     * Vymaze ze stromu slozku z parametru
     *
     * @param treeFolder slozka k vymazani
     */
    private void removeTreeFolder(TreeItem<TreeFolder> treeFolder) {
        System.out.println("LOG: " + treeFolder.getValue().getFolderName() + " removed");
        dataModel.removeFolder(treeFolder.getValue().getCardFolder());
        folderTreeView.getRoot().getChildren().remove(treeFolder);
        repaintGridPane();
    }

    /**
     * Znovu vykresli GridPane s kartami
     */
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

    /**
     * Vytvori novou minikartu
     *
     * @param card instance karty
     * @return nova instance minikarty
     */
    private MiniCard createMiniCard(Card card) {
        MiniCard miniCard = new MiniCard(card);
        miniCard.getStylesheets().add(Config.ACTIVE_STYLE_SHEET.get());
        miniCard.getStyleClass().add("miniCardVBox");

        return miniCard;
    }


    /**
     * Aktualizuje seznam s minikartami v konkretni slozce
     *
     * @param cardFolder instance slozky s kartami
     */
    public void updateMiniCards(CardFolder cardFolder) {
        miniCards.clear();
        for (int i = 0; i < cardFolder.getCards().size(); i++) {
            miniCards.add(createMiniCard(cardFolder.getCards().get(i)));
            repaintGridPane();
        }
    }

    /**
     * Metoda pro dokonceni vytvoreni nove karty -> posledni vytvorenou kartu prida na GridPane
     */
    public void addCurrentCardToGridPane() {
        MiniCard newMiniCard = createMiniCard(dataModel.getCurrentFolder().getCurrentCard());
        miniCards.add(newMiniCard);
        repaintGridPane();
    }

    /**
     * Odstrani minikartu
     *
     * @param card instance karty
     */
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

    /**
     * Akce pro tlacitko ulozeni dat.
     * Vyvola ulozeni slozek i karet do JSON souboru
     *
     * @throws IOException vyjimka pri chybe zapisu do souboru
     */
    @FXML
    private void saveData() throws IOException {
        JsonUtillities.saveJsonData(dataModel.getFolders());
    }
}