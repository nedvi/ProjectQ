package com.nedvedd.projectq;

import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import com.nedvedd.projectq.data.TreeFolder;
import com.nedvedd.projectq.view.MiniCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeController {

    private DataModel dataModel;

    private int currentColumnIndex = 0;
    private int currentRowIndex = 1;

    private final List<MiniCard> miniCards = new ArrayList<>();

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

    @FXML
    protected void printCards() {
        System.out.println(dataModel.getCards().toString());
    }

    private void repaintFolderTreeView() {

    }

    public void updateGridPane() {
        MiniCard newMiniCard = createMiniCard(dataModel.getCurrentCard());
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
        miniCard.getStylesheets().add(String.valueOf(getClass().getResource("styles/darkStyle.css")));
        miniCard.getStyleClass().add("miniCardVBox");

        return miniCard;
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
}