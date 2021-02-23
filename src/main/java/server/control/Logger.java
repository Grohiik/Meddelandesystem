package server.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import shared.entity.Message;

public class Logger implements PropertyChangeListener {
    private String loggerFileName;
    private ArrayList<Message> messageList = new ArrayList<>();
    OutputStream out;
    ObjectOutputStream outputStream;
    LoggerUI loggerUI;

    public Logger(ServerController serverController, String loggerFilename) {
        this.loggerFileName = loggerFilename;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(loggerFilename);
            outputStream = new ObjectOutputStream(fileOutputStream);
            serverController.addLoggerListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("You have a problem with your logger file");
        }
        loggerUI = new LoggerUI();
        loggerUI.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        messageList.add((Message) evt.getNewValue());
        try {
            outputStream.writeObject(evt.getNewValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LoggerUI extends Thread {
        @Override
        public void run() {}
    }
}