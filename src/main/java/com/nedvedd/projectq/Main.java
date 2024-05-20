package com.nedvedd.projectq;

import com.nedvedd.projectq.controller.CardCreationController;
import com.nedvedd.projectq.controller.CardViewController;
import com.nedvedd.projectq.controller.HomeController;
import com.nedvedd.projectq.controller.QuizController;
import com.nedvedd.projectq.data.CardFolder;
import com.nedvedd.projectq.data.DataModel;
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

public class Main extends Application {

    private static Stage primaryStage;
    public static FXMLLoader home;
    public static FXMLLoader cardCreation;
    public static FXMLLoader card;
    public static FXMLLoader quiz;

    public static Scene scene;

    public DataModel dataModel;

    @Override
    public void init() throws Exception {
        initDataModel();
    }

    @Override
    public void start(Stage stage) {

        scene = new Scene(home.getRoot(), 1080, 720);

        primaryStage = stage;
        primaryStage.setTitle("ProjectQ");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> exitBTNAction(event));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void switchSceneTo(FXMLLoader fxmlLoader) throws IOException {
        scene.setRoot(fxmlLoader.getRoot());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initDataModel() throws IOException {
        dataModel = new DataModel();

        home = new FXMLLoader(getClass().getResource("home-view.fxml"));
        home.load();

        cardCreation = new FXMLLoader(getClass().getResource("create-card.fxml"));
        cardCreation.load();

        card = new FXMLLoader(getClass().getResource("card-view.fxml"));
        card.load();

        quiz = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
        quiz.load();

        HomeController homeController = home.getController();
        CardCreationController cardCreationController = cardCreation.getController();
        CardViewController cardViewController = card.getController();
        QuizController quizController = quiz.getController();

        homeController.initModel(dataModel);
        cardCreationController.initModel(dataModel);
        cardViewController.initModel(dataModel);
        quizController.initModel(dataModel);

        createSampleCards();
    }

    public void createSampleCards() throws IOException {
        HomeController homeController = home.getController();
        homeController.generateFolderTreeView();
        //TODO: osetrit defaultni generaci dat v pripade, ze zadna ulozena data nemame
        //if
        dataModel.getFolders().clear();
        ObservableList<CardFolder> cardFolders = JSONUtillities.loadJsonData();
        for (CardFolder cardFolder : cardFolders) {
            dataModel.addFolder(cardFolder);
            homeController.addTreeFolder(cardFolder.getFolderName(), cardFolder);
        }

        dataModel.setCurrentFolder(dataModel.getFolders().get(0));
        //else

//        for (int i = 0; i < 20; i++) {
//            dataModel.getCurrentFolder().addCard(new Card("Otázka " + i, "Odpověď " + i));
//        }
//
//        for (int i = 0; i < 5; i++) {
//            dataModel.getFolders().get(1).addCard(new Card("Otázka " + i, "Odpověď " + i));
//        }

        homeController.updateMiniCards(dataModel.getCurrentFolder());
        System.out.println("LOG:\tNumber of folders: " + dataModel.getFolders().size());
//        JSONUtillities.saveJsonData(dataModel.getFolders());
    }

    public void loadData() {

    }

    /**
     * Akce pro tlacitko ukonceni programu (X).
     * Vytvori potvrzovaci dialog - kdyz ano, program se ukonci, kdyz ne ukonceni se prerusi a program pokracuje v behu.
     *
     * @param event ukonceni programu
     */
    private void exitBTNAction(WindowEvent event) {
        String exitStr = "Are you sure you want to exit the program?";

        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit");
        exitAlert.setHeaderText("Exit");
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