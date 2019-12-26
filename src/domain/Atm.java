package domain;


public class Atm {
    private int id;
    private String address;
    private String no;

    public Atm(int id, String address, String no) {
        this(address, no);
        this.id = id;
    }

    public Atm(String address, String no) {
        super();
        this.address = address;
        this.no = no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}

