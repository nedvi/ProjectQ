package com.nedvedd.projectq.view;

import com.nedvedd.projectq.Main;
import javafx.beans.property.SimpleStringProperty;

/**
 * Trida se statickymi konstantami pro jednoduchy prisup k casto pouzivanym hodnotam
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class Config {
    /** Konstanta cesty pro tmavy motiv (CSS) */
    public static final String DARK_STYLE_SHEET = String.valueOf(Main.class.getResource("styles/darkStyle.css"));

    /** Reference na aktualne zvoleny motiv (do budoucna, prozatim je dostupny pouze jeden) */
    public static SimpleStringProperty ACTIVE_STYLE_SHEET = new SimpleStringProperty(DARK_STYLE_SHEET);
}
