package client.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * FileIO writes object to disk.
 */
public class FileIO {
    private String directory = "data";

    public void setDirectory(String name) {
        directory = name;
    }

    public String getDirectory() {
        return directory;
    }

    public <T> boolean save(String filename, T object) {
        File data = new File(directory);
        if (!data.exists()) data.mkdir();

        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(directory + "/" + filename))) {
            oos.writeObject(object);
            oos.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public <T> T read(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                 new BufferedInputStream(new FileInputStream(directory + "/" + filename)))) {
            try {
                return (T) ois.readObject();
            } catch (ClassNotFoundException e) {
            }
        } catch (IOException e) {
        }

        return null;
    }
}
