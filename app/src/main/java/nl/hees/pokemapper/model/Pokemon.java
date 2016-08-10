package nl.hees.pokemapper.model;

/**
 * Created by Jaimy on 5-8-2016.
 */
public class Pokemon {
    private static int pokemonId = 1;
    private String name;
    private int image;

    public Pokemon(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public static int getPokemonId() {
        return pokemonId;
    }

    public static void setPokemonId(int pokemonId) {
        Pokemon.pokemonId = pokemonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
