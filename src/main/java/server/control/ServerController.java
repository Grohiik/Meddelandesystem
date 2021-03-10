package server.control;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import server.entity.*;
import shared.entity.*;

/**
 * ServerController that controls the server side.
 *
 * @author Marcus Linné
 * @author Christian Heisterkamp
 * @author Linnéa Mörk
 * @version 1.0
 */
public class ServerController {
    private PropertyChangeSupport loggerPropertyChange = new PropertyChangeSupport(this);
    private MessageSender messageSender = new MessageSender();
    private UnsentMessages unsentMessages = new UnsentMessages();
    private LinkedList<MessageListener> connectedClientList;
    private Clients clients;
    private Logger logger;
    private int port;

    /**
     * ServerController constructor that starts the server.
     *
     * @param port The port used by the ServerSocket.
     */
    public ServerController(int port) {
        startServer(port);
    }

    /**
     * Used to start the server by giving the ServerSocketListener,
     * a port and by starting it.
     *
     * @param port The port used by the ServerSocket.
     */
    public void startServer(int port) {
        this.port = port;
        ServerSocketListener serverSocketListener = new ServerSocketListener(port);
        serverSocketListener.start();
        messageSender.start();
        logger = new Logger(this, "Logger.log");
        System.out.println("Server has been started");
    }

    /**
     * Adds a logger listener.
     *
     * @param listener that is used to log.
     */
    public void addLoggerListener(PropertyChangeListener listener) {
        loggerPropertyChange.addPropertyChangeListener(listener);
    }

    /**
     * Method used to transmit which users that are online.
     */
    private void sendUserList() {
        ArrayList<User> users = new ArrayList<>();
        for (MessageListener listener : connectedClientList) {
            users.add(listener.user);
        }

        IMessage userListMessage = new UserListMessage(users.toArray(new User[users.size()]));

        messageSender.messagesToSend.put(userListMessage);
    }

    /**
     * ServerSocketListener is a sub class that listens to incoming connections.
     * Each connection receive a individual thread and transfers all incoming connections to the
     * MessageListener.
     */
    private class ServerSocketListener extends Thread {
        int port;

        /**
         * Constructor for ServerSocketListener.
         *
         * @param port The port to listen to.
         */
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
                    MessageListener messageListener = new MessageListener(socket, this);
                    connectedClientList.add(messageListener);
                    messageListener.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Messages are added to a buffer and then this class uses its HashMap to figure out where to
     * send it. It also invokes a PropertyChange so that the logger can notice any actions made.
     * Has a Buffer to store messages waiting to be sent and a HashMap which stores available
     * clients.
     */
    private class MessageSender extends Thread {
        private Buffer<IMessage> messagesToSend = new Buffer<>();
        private HashMap<User, ClientTransmission> clientTransmissions = new HashMap<>();

        /**
         * Adds a ClientTransmission to the internal hashmap
         * so that it can send messages via that ClientTransmission.
         *
         * @param user The key for the hashmap.
         * @param clientTransmission The value in the hashmap and the ClientTransmission to send
         *                           messages via.
         */
        public void addClientTransmission(User user, ClientTransmission clientTransmission) {
            clientTransmissions.put(user, clientTransmission);
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    IMessage message = messagesToSend.get();
                    User[] receivers = message.getReceiverList();
                    for (User receiver : receivers) {
                        ClientTransmission clientTransmission = clientTransmissions.get(receiver);
                        if (clientTransmission != null) {
                            clientTransmission.receivedMessages.put(message);
                        }
                    }
                    if (message instanceof Message) {
                        loggerPropertyChange.firePropertyChange(null, null,
                                                                new Message((Message) message));
                    } else if (message instanceof UserListMessage) {
                        loggerPropertyChange.firePropertyChange(
                            null, null, new UserListMessage((UserListMessage) message));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sub class that listens for messages from its client, each client has a MessageListener.
     * Makes clients show as online if the Listener receive valid info and sets receive time of each
     * message. When a User connects it will check if there are any non delivered messages
     * to the user and receive the messages.
     */
    private class MessageListener extends Thread {
        private ServerSocketListener serverSocketListener;
        private Socket socket;
        private ObjectInputStream objectInputStream;
        private User user;

        /**
         * MessageListener constructor that receive the socket it should listen to messages on.
         *
         * @param socket The socket used.
         * @param serverSocketListener The serverSocketListener it was assigned.
         */
        public MessageListener(Socket socket, ServerSocketListener serverSocketListener) {
            this.socket = socket;
            this.serverSocketListener = serverSocketListener;
        }

        @Override
        public void run() {
            boolean isValidUser = false;

            try {
                ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream());

                objectInputStream =
                    new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                try {
                    user = (User) objectInputStream.readObject();
                    if (clients.get(user) == null) {
                        clients.put(user, new Client(user));
                    }

                    Client client = clients.get(user);
                    client.setIsOnline(true);
                    clients.put(user, client);

                    isValidUser = true;

                    ClientTransmission clientTransmission =
                        new ClientTransmission(user, socket, objectOutputStream);
                    messageSender.addClientTransmission(user, clientTransmission);

                    sendUserList();

                    var allUnsentMessages = unsentMessages.get(user);
                    if (allUnsentMessages != null) {
                        ArrayList<Message> currentUnsentMessages = allUnsentMessages;
                        for (Message currentMessage : currentUnsentMessages) {
                            clientTransmission.receivedMessages.put(currentMessage);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("ERROR, WRONG USER FORMAT");
                    return;
                }

                while (!interrupted()) {
                    try {
                        Message message = (Message) objectInputStream.readObject();
                        message.setSentTime(new Date());
                        messageSender.messagesToSend.put(message);
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
            sendUserList();

            if (isValidUser) {
                clients.get(user).setIsOnline(false);
            }
        }
    }

    /**
     * ClientTransmission is used to get a msg from buffer and send it to the recipient.
     * If recipient is online, otherwise it calls for UnsentMessages.
     * There it is added to be sent when recipient comes online.
     */
    private class ClientTransmission extends Thread {
        private ObjectOutputStream objectOutputStream;
        private Buffer<IMessage> receivedMessages = new Buffer<>();
        private Socket socket;
        private User user;

        /**
         * ClientTransmission constructor used to start the ClientTransmission thread.
         *
         * @param user The user the message is to be sent to.
         * @param socket The socket used.
         * @param objectOutputStream The objectOutputStream used to write objects (messages).
         */
        public ClientTransmission(User user, Socket socket, ObjectOutputStream objectOutputStream) {
            this.user = user;
            this.socket = socket;
            this.objectOutputStream = objectOutputStream;
            start();
        }

        @Override
        public void run() {
            try {
                Client client = clients.get(user);

                while (!interrupted()) {
                    IMessage message = receivedMessages.get();

                    if (client.getIsOnline()) {
                        message.setReceiveTime(new Date());
                        objectOutputStream.writeObject(message);
                    } else {
                        if (message instanceof Message) {
                            unsentMessages.put(user, (Message) message);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}