package nl.hees.pokemapper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.hees.pokemapper.R;
import nl.hees.pokemapper.model.PokeMapperModel;
import nl.hees.pokemapper.model.Pokemon;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String LOCATION = "LOCATION";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        addPokemons();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_marker:
                // User chose the "Add marker" item
                Intent addActivityIntent = new Intent(MapsActivity.this, AddActivity.class);
                addActivityIntent.putExtra(LOCATION, mLastLocation);
                startActivity(addActivityIntent);
                break;
            case R.id.action_logout:
                // User chose the "Uitloggen" item
                PokeMapperModel.getInstance().setCurrentUser(null);
                Intent loginActivityIntent = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isUserLoggedIn() {
        return PokeMapperModel.getInstance().getCurrentUser() != null;
    }

    private void initialize() {
        if (!isUserLoggedIn()) {
            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        loadPokemonMarkers();
        loadPokestopMarkers();

    }

    private void loadPokemonMarkers() {
        String url = "http://pokemapper.net16.net/LoadPokemon.php";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray encounters = new JSONArray(response);

                    for (int i = 0; i < encounters.length(); i++) {
                        JSONObject jo = encounters.getJSONObject(i);
                        LatLng latLng = new LatLng(Double.parseDouble(jo.getString("latitude")), Double.parseDouble(jo.getString("longitude")));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(pokemons.get(Integer.parseInt(jo.getString("pokemonId"))).getName());
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(pokemons.get(Integer.parseInt(jo.getString("pokemonId"))).getImage()));
                        mMap.addMarker(markerOptions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = new StringRequest(url, listener, null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void loadPokestopMarkers() {
        String url = "http://pokemapper.net16.net/LoadPokestop.php";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray pokestops = new JSONArray(response);

                    for (int i = 0; i < pokestops.length(); i++) {
                        JSONObject jo = pokestops.getJSONObject(i);
                        LatLng latLng = new LatLng(Double.parseDouble(jo.getString("latitude")), Double.parseDouble(jo.getString("longitude")));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Pokéstop");
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pokestop));
                        mMap.addMarker(markerOptions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = new StringRequest(url, listener, null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("U bent hier.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        // Focus camera on current position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request cancelled, result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, disable the functionality that depends on this permission
                    Toast.makeText(this, "Goedkeuring geweigerd.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void addPokemons() {
        pokemons.add(new Pokemon("Bulbasaur", R.drawable.icon_001));
        pokemons.add(new Pokemon("Ivysaur", R.drawable.icon_002));
        pokemons.add(new Pokemon("Venusaur", R.drawable.icon_003));
        pokemons.add(new Pokemon("Charmander", R.drawable.icon_004));
        pokemons.add(new Pokemon("Charmeleon", R.drawable.icon_005));
        pokemons.add(new Pokemon("Charizard", R.drawable.icon_006));
        pokemons.add(new Pokemon("Squirtle", R.drawable.icon_007));
        pokemons.add(new Pokemon("Wartortle", R.drawable.icon_008));
        pokemons.add(new Pokemon("Blastoise", R.drawable.icon_009));
        pokemons.add(new Pokemon("Caterpie", R.drawable.icon_010));
        pokemons.add(new Pokemon("Metapod", R.drawable.icon_011));
        pokemons.add(new Pokemon("Butterfree", R.drawable.icon_012));
        pokemons.add(new Pokemon("Weedle", R.drawable.icon_013));
        pokemons.add(new Pokemon("Kakuna", R.drawable.icon_014));
        pokemons.add(new Pokemon("Beedrill", R.drawable.icon_015));
        pokemons.add(new Pokemon("Pidgey", R.drawable.icon_016));
        pokemons.add(new Pokemon("Pidgeotto", R.drawable.icon_017));
        pokemons.add(new Pokemon("Pidgeot", R.drawable.icon_018));
        pokemons.add(new Pokemon("Rattata", R.drawable.icon_019));
        pokemons.add(new Pokemon("Raticate", R.drawable.icon_020));
        pokemons.add(new Pokemon("Spearow", R.drawable.icon_021));
        pokemons.add(new Pokemon("Fearow", R.drawable.icon_022));
        pokemons.add(new Pokemon("Ekans", R.drawable.icon_023));
        pokemons.add(new Pokemon("Arbok", R.drawable.icon_024));
        pokemons.add(new Pokemon("Pikachu", R.drawable.icon_025));
        pokemons.add(new Pokemon("Raichu", R.drawable.icon_026));
        pokemons.add(new Pokemon("Sandshrew", R.drawable.icon_027));
        pokemons.add(new Pokemon("Sandslash", R.drawable.icon_028));
        pokemons.add(new Pokemon("Nidoran", R.drawable.icon_029));
        pokemons.add(new Pokemon("Nidorina", R.drawable.icon_030));
        pokemons.add(new Pokemon("Nidoqueen", R.drawable.icon_031));
        pokemons.add(new Pokemon("Nidoran", R.drawable.icon_032));
        pokemons.add(new Pokemon("Nidorino", R.drawable.icon_033));
        pokemons.add(new Pokemon("Nidoking", R.drawable.icon_034));
        pokemons.add(new Pokemon("Clefairy", R.drawable.icon_035));
        pokemons.add(new Pokemon("Clefable", R.drawable.icon_036));
        pokemons.add(new Pokemon("Vulpix", R.drawable.icon_037));
        pokemons.add(new Pokemon("Ninetales", R.drawable.icon_038));
        pokemons.add(new Pokemon("Jigglypuff", R.drawable.icon_039));
        pokemons.add(new Pokemon("Wigglytuff", R.drawable.icon_040));
        pokemons.add(new Pokemon("Zubat", R.drawable.icon_041));
        pokemons.add(new Pokemon("Golbat", R.drawable.icon_042));
        pokemons.add(new Pokemon("Oddish", R.drawable.icon_043));
        pokemons.add(new Pokemon("Gloom", R.drawable.icon_044));
        pokemons.add(new Pokemon("Vileplume", R.drawable.icon_045));
        pokemons.add(new Pokemon("Paras", R.drawable.icon_046));
        pokemons.add(new Pokemon("Parasect", R.drawable.icon_047));
        pokemons.add(new Pokemon("Venonat", R.drawable.icon_048));
        pokemons.add(new Pokemon("Venomoth", R.drawable.icon_049));
        pokemons.add(new Pokemon("Diglett", R.drawable.icon_050));
        pokemons.add(new Pokemon("Dugtrio", R.drawable.icon_051));
        pokemons.add(new Pokemon("Meowth", R.drawable.icon_052));
        pokemons.add(new Pokemon("Persian", R.drawable.icon_053));
        pokemons.add(new Pokemon("Psyduck", R.drawable.icon_054));
        pokemons.add(new Pokemon("Golduck", R.drawable.icon_055));
        pokemons.add(new Pokemon("Mankey", R.drawable.icon_056));
        pokemons.add(new Pokemon("Primeape", R.drawable.icon_057));
        pokemons.add(new Pokemon("Growlithe", R.drawable.icon_058));
        pokemons.add(new Pokemon("Arcanine", R.drawable.icon_059));
        pokemons.add(new Pokemon("Poliwag", R.drawable.icon_060));
    }
}
