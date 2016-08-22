package nl.hees.pokemapper.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import nl.hees.pokemapper.R;
import nl.hees.pokemapper.controller.AddPokemonRequest;
import nl.hees.pokemapper.controller.AddPokestopRequest;
import nl.hees.pokemapper.view.CustomAdapter;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Location mLastLocation;
    private RadioGroup rgPickLocation;
    private RadioButton rbPokestop;
    private RadioButton rbPokemon;
    private Spinner spPokemon;
    private Button btnSubmit;
    private String[] pokemonNames = {"Bulbasaur", "Ivysaur", "Venusaur"};
    private int[] pokemonImages = {R.drawable.image_001, R.drawable.image_002, R.drawable.image_003};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mLastLocation = getIntent().getParcelableExtra(MapsActivity.LOCATION);
        final double lat = mLastLocation.getLatitude();
        final double lng = mLastLocation.getLongitude();

        spPokemon = (Spinner) findViewById(R.id.spPokemon);
        spPokemon.setOnItemSelectedListener(this);
        spPokemon.setEnabled(false);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), pokemonImages, pokemonNames);
        spPokemon.setAdapter(customAdapter);

        rbPokestop = (RadioButton) findViewById(R.id.rbPokestop);
        rbPokemon = (RadioButton) findViewById(R.id.rbPokemon);

        rgPickLocation = (RadioGroup) findViewById(R.id.rgPickLocation);
        rgPickLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbPokestop:
                        spPokemon.setEnabled(false);
                        break;
                    case R.id.rbPokemon:
                        spPokemon.setEnabled(true);
                        break;
                }
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (rgPickLocation.getCheckedRadioButtonId()) {
                    case R.id.rbPokestop:
                        addPokestop(lat, lng);
                        break;
                    case R.id.rbPokemon:
                        int pokemonId = spPokemon.getSelectedItemPosition();
                        addPokemon(lat, lng, pokemonId);
                        break;
                    default:
                        Toast.makeText(AddActivity.this, "U heeft nog geen type locatie geselecteerd.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addPokestop(double lat, double lng) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(AddActivity.this, MapsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddActivity.this, "Toevoegen is mislukt. Probeer het opnieuw.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AddPokestopRequest addPokestopRequest = new AddPokestopRequest(lat, lng, listener);
        RequestQueue queue = Volley.newRequestQueue(AddActivity.this);
        queue.add(addPokestopRequest);
    }

    private void addPokemon(double lat, double lng, int pokemonId) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(AddActivity.this, MapsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddActivity.this, "Toevoegen is mislukt. Probeer het opnieuw.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AddPokemonRequest addPokemonRequest = new AddPokemonRequest(lat, lng, pokemonId, listener);
        RequestQueue queue = Volley.newRequestQueue(AddActivity.this);
        queue.add(addPokemonRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
