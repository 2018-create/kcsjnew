package domain;

public class Customer {
    private int id;
    private String name;
    private String pid;
    private String phone;
    private String address;

    public Customer(int id, String name, String pid, String phone, String address) {
        this(name, pid, phone, address);
        this.id = id;
    }

    public Customer(String name, String pid, String phone, String address) {
        super();
        this.name = name;
        this.pid = pid;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

