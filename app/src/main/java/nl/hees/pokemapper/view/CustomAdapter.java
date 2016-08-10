package nl.hees.pokemapper.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nl.hees.pokemapper.R;
import nl.hees.pokemapper.activity.AddActivity;

/**
 * Created by Jaimy on 10-8-2016.
 */
public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int[] pokemonImages;
    private String[] pokemonNames;
    private LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] pokemonImages, String[] pokemonNames) {
        this.context = applicationContext;
        this.pokemonImages = pokemonImages;
        this.pokemonNames = pokemonNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return pokemonImages.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.ivPokemonImage);
        TextView name = (TextView) view.findViewById(R.id.tvPokemonName);
        image.setImageResource(pokemonImages[i]);
        name.setText(pokemonNames[i]);
        return view;
    }
}
