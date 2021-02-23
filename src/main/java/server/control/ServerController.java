package server.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import server.entity.Buffer;
import shared.entity.Message;

/**
 * @author Marcus Linné, Christian Heisterkamp
 * @version 1.0
 */
public class ServerController {
    Buffer<Message> receivedMessages;
    LinkedList<MessageListener> serverSocketObject;

    public ServerController() {}

    // TODO ServerSocketListener, with new thread and tcp, uses arraylist to keep track of objects
    private class ServerSocketListener extends Thread {
        int port;

        public ServerSocketListener(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket;

                while(!interrupted()) {
                    socket= serverSocket.accept();

                }
            }
            catch (IOException e){
                e.printStackTrace();
                System.err.println("not working");
            }
        }
        // listens for a tcp connection and creates a new thread for that connection.
        // puts that object in an ArrayList to keep track of too.
    }

    // TODO messageListener add messages to buffer
    private class MessageListener {
        // one per connection
        // listens for a message object and puts it in a buffer
        // adds a received timestamp first
    }

    // TODO messageSender, tries to get msg from buf and check if on or not using message listener
    private class MessageSender {
        // tries too get messages from a buffer.
        // checks if the user it is meant for is online
        // if it is it sends the message using MessageListener's socket (THIS IS LIKELY TO BREAK TRY
        // TOO FIX IT) if it isn't it adds it too UnsentMessages too be sent later.
    }
}
