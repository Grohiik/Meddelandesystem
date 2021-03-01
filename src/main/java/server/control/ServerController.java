package server.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import server.entity.Buffer;
import server.entity.UnsentMessages;
import shared.entity.Client;
import shared.entity.Clients;
import shared.entity.Message;
import shared.entity.User;

/**
 * ServerController that controls the server side
 * @author Marcus Linné, Christian Heisterkamp
 * @version 1.0
 */
public class ServerController {
    private Buffer<Message> receivedMessages;
    private LinkedList<MessageListener>
        connectedClientList; // Is created when clients tries to connect
    private Clients clients;
    private int port;

    public void setPort(int port) {
        this.port = port;
    }

    public void startServer() {
        ServerSocketListener serverSocketListener = new ServerSocketListener(port);
        serverSocketListener.start();
    }

    /**
     * Used to turn off the server by closing the serverSocket
     * @param serverSocket
     */
    public void stopServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // TODO ServerSocketListener, with new thread and tcp, uses LinkedList to keep track of objects
    /**
     * Sub class that listens to incoming connections to the server.
     * All connections receive a personal server thread.
     * Transfers accepted incoming connections to MessageListener.
     */
    private class ServerSocketListener extends Thread {
        int port;

        public ServerSocketListener(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket;

                clients = new Clients();

                if (connectedClientList == null) {
                    connectedClientList = new LinkedList<>();
                }

                while (!interrupted()) {
                    socket = serverSocket.accept();
                    MessageListener messageListener = new MessageListener(socket);
                    connectedClientList.add(messageListener);
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    // TODO messageSender, tries to get msg from buffer and check if on or not using message
    // listener
    // USE RECEIVE TIME
    private class MessageSender extends Thread {
        private ObjectOutputStream objectOutputStream;
        private Socket socket;
        private User user;

        public MessageSender(User user, Socket socket) {
            this.user = user;
            this.socket = socket;
            start();
        }

        @Override
        public void run() {
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                if (!receivedMessages.isEmpty()) {
                    while (!interrupted()) {
                        Client client = new Client();
                        Message message = receivedMessages.get();
                        message.setReceiveTime(new Date());

                        if (client.getIsOnline() == true) {
                            objectOutputStream.writeObject(message);
                        } else {
                            UnsentMessages unsentMessages = new UnsentMessages();
                            unsentMessages.put(user, message);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        // tries too get messages from a buffer.
        // checks if the user it is meant for is online
        // if it is it sends the message using MessageListener's socket (THIS IS LIKELY TO BREAK TRY
        // TOO FIX IT) if it isn't it adds it too UnsentMessages too be sent later.
    }
}