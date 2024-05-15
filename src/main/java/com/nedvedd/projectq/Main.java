package com.nedvedd.projectq;

import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    public static FXMLLoader home;
    public static FXMLLoader cardCreation;
    public static FXMLLoader card;

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

        HomeController homeController = home.getController();
        CardCreationController cardCreationController = cardCreation.getController();
        CardViewController cardViewController = card.getController();

        homeController.initModel(dataModel);
        cardCreationController.initModel(dataModel);
        cardViewController.initModel(dataModel);

        createSampleCards();

        JSONUtillities.cretaeDefaultCards();
    }

    public void createSampleCards() {
        HomeController controller = home.getController();

        for (int i = 0; i < 20; i++) {
            dataModel.addCard(new Card("Otázka " + i, "Odpověď " + i));
            controller.updateGridPane();
        }

//        Card sampleCard01 = new Card("Karta 1", "Otazka 1", "Odpoved 1");
//        dataModel.addCard(sampleCard01);
//        controller.updateGridPane();
//
//        Card sampleCard02 = new Card("Karta 2", "Otazka 2", "Odpoved 2");
//        dataModel.addCard(sampleCard02);
//        controller.updateGridPane();
    }
}