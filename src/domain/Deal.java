package domain;

public class Deal {
    private int id;
    private String seq;
    private double dealMoney;
    private String dealType;
    private String rdate;
    private Account account;

    public Deal(String seq, double dealMoney, String dealType, String rdate, Account account) {
        super();
        this.seq = seq;
        this.dealMoney = dealMoney;
        this.dealType = dealType;
        this.rdate = rdate;
        this.account = account;
    }

    public Deal(int id, String seq, double dealMoney, String dealType, String rdate, Account account) {
        this(seq, dealMoney, dealType, rdate, account);
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public double getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(int dealMoney) {
        this.dealMoney = dealMoney;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }
}

