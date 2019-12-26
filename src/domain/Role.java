package domain;

public class Role {
    private int id;
    private String no;
    private String description;

    public Role() {

    }

    public Role(int id, String no, String description) {
        this(no, description);
        this.id = id;
    }

    public Role(String no, String description) {
        super();
        this.no = no;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo() {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
