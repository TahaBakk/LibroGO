package com.example.x3727349s.librogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import org.osmdroid.bonuspack.overlays.Marker;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay mCompassOverlay;
    private IMapController mapController;
    private static final int ACTIVITAT_SELECCIONAR_IMATGE = 1;
    //private StorageReference mStorageRef;
    private DatabaseReference dbRef;
    private static double longitude;
    private static double latitude;
    private static String rutaFoto;
    private ArrayList<Pojo> pos = new ArrayList<>();
    private RadiusMarkerClusterer markers;



    public MainActivityFragment() {
    }

    @Override//notificamos al activity quer le añadimos items al menu
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //mStorageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Photo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        map = (MapView) view.findViewById(R.id.mapView);

        initializeMap();
        setZoom();
        setOverlays();
        putMarkers();



        return view;
    }
    //INICIALIZAR MAPA Y CONF
    private void initializeMap() {
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setTilesScaledToDpi(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
    }

    private void setZoom() {
        //  Setteamos el zoom al mismo nivel y ajustamos la posición a un geopunto
        mapController = map.getController();
        mapController.setZoom(14);
    }

    private void setOverlays() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();

        myLocationOverlay = new MyLocationNewOverlay(getContext(),new GpsMyLocationProvider(getContext()),map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo( myLocationOverlay.getMyLocation());
            }
        });

        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        mCompassOverlay = new CompassOverlay(getContext(),new InternalCompassOrientationProvider(getContext()),map);
        mCompassOverlay.enableCompass();

        map.getOverlays().add(myLocationOverlay);
        map.getOverlays().add(this.mScaleBarOverlay);
        map.getOverlays().add(this.mCompassOverlay);
    }


    public void putMarkers(){

        pos.clear();
        setupMarkerOverlay();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String datosSnap = snapshot.getValue().toString();

                    String[] parts = datosSnap.split(",");
                    String[] pathFoto = parts[0].split("=");
                    String[] lon = parts[1].split("=");
                    String[] lat = parts[2].split("=");//quitarle el ultimo caracter
                    //casteamos
                    String latSin = lat[1].substring(0, lat[1].length()-1);
                    double lati = Double.parseDouble(lon[1]);
                    double loni = Double.parseDouble(latSin);

                    Pojo pj = new Pojo(loni, lati, pathFoto[1]);
                    //Pojo pj = snapshot.getValue(Pojo.class);//loc
                    System.out.println("vaaaaaaaaaaaaa"+ pj.toString());
                    pos.add(pj);//locationsList

                    Marker marker = new Marker(map);

                    GeoPoint point = new GeoPoint(
                            pj.getLatitude(),
                            pj.getLongitude()
                    );
                    marker.setPosition(point);
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    marker.setIcon(getResources().getDrawable(R.drawable.iconlibro));
                    marker.setTitle(pj.getRutaFoto());
                    marker.setAlpha(0.6f);
                    System.out.println("Vaaaaaaaaaaaaaa?"+ pj.getLatitude() );
                    markers.add(marker);
                }
                markers.invalidate();
                map.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupMarkerOverlay() {
        markers = new RadiusMarkerClusterer(getContext());
        map.getOverlays().add(markers);

        Drawable clusterIconD = getResources().getDrawable(R.drawable.iconlibro);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();

        markers.setIcon(clusterIcon);
        markers.setRadius(100);

    }


   @Override//añadimos items al menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menumapa, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fotoMenu) {
            dispatchTakePictureIntent();
            putMarkers();
            return true;

        }else if (id == R.id.mapaMenu){
            Intent i = new Intent(getContext(), MainActivityFragment.class);
            startActivity(i);
            putMarkers();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        //Se crea el nombre del fitxero
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Guardar un archivo: ruta de acceso con ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        //Pojo.setRutaFoto(image.getAbsolutePath());
        rutaFoto = image.getAbsolutePath();
        return image;
    }


    static final int REQUEST_TAKE_PHOTO = 1;
    //Hacer foto y pillar ruta
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("Error al crear la imagen = "+ex);
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        getLocation();
        subirDatosFirebase();

    }
    //GPS
    public void getLocation()
    {
        //Coje la localizacion
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //Pojo.setLongitude(location.getLongitude());
            //Pojo.setLatitude(location.getLatitude());
            System.out.println("DEBUG****-->LAT ==>"+location.getLongitude()+" LON ==> "+location.getLatitude());
            
            //return new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (NullPointerException e){
            e.printStackTrace();
           // return null;
        }
    }

    public void subirDatosFirebase(){

        Pojo pojo = new Pojo(longitude, latitude, rutaFoto);
        dbRef.push().setValue(pojo);

    }


    //esto es para llamar la galeria bueno para seleccionar opcion si se tiene 2 o mas
        /*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, ACTIVITAT_SELECCIONAR_IMATGE);*/


}
