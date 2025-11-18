import java.io.*;
import java.util.HashMap;

public class AuthManager {
    private HashMap<String, String> users;
    private static final String FILE = "users.dat";

    public AuthManager() {
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            users = (HashMap<String, String>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
