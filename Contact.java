public class Contact extends Person {
    private String email;

    public Contact(String name, String phone, String email) {
        super(name, phone); // inherits name and phone
        this.email = email.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.trim(); }
}
