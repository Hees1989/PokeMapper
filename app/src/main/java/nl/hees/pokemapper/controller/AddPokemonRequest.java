package nl.hees.pokemapper.controller;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaimy on 10-8-2016.
 */
public class AddPokemonRequest extends StringRequest {
    private static String ADD_POKEMON_REQUEST_URL = "http://pokemapper.net16.net/AddPokemon.php";
    private Map<String, String> params;

    public AddPokemonRequest(double lat, double lng, int pokemonId, Response.Listener<String> listener) {
        super(Method.POST, ADD_POKEMON_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("latitude", String.valueOf(lat));
        params.put("longitude", String.valueOf(lng));
        params.put("pokemonId", String.valueOf(pokemonId));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
