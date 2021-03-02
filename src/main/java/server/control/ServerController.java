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
    private LinkedList<MessageListener>
        connectedClientList; // Is created when clients tries to connect
    private Clients clients;
    private int port;

    public ServerController() {
        setPort(port);
        startServer();
    }
    public void setPort(int port) {
        this.port = port;
    }

    public void startServer() {
        ServerSocketListener serverSocketListener = new ServerSocketListener(port);
        serverSocketListener.start();
        System.out.println("Server has been started");
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
     * TODO keep or not keep listenerlist
     * Sub class that listens to incoming connections to the server. DONE
     * All connections receive a personal server thread. DONE
     * Transfers accepted incoming connections to MessageListener. DONE
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
        // listens for a tcp connection and creates a new thread for that connection. DONE
        // puts that object in an LinkedList to keep track of too. DONE
    }

    /**
     * TODO messageListener add messages to buffer DONE
     * Sub class that listens for messages and makes clients show as online if what the Listener
     * receive is valid
     */
    private class MessageListener extends Thread {
        private Socket socket;
        private ObjectInputStream objectInputStream;
        private User user;

        public MessageListener(Socket socket) {
            this.socket = socket;
            start();
        }

        @Override
        public void run() {
            boolean isValidUser = false;
            MessageSender messageSender;

            try {
                objectInputStream =
                    new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                /**
                 * TODO get user stuff
                 * TODO use the client not create the client FIX
                 * user comes from client
                 */
                try {
                    user = (User) objectInputStream.readObject();
                    Client client = new Client();
                    client.setIsOnline(true);
                    clients.put(user, client);

                    isValidUser = true;

                    messageSender = new MessageSender(user, socket);
                    messageSender.start();
                } catch (ClassNotFoundException e) {
                    System.err.println("ERROR, WRONG USER FORMAT");
                    return;
                }

                while (!interrupted()) {
                    try {
                        Message message = (Message) objectInputStream.readObject();
                        message.setSentTime(new Date());
                        messageSender.receivedMessages.put(message);
                    } catch (ClassNotFoundException e) {
                        System.err.println("ERROR, WRONG MESSAGE FORMAT");
                    }
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            connectedClientList.remove(this);
            if (isValidUser) {
                clients.get(user).setIsOnline(false);
            }
        }
        // one per connection DONE
        // listens for a message object and puts it in a buffer DONE
        // adds a received timestamp first DONE
    }

    // messageSender, tries to get msg from buffer and check if on or not using message DONE
    // listener USE RECEIVE TIME
    private class MessageSender extends Thread {
        private Buffer<Message> receivedMessages = new Buffer<>();
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
        // tries too get messages from a buffer. DONE
        // checks if the user it is meant for is online DONE
        // if it is it sends the message using MessageListener's socket (THIS IS LIKELY TO BREAK TRY
        // TOO FIX IT) if it isn't it adds it too UnsentMessages too be sent later. DONE
    }
}