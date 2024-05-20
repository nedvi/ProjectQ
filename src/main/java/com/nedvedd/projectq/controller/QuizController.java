package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.view.QuizCard;
import com.nedvedd.projectq.view.QuizCardType;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class QuizController extends AController {

    //=================================== Up-Time ===================================
    private int sec = 0;
    private int min = 0;

    @FXML
    private Label upTimeLabel;
    private Timer upTimeTimer;

    private ObservableList<QuizCard> quizCards;

    @FXML
    private GridPane gridPane;

    private int mistakes = 0;

    private void fillGridPaneWithQuizCards() {
        mistakes = 0;
        quizCards = FXCollections.observableArrayList();
        for (Card card : dataModel.getCurrentFolder().getCards()) {
            quizCards.addAll(createPairOfQuizCards(card));
        }

        Collections.shuffle(quizCards);



        gridPane.getChildren().clear();
        int col = 0;
        int row = 0;
        for (QuizCard quizCard : quizCards) {
            gridPane.add(quizCard, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
                gridPane.addRow(row);
            }

            quizCard.setOnMouseClicked(event -> {
                QuizCard[] currentPairOfQuiz = dataModel.getCurrentPairOfQuizCards();

                if (currentPairOfQuiz[0] == null && currentPairOfQuiz[1] == null) {
                    currentPairOfQuiz[0] = quizCard;
                    quizCard.setStyle("-fx-background-color: ORANGE");
                } else if (currentPairOfQuiz[1] == null) {
                    currentPairOfQuiz[1] = quizCard;
                    if (currentPairOfQuiz[0].getCard().equals(currentPairOfQuiz[1].getCard()) && !currentPairOfQuiz[0].getQuizCardType().equals(currentPairOfQuiz[1].getQuizCardType())) {
                        currentPairOfQuiz[0].setStyle("-fx-background-color: GREEN");
                        currentPairOfQuiz[1].setStyle("-fx-background-color: GREEN");

                        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
                        pauseTransition.setOnFinished(e -> {
                            quizCards.removeAll(currentPairOfQuiz);
                            gridPane.getChildren().removeAll(currentPairOfQuiz);
                            currentPairOfQuiz[0] = null;
                            currentPairOfQuiz[1] = null;
                            System.out.println("LOG: Correct combination -> Removing cards...");
                            Platform.runLater(() -> {
                                if (quizCards.isEmpty()) {
                                    upTimeTimer.cancel();
                                    showResults();
                                }
                            });
                        });
                        pauseTransition.play();
                    } else {
                        currentPairOfQuiz[0].setStyle("-fx-background-color: RED");
                        currentPairOfQuiz[1].setStyle("-fx-background-color: RED");

                        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
                        pauseTransition.setOnFinished(e -> {
                            currentPairOfQuiz[0].setStyle(null);
                            currentPairOfQuiz[1].setStyle(null);

                            currentPairOfQuiz[0] = null;
                            currentPairOfQuiz[1] = null;

                            System.out.println("LOG: Wrong combination");
                        });
                        pauseTransition.play();

                        mistakes++;
                    }
                }

            });
        }
    }

    private QuizCard[] createPairOfQuizCards(Card card) {
        QuizCard quizCardQuestion = new QuizCard(card, QuizCardType.QUESTION);
        QuizCard quizCardAnswer = new QuizCard(card, QuizCardType.ANSWER);
        return new QuizCard[]{quizCardQuestion, quizCardAnswer};
    }

    @FXML
    private void cancel() throws IOException {
        quizCards.clear();
        gridPane.getChildren().clear();
        dataModel.getCurrentPairOfQuizCards()[0] = null;
        dataModel.getCurrentPairOfQuizCards()[1] = null;
        upTimeTimer.cancel();
        Main.switchSceneTo(Main.home);
    }

    private void showResults() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Výsledky");
        alert.setHeaderText("Kvíz dokončen!");
        alert.setContentText(String.format("Čas = %s\nPočet chyb: %d", upTimeLabel.getText(), mistakes));
        alert.setOnCloseRequest(e -> {
            try {
                Main.switchSceneTo(Main.home);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        alert.showAndWait();
    }

    public void startQuiz() {
        fillGridPaneWithQuizCards();
        startUpTime();
    }

    /**
     * Spusti a formatuje Up-Time
     */
    private void startUpTime() {
        sec = 0;
        min = 0;
        upTimeTimer = new Timer();
        upTimeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        upTimeLabel.setText(String.format("%02d:%02d", min, sec));
                        if (sec<59 && min<59) {
                            sec++;
                        } else if (sec==59 && min<59) {
                            min++;
                            sec = 0;
                        }
                    }
                });
            }
        }, 0, 1000);
    }
}
