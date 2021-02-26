package server.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import server.control.ServerController;
import shared.entity.Message;

/**
 * Logger that logs all messages being sent to the server.
 *
 * @author Linnéa Mörk, Christian Heisterkamp
 * @version 1.0
 */
public class Logger implements PropertyChangeListener {
    private final String loggerFileName;
    private ArrayList<Message> messageList;
    LoggerUI loggerUI;

    /**
     * Takes a ServerController and subscribes to its PropertyChangeSupport,
     * and a file to store the traffic in. Creates a new file if it doesn't exist.
     *
     * @param serverController The ServerController to subscribe to.
     * @param loggerFilename The filename of the file to store the traffic in.
     */
    public Logger(ServerController serverController, String loggerFilename) {
        this.loggerFileName = loggerFilename;
        try {
            FileInputStream fileInputStream = new FileInputStream(loggerFilename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            messageList = (ArrayList<Message>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            File file = new File(loggerFileName);
            try {
                file.createNewFile();
                messageList = new ArrayList<>();
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
     * @param evt event with the "new Value" being the message too add
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        messageList.add((Message) evt.getNewValue());
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
            while (!interrupted()) {
                try {
                    System.out.println("Enter start date");
                    startDate = format.parse(scanner.nextLine());
                    System.out.println("Enter end date");
                    endDate = format.parse(scanner.nextLine());
                    for (Message currentMessage : messageList) {
                        currentDate = currentMessage.getReceiveDate();
                        if (currentDate.after(startDate) && currentDate.before(endDate)) {
                            System.out.println(currentMessage);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}