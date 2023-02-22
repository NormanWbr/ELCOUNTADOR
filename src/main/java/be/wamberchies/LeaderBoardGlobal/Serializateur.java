package be.wamberchies.LeaderBoardGlobal;

import java.io.*;

public class Serializateur {

    /**
     * Serialize an object to a file
     * @param object object to serialize
     * @param filePath path of the file
     * @param <T> type of the object
     */
    public static <T> void serialize(T object, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize an object from a file
     * @param filePath path of the file
     * @return the deserialized object
     * @param <T> type of the object
     * @throws FileNotFoundException if the file doesn't exist
     */
    public static <T> T deserialize(String filePath) throws FileNotFoundException {
        T object = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            object = (T) ois.readObject();

        } catch (FileNotFoundException | ClassNotFoundException e) {
            throw new FileNotFoundException("Pas de sauvegarde encore créée");

        } catch (IOException e) {
            e.printStackTrace();
            File file = new File(filePath);
            file.delete();
        }

        return object;
    }
}
