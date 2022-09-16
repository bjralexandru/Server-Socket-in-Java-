package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPConnectionWorkerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPConnectionWorkerThread.class);

    // This class will address the issue of handling each
    // connection independent to the others in the socket queue.
    private Socket socket;

    public HTTPConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Define them outside to give "finally"
        // the scope to these variables.
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            // This is how you read the input to our server on the socket connection
            outputStream = socket.getOutputStream();
            // This is how we return the response to the request


            // TODO we would do the writing
            // We build the page to send to the browser

            String html = "<html><head><title>Simple Java HTTP Server </title><body><h1>This page was served using my Simple Java HTTP Server.</h1></body></head></html>";

            // Tell the server how to use this html String
            // Also this is part of the HTTP-protocol which we'll cover in the next part of the project
            final String CRLF = "\n\r"; // 13, 10 ASCII
            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;
            outputStream.write(response.getBytes());


            LOGGER.info(" * Connection processing finished.");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Problem with communication", e);
        } finally {
            // Don't forget to close all the connections and streams.
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) { // Disregard and close.
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) { // Disregard and close.
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) { // Disregard and close.
                    }
                }

            }
        }
    }
}
