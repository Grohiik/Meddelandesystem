package server.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import shared.entity.Message;

public class Logger implements PropertyChangeListener {
    private final String loggerFileName;
    private ArrayList<Message> messageList;
    LoggerUI loggerUI;

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

    private class LoggerUI extends Thread {
        @Override
        public void run() {
            Date startDate;
            Date endDate;
            Date currentDate;
            Scanner scanner = new Scanner(System.in);
            while (!interrupted()) {
                try {
                    System.out.println("Enter start date");
                    startDate = DateFormat.getDateInstance().parse(scanner.nextLine());
                    System.out.println("Enter end date");
                    endDate = DateFormat.getDateInstance().parse(scanner.nextLine());
                    for (Message currentMessage : messageList) {
                        currentDate = currentMessage.getReceiveDate();
                        if (currentDate.after(startDate) && currentDate.before(endDate)) {
                            System.out.println(currentDate);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}