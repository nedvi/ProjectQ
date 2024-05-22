package com.nedvedd.projectq;

import com.nedvedd.projectq.controller.CardCreatorController;
import com.nedvedd.projectq.controller.CardViewController;
import com.nedvedd.projectq.controller.HomeController;
import com.nedvedd.projectq.controller.QuizController;
import com.nedvedd.projectq.model.Card;
import com.nedvedd.projectq.model.CardFolder;
import com.nedvedd.projectq.model.DataModel;
import com.nedvedd.projectq.utillities.JsonUtilities;
import com.nedvedd.projectq.view.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

/**
 * Hlavni trida programu ProjectQ-> Ucici aplikace s moznosti vytvareni a prochazeni karticek a kvizovym modem
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class Main extends Application {

    /** Primary Stage */
    private static Stage primaryStage;

    /** FXMLLoader domovske stranky */
    public static FXMLLoader home;

    /** FXMLLoader tvorby karty */
    public static FXMLLoader cardCreator;

    /** FXMLLoader zobrazeni karty */
    public static FXMLLoader card;

    /** FXMLLoader kvizovoveho rezimu */
    public static FXMLLoader quiz;

    /** Scena */
    public static Scene scene;

    /** Datovy model */
    public DataModel dataModel;

    /**
     * Vyvola inicializaci dat
     *
     * @throws Exception vyjimka pri chybe inicializace
     */
    @Override
    public void init() throws Exception {
        dataModel = new DataModel();

        home = new FXMLLoader(getClass().getResource("home-view.fxml"));
        home.load();

        cardCreator = new FXMLLoader(getClass().getResource("card-creator-view.fxml"));
        cardCreator.load();

        card = new FXMLLoader(getClass().getResource("card-view.fxml"));
        card.load();

        quiz = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
        quiz.load();

        initDataModel();
    }

    /**
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {

        scene = new Scene(home.getRoot(), 1080, 720);

        primaryStage = stage;
        primaryStage.setTitle("ProjectQ");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> exitBTNAction(event));
        primaryStage.show();
    }

    /**
     * Spousteci metoda
     *
     * @param args parametry prikazove radky
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Prepne na pozadovanou scenu
     *
     * @param fxmlLoader instance fxml loaderu
     * @throws IOException vyjimka pri nenactenem fxml loaderu
     */
    public static void switchSceneTo(FXMLLoader fxmlLoader) throws IOException {
        scene.setRoot(fxmlLoader.getRoot());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Inicializuje jedinou spolecnou instanci datoveho modelu do vsech controlleru pro jednuduchou manipulaci s daty napric vsemi controllery
     */
    public void initDataModel() {
        HomeController homeController = home.getController();
        CardCreatorController cardCreatorController = cardCreator.getController();
        CardViewController cardViewController = card.getController();
        QuizController quizController = quiz.getController();

        homeController.initModel(dataModel);
        cardCreatorController.initModel(dataModel);
        cardViewController.initModel(dataModel);
        quizController.initModel(dataModel);

        getContent();
    }

    /**
     * Vytvori obsah domovske stranky.
     * Pokud mame ulozena data v .json souboru, nacte je. Jinak vygeneruje 2 slozky s nekolika prikladovymi kartami
     */
    public void getContent() {
        HomeController homeController = home.getController();
        homeController.generateFolderTreeView();

        ObservableList<CardFolder> cardFolders = JsonUtilities.loadJsonData();
        dataModel.getFolders().clear();

        if (cardFolders != null) {
            for (CardFolder cardFolder : cardFolders) {
                dataModel.addFolder(cardFolder);
                homeController.addTreeFolder(cardFolder.getFolderName(), cardFolder);
            }
        } else {
            CardFolder cardFolder1 = new CardFolder("Složka 1");
            dataModel.addFolder(cardFolder1);
            for (int i = 0; i < 5; i++) {
                dataModel.getFolders().get(0).addCard(new Card("Otázka " + i, "Odpověď " + i));
            }
            homeController.addTreeFolder(cardFolder1.getFolderName(), cardFolder1);

            CardFolder cardFolder2 = new CardFolder("Složka 2");
            dataModel.addFolder(cardFolder2);
            for (int i = 0; i < 3; i++) {
                dataModel.getFolders().get(1).addCard(new Card("Otázka " + i, "Odpověď " + i));
            }
            homeController.addTreeFolder(cardFolder2.getFolderName(), cardFolder2);
        }

        dataModel.setCurrentFolder(dataModel.getFolders().get(0));

        homeController.updateMiniCards(dataModel.getCurrentFolder());
        System.out.println("LOG:\tNumber of folders: " + dataModel.getFolders().size());
    }

    /**
     * Akce pro tlacitko ukonceni programu (X).
     * Vytvori potvrzovaci dialog - kdyz ano, program se ukonci, kdyz ne ukonceni se prerusi a program pokracuje v behu.
     *
     * @param event ukonceni programu
     */
    private void exitBTNAction(WindowEvent event) {
        String exitStr = "Opravdu chcete ukončit program?";

        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit");
        exitAlert.setHeaderText("Ukončit program");
        exitAlert.setContentText(exitStr);

        DialogPane alertDP = exitAlert.getDialogPane();
        alertDP.getStylesheets().add(Config.ACTIVE_STYLE_SHEET.get());
        alertDP.getStyleClass().add("alertDP");

        Optional<ButtonType> result = exitAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
            System.exit(0);
        } else {
            event.consume();	// prerusi ukonceni programu
            exitAlert.close();
        }
    }
}