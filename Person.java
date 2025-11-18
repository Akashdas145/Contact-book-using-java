import java.io.Serializable;

public class Person implements Serializable {
    protected String name;
    protected String phone;

    public Person(String name, String phone) {
        this.name = name.trim();
        this.phone = phone.trim();
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name.trim(); }
    public void setPhone(String phone) { this.phone = phone.trim(); }
}
