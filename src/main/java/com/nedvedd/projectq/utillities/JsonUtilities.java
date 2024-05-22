package com.nedvedd.projectq.utillities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nedvedd.projectq.model.CardFolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Trida se statickymi metodami pro operace s .json souborem
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class JsonUtilities {

    /** Instance ObjectMapperu, ktera ma funkce cteni/zapisu dat do .json souboru */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Ulozi data do userData.json souboru
     *
     * @param jsonDataObject objekt k zapisu
     * @throws IOException vyjimka pri chybe zapisu do souboru
     */
    public static void saveJsonData(Object jsonDataObject) throws IOException {
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File("userData.json"), jsonDataObject);
        System.out.println("LOG:\tData saved to JSON");
    }

    /**
     * Nacte data z userData.json souboru (pokud soubor existuje)
     *
     * @return seznam s vsemi slozkami (vcetne jejich karet)
     */
    public static ObservableList<CardFolder> loadJsonData() {
        try {
            List<CardFolder> cardFolders = mapper.readValue(new File("userData.json"), new TypeReference<List<CardFolder>>(){});
            System.out.println("LOG:\tData loaded from JSON");
            return FXCollections.observableArrayList(cardFolders);
        } catch (IOException e) {
            System.out.println("LOG:\tNo saved data, generating sample folders with cards...");
        }
        return null;
    }
}
