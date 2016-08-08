package nl.hees.pokemapper.controller;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaimy on 7-8-2016.
 */
public class RegisterRequest extends StringRequest {
    private static String REGISTER_REQUEST_URL = "http://pokemapper.net16.net/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String password, String name, String surname, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("name", name);
        params.put("surname", surname);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    /*@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }*/
}
