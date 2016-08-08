package nl.hees.pokemapper.model;

/**
 * Created by Jaimy on 5-8-2016.
 */
public class User {
    private static int userId = 1;
    private String name, surname, username, password;

    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
