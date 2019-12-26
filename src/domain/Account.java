package domain;

public class Account {
    private int id;
    private String acct;
    private String passwd;
    private double balance;
    private String address;
    private Customer customer;
    private Role role;

    public Account() {
    }

    public Account(int id, String acct, String passwd, double balance, String address, Customer customer, Role role) {
        this(acct, passwd, balance, address, customer, role);
        this.id = id;
    }

    public Account(String acct, String passwd, double balance, String address, Customer customer, Role role) {
        super();
        this.acct = acct;
        this.passwd = passwd;
        this.balance = balance;
        this.address = address;
        this.customer = customer;
        this.role = role;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}