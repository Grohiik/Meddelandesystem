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
import server.entity.Buffer;
import server.entity.UnsentMessages;
import shared.entity.*;

/**
 * ServerController that controls the server side
 *
 * @author Marcus Linné
 * @author Christian Heisterkamp
 * @author Linnéa Mörk
 * @version 1.0
 */
public class ServerController {
    private MessageSender messageSender = new MessageSender();
    private UnsentMessages unsentMessages = new UnsentMessages();
    private LinkedList<MessageListener>
        connectedClientList; // Is created when clients tries to connect
    private Clients clients;
    private int port;

    public ServerController(int port) {
        startServer(port);
    }

    /**
     * Used to start the server by giving the ServerSocketListener,
     * a port and by starting it.
     * @param port Port used by the ServerSocket.
     */
    public void startServer(int port) {
        this.port = port;
        ServerSocketListener serverSocketListener = new ServerSocketListener(port);
        serverSocketListener.start();
        messageSender.start();
        System.out.println("Server has been started");
    }

    /**
     * Used to turn off the server by closing the serverSocket.
     * @param serverSocket ServerSocket to be closed.
     */
    public void stopServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    PropertyChangeSupport loggerPropertyChange = new PropertyChangeSupport(this);

    public void addLoggerListener(PropertyChangeListener listener) {
        loggerPropertyChange.addPropertyChangeListener(listener);
    }

    /**
     * TODO keep or not keep ListenerList
     * ServerSocketListener, with new thread and tcp, uses LinkedList to keep track of objects
     * Sub class that listens to incoming connections to the server. DONE
     * All connections receive a personal server thread. DONE
     * Transfers accepted incoming connections to MessageListener. DONE
     */
    private class ServerSocketListener extends Thread {
        int port;

        /**
         * Constructor for ServerSocketListener.
         * @param port Port to listen to.
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
                    sendUserList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method used to transmit all users that are online
         */
        private void sendUserList() {
            ArrayList<User> users = new ArrayList<>();
            for (MessageListener listener : connectedClientList) {
                users.add(listener.user);
            }
            UserListMessage userListMessage =
                new UserListMessage(users.toArray(new User[users.size()]));

            messageSender.messagesToSend.put(userListMessage);
        }
    }

    /**
     * Messages are added to a buffer and then this class uses it's HashMap to figure out where to
     * send it. This is used to know who's the sender.
     * TODO add comments
     */
    private class MessageSender extends Thread {
        private Buffer<IMessage> messagesToSend = new Buffer<>();
        private HashMap<User, ClientTransmission> clientTransmissions = new HashMap<>();

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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * MessageListener puts messages into a buffer DONE
     * Sub class that listens for messages and makes clients show as online if what the Listener
     * receive is valid. Sets receive time as well on the message.
     * Each connected client has a MessageListener.
     * When User connects it will check if there are any,
     * non delivered messages to the user and receive those.
     */
    private class MessageListener extends Thread {
        private ServerSocketListener serverSocketListener;
        private Socket socket;
        private ObjectInputStream objectInputStream;
        private User user;

        public MessageListener(Socket socket, ServerSocketListener serverSocketListener) {
            this.socket = socket;
            this.serverSocketListener = serverSocketListener;
            start();
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
                        clients.put(user, new Client());
                    }

                    Client client = clients.get(user);
                    client.setIsOnline(true);
                    clients.put(user, client);

                    isValidUser = true;

                    ClientTransmission clientTransmission =
                        new ClientTransmission(user, socket, objectOutputStream);
                    messageSender.addClientTransmission(user, clientTransmission);

                    if (unsentMessages.get(user) != null) {
                        ArrayList<Message> currentUnsentMessages = unsentMessages.get(user);
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
                        IMessage message = (IMessage) objectInputStream.readObject();
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
            serverSocketListener.sendUserList();

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
        ObjectOutputStream objectOutputStream;
        private Buffer<IMessage> receivedMessages = new Buffer<>();
        private Socket socket;
        private User user;

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
