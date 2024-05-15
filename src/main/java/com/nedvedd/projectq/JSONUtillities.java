package com.nedvedd.projectq;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nedvedd.projectq.data.Card;

import java.io.IOException;

public class JSONUtillities {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void cretaeDefaultCards() {
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
}
