package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread  extends  Thread{
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    // This class needs a constructor which will take the Port
    // and the WebRoot.
    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port );
    }

    @Override
    public void run() {
        try {

            // Without the following while loop, our server would accept only 1 connection
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                // serverSocket as it is now will append every connection to a queue
                // which in the event of 1 connection taking too long, the others would
                // be stuck in the queue.


                // .accept() prompts the socket to accept any connection that arrives for the specified port.

                Socket socket = serverSocket.accept();

                // We store the connection in a Socket variable which acts like an entity
                // on which we can further perform particular tasks.

                // Let's see when a connection is accepted
                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());

                HTTPConnectionWorkerThread workerThread = new HTTPConnectionWorkerThread(socket);
                workerThread.start();
                }

            } catch (IOException e) {
                LOGGER.error("Problem with setting socket", e);
        } finally { // This is how you close a serverSocket.
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    // Disregard and close it. Don't throw anything.
                }
            }
        }
    }
}
