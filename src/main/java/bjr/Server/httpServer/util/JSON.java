package bjr.Server.httpServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;


public class JSON {
    /*
    *
    * This is how you parse JSON using Jackson
    *
    * Dependencies are inserted into the pom.xml file
    *
    * */

    private static final ObjectMapper myObjectMapper = defaultObjMapper();

    // A method that creates a new ObjMapper
    private static ObjectMapper defaultObjMapper() {
        ObjectMapper om = new ObjectMapper();
        // In case 1 property in the JSON we're trying to parse is missing
        // the next configure line allows the code not to fail.
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return om;
    }

    // This is how to pass a String to a JsonNode:

    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        // This method will take the JSON source and pass it
        // further to our object mapper.
        return myObjectMapper.readTree(jsonSrc);
    }


    // We need a way to move this JsonNode to our Configuration
    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        /*
        * <A> is called a generic, and basically it means
        * that we don't know what type the method is going
        * to return.
        *
        * the A afterwards indicates it return an "A" type.
        *
        * also it takes a generic class of the same type
        * as argument and basically what we want is to
        * transform the JsonNode into that class.
        * */
        return myObjectMapper.treeToValue(node, clazz);
    }

    /*
    *
    * Also, to make it more complete we want to be able
    * to transform the Configuration file into a JsonNode
    *
    */
    public static JsonNode toJson(Object obj){
        // Generates a JsonNode from a class

        return myObjectMapper.valueToTree(obj);
    }

    // This is a wrapper for the generateJson method
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    // This is built in order to display the JSON
    // indented.
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    // Can come handy to see a JsonNode as a String
    // and for that is the next method.
    private static String generateJson(Object o, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(o);
    }

}
