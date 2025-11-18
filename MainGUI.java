import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainGUI {
    private JFrame frame;
    private DefaultTableModel tableModel;
    private ContactManager cm;
    private AuthManager auth;
    private String currentUser;

    public MainGUI() {
        frame = new JFrame("Contact Book");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        auth = new AuthManager();
        showLoginPanel();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }

    // ---------------- Login Panel ----------------
    private void showLoginPanel() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(registerBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (auth.login(user, pass)) {
                currentUser = user;
                cm = new ContactManager(currentUser);
                showContactPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!");
            }
        });

        registerBtn.addActionListener(e -> {
            String user = JOptionPane.showInputDialog(frame, "Enter new username:");
            String pass = JOptionPane.showInputDialog(frame, "Enter new password:");
            if (user != null && pass != null) {
                if (auth.register(user, pass)) {
                    JOptionPane.showMessageDialog(frame, "User registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Username already exists!");
                }
            }
        });
    }

    // ---------------- Contact Panel ----------------
    private void showContactPanel() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        String[] columns = {"Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add Contact");
        JButton editBtn = new JButton("Edit Contact");
        JButton deleteBtn = new JButton("Delete Contact");
        JButton searchBtn = new JButton("Search");
        JButton sortBtn = new JButton("Sort A-Z");
        JButton logoutBtn = new JButton("Logout");

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(searchBtn);
        btnPanel.add(sortBtn);
        btnPanel.add(logoutBtn);
        frame.add(btnPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();

        refreshContacts(cm.getAllContacts());

        // Add contact
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Name:");
            String phone = JOptionPane.showInputDialog("Phone:");
            String email = JOptionPane.showInputDialog("Email:");
            if (!name.isEmpty()) {
                cm.addContact(name, phone, email);
                refreshContacts(cm.getAllContacts());
            }
        });

        // Edit contact
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String oldName = (String) table.getValueAt(row, 0);
                Contact c = cm.findContact(oldName);
                String newName = JOptionPane.showInputDialog("Name:", c.getName());
                String newPhone = JOptionPane.showInputDialog("Phone:", c.getPhone());
                String newEmail = JOptionPane.showInputDialog("Email:", c.getEmail());
                cm.updateContact(oldName, newName, newPhone, newEmail);
                refreshContacts(cm.getAllContacts());
            } else {
                JOptionPane.showMessageDialog(frame, "Select a contact to edit!");
            }
        });

        // Delete contact
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String name = (String) table.getValueAt(row, 0);
                cm.deleteContact(name);
                refreshContacts(cm.getAllContacts());
            } else {
                JOptionPane.showMessageDialog(frame, "Select a contact to delete!");
            }
        });

        // Search contact
        searchBtn.addActionListener(e -> {
            String key = JOptionPane.showInputDialog("Enter keyword to search:");
            if (!key.isEmpty()) {
                refreshContacts(cm.searchContacts(key));
            }
        });

        // Sort contacts
        sortBtn.addActionListener(e -> {
            cm.sortContacts();
            refreshContacts(cm.getAllContacts());
        });

        // Logout
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cm = null;
            tableModel.setRowCount(0);
            showLoginPanel();
        });
    }

    private void refreshContacts(List<Contact> contacts) {
        tableModel.setRowCount(0);
        for (Contact c : contacts) {
            tableModel.addRow(new Object[]{c.getName(), c.getPhone(), c.getEmail()});
        }
    }
}
