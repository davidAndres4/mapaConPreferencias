package com.example.miprimermapa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.miprimermapa.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    //marcador
    private Marker markerJulian;
    private Marker markerRibera;
    private Marker markerGregorio;
    private Marker markerSidney;

    //latitudes y longitudes
    LatLng julian;
    LatLng ribera;
    LatLng gregorio;
    LatLng sidney;
    LatLng inicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        julian = new LatLng(41.63216342592628, -4.7585783692597206);
        ribera = new LatLng(41.6647974045449, -4.723264553916386);
        gregorio = new LatLng(41.64050385673862, -4.734265730629977);
        sidney = new LatLng(-34, 151);
       // inicial = new LatLng(-34, 151);
        try {
            cargarPreferencias();
        }catch (NumberFormatException e) {
            inicial = julian;
            Toast.makeText(this, "Ubicación por defecto: IES Julián Marías", Toast.LENGTH_LONG).show();
        }


        markerJulian = mMap.addMarker(new MarkerOptions().position(julian).title("IES Julián Marías")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));
        markerRibera = mMap.addMarker(new MarkerOptions().position(ribera).title("IES Ribera de Castilla").draggable(true));
        markerGregorio = mMap.addMarker(new MarkerOptions().position(gregorio).title("IES Gregorio Fernández").draggable(true));
        markerSidney = mMap.addMarker(new MarkerOptions().position(sidney).title("Sydney").draggable(true));

        CircleOptions circleOptions = new CircleOptions().center(julian).radius(100).fillColor(0x8000ffff).strokeColor(0xffff00ff);
        Circle circle = mMap.addCircle(circleOptions);


        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(julian.latitude, julian.longitude),
                        new LatLng(ribera.latitude, ribera.longitude),
                        new LatLng(gregorio.latitude, gregorio.longitude)));


        // mMap.addMarker(new MarkerOptions().position(julian).title("Marker in julian"));
        //cargarPreferencias();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inicial));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setOnMapClickListener(this);

        mMap.setOnMarkerClickListener(this);

        mMap.setOnMarkerDragListener(this);
    }

    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, "has hecho click en: " + latLng.latitude + ", " +
                latLng.longitude, Toast.LENGTH_SHORT).show();
        //marker.setPosition(latLng);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "has hecho click en: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(this, "Has empezado a arrastrar en: " +
                marker.getPosition().latitude + ", " + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.i("MARKER", marker.getPosition().latitude + ", " + marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(this, "Has dejado de arrastrar en: " +
                marker.getPosition().latitude + ", " + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
    }



    //----------------MENÚ------------------------------------

    /**
     * Método que me crea el menú del action bar, inflándolo a partir
     * del archivo xml menu_preferencias.xml
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferencias, menu);
        return true;
    }

    /**
     * Método que responde a los eventos de click de los elementos
     * colocado en el action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // int id = item.getItemId();
        /*switch(id){
            case R.id.sidney:
                guardarPreferencias(item.getItemId());
                return true;
            case R.id.julianMarias:
                guardarPreferencias(item.getItemId());
                return true;
        }*/

        guardarPreferencias(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    //--------------------PREFERENCIAS------------------------------------------

    private void guardarPreferencias(int itemId) {
        //Permite almacenar en un fichero las preferencias
        SharedPreferences preferences = getSharedPreferences("ubicacion", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        if(itemId == R.id.julianMarias) {
            editor.putString("latitud", String.valueOf(julian.latitude));
            editor.putString("longitud", String.valueOf(julian.longitude));
        }

        if(itemId == R.id.sidney) {
            editor.putString("latitud", String.valueOf(sidney.latitude));
            editor.putString("longitud", String.valueOf(sidney.longitude));
        }

        editor.commit();
    }

    private void cargarPreferencias() throws NumberFormatException{
        SharedPreferences preferences = getSharedPreferences("ubicacion", Context.MODE_PRIVATE);

        String latitud = preferences.getString("latitud","");
        String longitud = preferences.getString("longitud","");

        inicial = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
    }
}
























