package client.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * FileIO writes generic object to disk.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class FileIO {
    private String directory = "data";

    /**
     * Set the directory data name.
     *
     * @param name The data directory name or path.
     */
    public void setDirectory(String name) {
        directory = name;
    }

    /**
     * Get the directory name.
     *
     * @return The name or path of the data directory.
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Save the object to disk.
     *
     * @param <T>      Type to use for saving the object
     * @param filename The intended filename for the written fie
     * @param object   The object to be saved
     *
     * @return {@code true} success, {@code false} failed to save.
     */
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

    /**
     * Read the stored binary file.
     *
     * @param <T>      The type of the object to cast to.
     * @param filename The name of the data file.
     * @param type     The type to cast to when returning the object.
     *
     * @return The read object with correct type.
     */
    public <T> T read(String filename, Class<T> type) {
        try (ObjectInputStream ois = new ObjectInputStream(
                 new BufferedInputStream(new FileInputStream(directory + "/" + filename)))) {
            try {
                return type.cast(ois.readObject());
            } catch (ClassNotFoundException e) {
            }
        } catch (IOException e) {
        }

        return null;
    }
}
