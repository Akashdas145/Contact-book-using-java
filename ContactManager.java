import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContactManager {
    private List<Contact> contacts;
    private String fileName;

    public ContactManager(String username) {
        fileName = "contacts_" + username + ".dat";
        loadContacts();
    }

    @SuppressWarnings("unchecked")
    private void loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            contacts = (List<Contact>) ois.readObject();
        } catch (Exception e) {
            contacts = new ArrayList<>();
        }
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addContact(String name, String phone, String email) {
        contacts.add(new Contact(name, phone, email));
        saveContacts();
    }

    public void deleteContact(String name) {
        contacts.removeIf(c -> c.getName().equalsIgnoreCase(name));
        saveContacts();
    }

    public void updateContact(String oldName, String newName, String newPhone, String newEmail) {
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(oldName)) {
                c.setName(newName);
                c.setPhone(newPhone);
                c.setEmail(newEmail);
                break;
            }
        }
        saveContacts();
    }

    public Contact findContact(String name) {
        return contacts.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    public List<Contact> searchContacts(String keyword) {
        String key = keyword.toLowerCase();
        return contacts.stream()
                .filter(c -> c.getName().toLowerCase().contains(key) ||
                             c.getPhone().contains(key) ||
                             c.getEmail().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    public void sortContacts() {
        contacts.sort(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER));
        saveContacts();
    }
}
