package client.entity;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import shared.entity.User;

/**
 * A hashmap with contacts saved on disk.
 *
 * @author Linnéa Mörk
 */
public class ContactList {
    private HashMap<String, User> contacts;
    private String filename;

    /**
     * Initializes the hashmap with what is stored on disk.
     * If the file does not exist, it creates it and initializes an empty hashmap.
     * @param filename The file to load.
     */
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

    /**
     * Returns the {@link User} keyed to the user name.
     * @param name user name you want to fetch a {@link User} for.
     * @return The {@link User}
     */
    public User get(String name) {
        return contacts.get(name);
    }

    /**
     * Puts a new user into the hashmap, and rewrites to disk
     * @param name The user name, used as key in the hashmap.
     * @param user The {@link User} object, used as value in the hashmap.
     */
    public void put(String name, User user) {
        contacts.put(name, user);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(contacts);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the key in the hashmap.
     * @return The key in the hashmap.
     */
    public Set<String> getMap() {
        return contacts.keySet();
    }
}
