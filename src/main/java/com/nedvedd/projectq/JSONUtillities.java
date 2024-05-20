package com.nedvedd.projectq;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nedvedd.projectq.data.Card;
import com.nedvedd.projectq.data.CardFolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JSONUtillities {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Deprecated
    public static void createDefaultCards() {
        String startOfArray = "{[";
        String sampleCard01Str = "{\"question\":\"Otazka 1\", \"answer\":\"Odpoved 1\"}";
        String sampleCard02Str = "{\"question\":\"Otazka 2\", \"answer\":\"Odpoved 2\"}";
        String endOfArray = "]}";
        //map json to card
        try{
            Card sampleCard01 = mapper.readValue(sampleCard01Str, Card.class);
            Card sampleCard02 = mapper.readValue(sampleCard02Str, Card.class);

//            System.out.println(sampleCard01);

            String fullJsonString = startOfArray + sampleCard01 + sampleCard02 + endOfArray;

            sampleCard01Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sampleCard01);


            System.out.println(sampleCard01Str);
        }
        catch (JsonParseException e) { e.printStackTrace();}
        catch (JsonMappingException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void saveJsonData(Object jsonDataObject) throws IOException {
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("src/main/java/com/nedvedd/projectq/userData/data.json"), jsonDataObject);
        System.out.println("LOG:\tData saved to JSON");
    }

    public static ObservableList<CardFolder> loadJsonData() {
        try {
            String json = Files.readString(Path.of("src/main/java/com/nedvedd/projectq/userData/data.json"));

            List<CardFolder> cardFolders = mapper.readValue(json, new TypeReference<List<CardFolder>>(){});
            System.out.println("LOG:\tData loaded from JSON");
//            System.out.println(json);
            return FXCollections.observableArrayList(cardFolders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
