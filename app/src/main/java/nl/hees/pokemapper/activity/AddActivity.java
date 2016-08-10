package nl.hees.pokemapper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import nl.hees.pokemapper.R;
import nl.hees.pokemapper.view.CustomAdapter;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean isPokestopChecked = true;
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
                        isPokestopChecked = true;
                        break;
                    case R.id.rbPokemon:
                        spPokemon.setEnabled(true);
                        isPokestopChecked = false;
                        break;
                }
                System.out.println(i);
                Log.d("Checked ID: ", "" + rgPickLocation.getCheckedRadioButtonId());
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPokestopChecked) {
                    addPokestop();
                } else {
                    addPokemon();
                }
            }
        });
    }

    private void addPokestop() {

    }

    private void addPokemon() {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
