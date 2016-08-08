package nl.hees.pokemapper.model;

/**
 * Created by Jaimy on 5-8-2016.
 */
public class Pokemon {
    private int pokemonId;
    private String name;

    public Pokemon(int pokemonId, String name) {
        this.pokemonId = pokemonId;
        this.name = name;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
