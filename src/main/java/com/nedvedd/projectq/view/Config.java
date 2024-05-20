package com.nedvedd.projectq.view;

import com.nedvedd.projectq.Main;
import javafx.beans.property.SimpleStringProperty;

public class Config {
    public static final String DARK_STYLE_SHEET = String.valueOf(Main.class.getResource("styles/darkStyle.css"));
    public static SimpleStringProperty ACTIVE_STYLE_SHEET = new SimpleStringProperty(DARK_STYLE_SHEET);
}
