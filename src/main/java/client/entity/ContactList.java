package client.entity;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import shared.entity.User;

public class ContactList {
    private HashMap<String, User> contacts;
    private String filename;

    public ContactList(String filename) {
        this.filename = filename;
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            contacts = (HashMap<String, User>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            File file = new File(filename);
            try {
                file.createNewFile();
                contacts = new HashMap<>();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User get(String name) {
        return contacts.get(name);
    }

    public void put(String name, User user) {
        contacts.put(name, user);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getMap() {
        return contacts.keySet();
    }
}
