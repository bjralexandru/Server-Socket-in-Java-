package bjr.Driver;

import bjr.Driver.config.Configuration;
import bjr.Driver.config.ConfigurationManager;
import core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/*
*
* Driver class for the Http Server
*
*/

public class HttpDriverClass {
    // Here we start using the logger
    private static Logger LOGGER = LoggerFactory.getLogger(HttpDriverClass.class);

    public static void main(String[] args) {


        LOGGER.info("Server starting...");

        // Now that we've built:
        // ConfigurationManager 1st (but not populated, just blank methods)
        // Configuration 2nd (blank methods)
        // the JSON util (1st fully completed script)
        // Completed the methods of ConfManager and Conf afterwards

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());


        try {
            // This code was originally inside the main class, but we've moved it over to
            // the ServerListenerThread.
            // Here we just create an instance of that class which takes its arguments from
            // the configuration file.
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } // TODO handle later.


    }
}
