package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.Main;
import com.nedvedd.projectq.model.Card;
import com.nedvedd.projectq.view.Config;
import com.nedvedd.projectq.view.QuizCard;
import com.nedvedd.projectq.view.QuizCardType;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller pro kvizovy rezim
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class QuizController extends AController {

    /** Seznam kvizovych karet */
    private ObservableList<QuizCard> quizCards;

    /** Atribut pocet chyb */
    private int mistakes = 0;

    /** Pocet sekund upTimu kvizu */
    private int sec = 0;

    /** Pocet minut upTimu kvizu */
    private int min = 0;

    /** Timer pro pocitani uplynuleho casu */
    private Timer upTimeTimer;

    /** Refernce na Label s aktualne uplynutym casem*/
    @FXML
    private Label upTimeLabel;

    /** Reference na GridPane s kviz. kartami v GUI */
    @FXML
    private GridPane gridPane;

    /**
     * Ze slozky, ve ktere se uzivatel aktualne nachazi (currentFolder z dataModelu) se pripravi kviz.
     * Kvizove karty se po nacteni zamichaji.
     * Dale je naimplementovana logika pro zvyrazneni karet (aktualni vyber prvni karty + zcervenani dvou vybranych karet v pripade spatne kombinace,
     * a zezelenani paru karet a jejich nasledne zmizeni po jedne sekunde)
     */
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
                QuizCard[] currentPairOfQuizCards = dataModel.getCurrentPairOfQuizCards();

                if (currentPairOfQuizCards[0] == null && currentPairOfQuizCards[1] == null) {
                    currentPairOfQuizCards[0] = quizCard;
                    quizCard.setStyle("-fx-background-color: ORANGE");
                } else if (currentPairOfQuizCards[1] == null) {
                    currentPairOfQuizCards[1] = quizCard;
                    if (currentPairOfQuizCards[0].getCard().equals(currentPairOfQuizCards[1].getCard()) && !currentPairOfQuizCards[0].getQuizCardType().equals(currentPairOfQuizCards[1].getQuizCardType())) {
                        currentPairOfQuizCards[0].setStyle("-fx-background-color: GREEN");
                        currentPairOfQuizCards[1].setStyle("-fx-background-color: GREEN");

                        PauseTransition pauseTransition = getCorrectPauseTransition(currentPairOfQuizCards);
                        pauseTransition.play();
                    } else {
                        currentPairOfQuizCards[0].setStyle("-fx-background-color: RED");
                        currentPairOfQuizCards[1].setStyle("-fx-background-color: RED");

                        PauseTransition pauseTransition = getWrongPauseTransition(currentPairOfQuizCards);
                        pauseTransition.play();

                        mistakes++;
                    }
                }

            });
        }
    }

    /**
     * Vytvori instanci PauseTransition, ktera vyvola pozastaveni na 1 sekundu (po tuto dobu bude par karet zvyrazen zelene)
     * a nasledne odstrani karty z GridPane.
     *
     * @param currentPairOfQuizCards aktualni par vybranych kvizovych karet
     * @return 1-sekundova PauseTransition
     */
    private PauseTransition getCorrectPauseTransition(QuizCard[] currentPairOfQuizCards) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(e -> {
            quizCards.removeAll(currentPairOfQuizCards);
            gridPane.getChildren().removeAll(currentPairOfQuizCards);
            currentPairOfQuizCards[0] = null;
            currentPairOfQuizCards[1] = null;
            System.out.println("LOG:\tCorrect combination -> Removing cards...");
            Platform.runLater(() -> {
                if (quizCards.isEmpty()) {
                    upTimeTimer.cancel();
                    showResults();
                }
            });
        });
        return pauseTransition;
    }

    /**
     * Vytvori instanci PauseTransition, ktera vyvola pozastaveni na 1 sekundu (po tuto dobu bude par karet zvyrazen cervene)
     * a nasledne zrusi cervenou barvu na spatne vybranych kartach
     *
     * @param currentPairOfQuizCards aktualni par vybranych kvizovych karet
     * @return 1-sekundova PauseTransition
     */
    private PauseTransition getWrongPauseTransition(QuizCard[] currentPairOfQuizCards) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(e -> {
            currentPairOfQuizCards[0].setStyle(null);
            currentPairOfQuizCards[1].setStyle(null);

            currentPairOfQuizCards[0] = null;
            currentPairOfQuizCards[1] = null;

            System.out.println("LOG:\tWrong combination");
        });
        return pauseTransition;
    }

    /**
     * Z predane instance karty vytvori par kvizovych karet -> rozdeli jednu plnohodnotou kartu na otazku a odpoved
     *
     * @param card instance karty
     * @return par karet otazka + odpoved
     */
    private QuizCard[] createPairOfQuizCards(Card card) {
        QuizCard quizCardQuestion = new QuizCard(card, QuizCardType.QUESTION);
        QuizCard quizCardAnswer = new QuizCard(card, QuizCardType.ANSWER);
        return new QuizCard[]{quizCardQuestion, quizCardAnswer};
    }

    /**
     * Akce pro tlacitko zruseni kvizu a vraceni se zpet na domovskou stranku
     *
     * @throws IOException vyjimka pri nenactenem HomeLoaderu
     */
    @FXML
    private void cancel() throws IOException {
        quizCards.clear();
        gridPane.getChildren().clear();
        dataModel.getCurrentPairOfQuizCards()[0] = null;
        dataModel.getCurrentPairOfQuizCards()[1] = null;
        upTimeTimer.cancel();
        Main.switchSceneTo(Main.home);
    }

    /**
     * Zobrazi dialogove okno s vysledkem a casem kvizu
     */
    private void showResults() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Výsledky");
        alert.setHeaderText("Kvíz dokončen!");
        alert.setContentText(String.format("Čas = %s\nPočet chyb: %d", upTimeLabel.getText(), mistakes));

        DialogPane alertDP = alert.getDialogPane();
        alertDP.getStylesheets().add(Config.ACTIVE_STYLE_SHEET.get());
        alertDP.getStyleClass().add("alertDP");

        alert.setOnCloseRequest(e -> {
            try {
                Main.switchSceneTo(Main.home);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        alert.showAndWait();
    }

    /**
     * Zahaji kviz
     */
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
                Platform.runLater(() -> {
                    upTimeLabel.setText(String.format("%02d:%02d", min, sec));
                    if (sec<59 && min<59) {
                        sec++;
                    } else if (sec==59 && min<59) {
                        min++;
                        sec = 0;
                    }
                });
            }
        }, 0, 1000);
    }
}
