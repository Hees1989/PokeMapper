package nl.hees.pokemapper.controller;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaimy on 10-8-2016.
 */
public class AddPokestopRequest extends StringRequest {
    private static String ADD_POKESTOP_REQUEST_URL = "http://pokemapper.net16.net/AddPokestop.php";
    private Map<String, String> params;

    public AddPokestopRequest(double lat, double lng, Response.Listener<String> listener) {
        super(Method.POST, ADD_POKESTOP_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("latitude", String.valueOf(lat));
        params.put("longitude", String.valueOf(lng));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
