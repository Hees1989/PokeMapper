package nl.hees.pokemapper.model;

/**
 * Created by Jaimy on 5-8-2016.
 */
public class PokeMapperModel {
    private static PokeMapperModel instance = null;
    private User currentUser = null;

    public static PokeMapperModel getInstance() {
        if (instance == null) {
            instance = new PokeMapperModel();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
