package com.nedvedd.projectq;

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

    public static Scene scene;

    @Override
    public void init() throws Exception {
        initDataModel();
    }

    @Override
    public void start(Stage stage) {

        scene = new Scene(home.getRoot(), 480, 720);

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
        DataModel dataModel = new DataModel();

        home = new FXMLLoader(getClass().getResource("home-view.fxml"));
        home.load();

        cardCreation = new FXMLLoader(getClass().getResource("create-card.fxml"));
        cardCreation.load();

        HomeController homeController = home.getController();
        CardCreationController cardCreationController = cardCreation.getController();

        homeController.initModel(dataModel);
        cardCreationController.initModel(dataModel);
    }
}