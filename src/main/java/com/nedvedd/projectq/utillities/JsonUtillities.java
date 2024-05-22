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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Trida se statickymi metodami pro operace s .json souborem
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class JsonUtillities {

    /** Instance ObjectMapperu, ktera ma funkce cteni/zapisu dat do .json souboru */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Ulozi data do data.json souboru
     *
     * @param jsonDataObject objekt k zapisu
     * @throws IOException vyjimka pri chybe zapisu do souboru
     */
    public static void saveJsonData(Object jsonDataObject) throws IOException {
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("src/main/java/com/nedvedd/projectq/userData/data.json"), jsonDataObject);
        System.out.println("LOG:\tData saved to JSON");
    }

    /**
     * Nacte data z data.json souboru (pokud soubor existuje)
     *
     * @return seznam s vsemi slozkami (vcetne jejich karet)
     */
    public static ObservableList<CardFolder> loadJsonData() {
        try {
            String json = Files.readString(Path.of("src/main/java/com/nedvedd/projectq/userData/data.json"));

            List<CardFolder> cardFolders = mapper.readValue(json, new TypeReference<List<CardFolder>>(){});
            System.out.println("LOG:\tData loaded from JSON");
            return FXCollections.observableArrayList(cardFolders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
