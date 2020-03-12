package myClass;

public class User {
    private String email;
    private String name;
    private String userName;
    private String rank;
    private String password;
    private int id;

    public User(String name, String rank, String userName, String email, String password, int id) {
        this.email = email;
        this.name = name;
        this.rank = rank;
        this.id = id;
        this.password = password;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
