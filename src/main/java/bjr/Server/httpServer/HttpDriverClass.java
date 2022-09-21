package bjr.Server.httpServer;

import bjr.Server.httpServer.config.Configuration;
import bjr.Server.httpServer.config.ConfigurationManager;
import bjr.Server.httpServer.core.ServerListenerThread;
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
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            // Listen and serve on the specified port and webroot
            serverListenerThread.start();
            // Start a thread using the ConnectionWorkerThread
        } catch (IOException e) {
            throw new RuntimeException(e);
        } // TODO handle later.


    }
}
