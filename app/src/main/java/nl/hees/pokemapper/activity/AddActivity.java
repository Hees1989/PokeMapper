package nl.hees.pokemapper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import nl.hees.pokemapper.R;
import nl.hees.pokemapper.view.CustomAdapter;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spPokemon;
    private String[] pokemonNames = {"Bulbasaur", "Ivysaur", "Venusaur"};
    private int[] pokemonImages = {R.drawable.image_001, R.drawable.image_002, R.drawable.image_003};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        spPokemon = (Spinner) findViewById(R.id.spPokemon);
        spPokemon.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), pokemonImages, pokemonNames);
        spPokemon.setAdapter(customAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
