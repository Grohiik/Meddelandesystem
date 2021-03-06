package server.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import shared.entity.IMessage;
import shared.entity.Message;
import shared.entity.UserListMessage;

/**
 * Logger that logs all messages being sent to the server.
 *
 * @author Linnéa Mörk
 * @author Christian Heisterkamp
 * @author Marcus Linné
 * @version 1.0
 */
public class Logger implements PropertyChangeListener {
    private final String loggerFileName;
    private ArrayList<IMessage> messageList;
    LoggerUI loggerUI;

    /**
     * Takes a ServerController and subscribes to it's PropertyChangeSupport,
     * and a file to store the traffic in. Creates a new file if it doesn't exist.
     * TODO improve file creation if possible
     *
     * @param serverController The ServerController to subscribe to.
     * @param loggerFilename The filename of the file to store the traffic in.
     */
    public Logger(ServerController serverController, String loggerFilename) {
        this.loggerFileName = loggerFilename;
        try {
            FileInputStream fileInputStream = new FileInputStream(loggerFilename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            messageList = (ArrayList<IMessage>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(loggerFilename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                messageList = new ArrayList<>();
                messageList.add(new Message());
                objectOutputStream.writeObject(messageList);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        serverController.addLoggerListener(this);
        loggerUI = new LoggerUI();
        loggerUI.start();
    }

    /**
     * PropertyChange that gets called when the server receives a new message.
     * Adds the message to the messageList and rewrites object file.
     *
     * @param evt event with the "new Value" being the message to add
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        IMessage message = (IMessage) evt.getNewValue();
        message.setReceiveTime(new Date());
        if (message instanceof Message) {
            ((Message) message).setImage(null);
        }
        messageList.add(message);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(loggerFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(messageList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs on a separate thread in a infinite loop.
     * Gets a start date and an end date,
     * prints every message that was send inside of those dates
     */
    private class LoggerUI extends Thread {
        @Override
        public void run() {
            Date startDate;
            Date endDate;
            Date currentDate;
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ArrayList<String> lastList = new ArrayList<>();
            ArrayList<String> thisList;

            System.out.println("The server logger is online and running");

            while (!interrupted()) {
                try {
                    System.out.println("-----------------------------------------");
                    System.out.print("Enter start date: ");
                    startDate = format.parse(scanner.nextLine());
                    System.out.print("Enter end date: ");
                    endDate = format.parse(scanner.nextLine());
                    System.out.println("-----------------------------------------");
                    System.out.println("Showing log between: " + format.format(startDate) + " and "
                                       + format.format(endDate));
                    System.out.println("-----------------------------------------");

                    for (IMessage currentMessage : messageList) {
                        currentDate = currentMessage.getReceiveTime();

                        if (currentDate == null) {
                            continue;
                        }
                        if (currentDate.after(startDate) && currentDate.before(endDate)) {
                            if (currentMessage instanceof UserListMessage) {
                                thisList = new ArrayList<>(
                                    Arrays.asList(currentMessage.toString().split(" ")));

                                for (String user : thisList) {
                                    if (!lastList.contains(user) && !user.equals("")) {
                                        System.out.println(
                                            user + " connected at: " + format.format(currentDate));
                                    }
                                }

                                for (String user : lastList) {
                                    if (!thisList.contains(user) && !user.equals("")) {
                                        System.out.println(user + " disconnected at: "
                                                           + format.format(currentDate));
                                    }
                                }

                                lastList = thisList;
                            } else {
                                System.out.println(currentMessage);
                            }
                        }
                        if (currentDate.after(endDate)) {
                            break;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}