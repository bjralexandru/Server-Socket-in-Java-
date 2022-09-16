package bjr.Driver.config;

import bjr.Driver.util.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    // This is how to define it as a singleton.
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    /*
    *
    * The reason why we want to follow this pattern is because
    * we don't want more than 1 ConfigurationManager shared
    * between all the files in the project.
    *
    * */

    private ConfigurationManager(){

    }

    // This is the 1 instance we want to build
    // But also check if it doesn't exist already.
    public static ConfigurationManager getInstance(){
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    // Next we want to read a configuration file.

    /*
    *
    * Used to load a config file by the path provided
    *
    * We actually started writing these methods only after
    * writing all the JSON manipulating methods in the
    * JSON class inside the utility folder.
    *
    *
    * */
    public void loadConfigurationFile(String filePath)  {
        FileReader fileReader;
        // By default, you can add this exception to
        // the method's signature, but it's best practice
        // that you surround a FileReader with
        // try-catch with resources.
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuilder sb = new StringBuilder();
        int i;
        try {
            while ((i = fileReader.read()) != -1){
                sb.append((char)i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        JsonNode conf;
        try {
            conf = JSON.parse(sb.toString());
            // Here we've used the custom toString() method
            // we've just built
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration file", e);
        }

        try {
            myCurrentConfiguration = JSON.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration file, internal", e);
        }
    }
    /*
    *
    * Return the current loaded Configuration
    * */
    public Configuration getCurrentConfiguration() {
        if(myCurrentConfiguration == null){
            // This is an error handling example
            // for which we've built a separate class
            // pressed CMD + N and auto-generated the constructor methods.
            throw new HttpConfigurationException("No current configuration");
        }

        return myCurrentConfiguration;
    }
}
