package bjr.Server.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public void parseHTTPrequest(InputStream inputStream) {
        // We don't make it static because of a DESIGN argument
        // It will allow us to do some configuration on the HTTP-Parser
        // Also, doing so - makes it available to each thread which we are working with.
        // Making it static would imply that any new thread will have its own instance of this
        // method.


    }
}
