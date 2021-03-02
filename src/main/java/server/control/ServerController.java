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
import shared.entity.*;

/**
 * ServerController that controls the server side
 *
 * @author Marcus Linné, Christian Heisterkamp, Linnéa Mörk
 * @version 1.0
 */
public class ServerController {
    private LinkedList<MessageListener>
        connectedClientList; // Is created when clients tries to connect
    private Clients clients;
    private int port;

    public ServerController() {
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

    /**
     * TODO keep or not keep ListenerList
     * ServerSocketListener, with new thread and tcp, uses LinkedList to keep track of objects
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

        private void sendClientList() {
            ArrayList<User> users = new ArrayList<>();
            for (MessageListener listener : connectedClientList) {
                users.add(listener.user);
            }

            UserListMessage userListMessage = new UserListMessage((User[]) users.toArray());

            messageSender.messagesToSend.put(userListMessage);
        }
    }

    /**
     * MessageListener puts messages into a buffer DONE
     * Sub class that listens for messages and makes clients show as online if what the Listener
     * receive is valid. Sets receive time as well on the message.
     * Each connected client has a MessageListener.
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

                // TODO use the client not create the client FIX
                // User comes from client
                try {
                    user = (User) objectInputStream.readObject();
                    Client client = new Client();
                    client.setIsOnline(true);
                    clients.put(user, client);

                    isValidUser = true;

                    ClientTransmission clientTransmission = new ClientTransmission(user, socket);
                    messageSender.addClientTransmission(user, clientTransmission);
                    clientTransmission.start();
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
            if (isValidUser) {
                clients.get(user).setIsOnline(false);
            }
        }
    }

    // TODO checks if the user it is meant for is online?
    /**
     * ClientTransmission is used to get a msg from buffer and send it to the recipient.
     * If recipient is online, otherwise it calls for UnsentMessages.
     */
    private class ClientTransmission extends Thread {
        private Buffer<IMessage> receivedMessages = new Buffer<>();
        private Socket socket;
        private User user;

        public ClientTransmission(User user, Socket socket) {
            this.user = user;
            this.socket = socket;
            start();
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream());
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