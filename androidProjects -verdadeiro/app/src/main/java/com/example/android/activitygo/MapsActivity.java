package com.example.android.activitygo;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    private ArrayList<Double> posicoes = new ArrayList<>();
    private float accKm = 0;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private Fragment SelectedFragment;
    private boolean isStopped = false;

    private Button finalizar;

    private ArrayList<LatLng> arrayMarkers = new ArrayList<LatLng>();
    private DatabaseReference databaseCorrida;
    private DatabaseReference databaseUsers;

    private boolean atingiu = false;
    private boolean atingiuTwo = false;
    private boolean atingiuFive = false;
    private String chronoTime;
    private String chronoTimeTwo;
    private String chronoTimeFive;
    private Date date;

    private String firstName = "";
    private String lastName = "";
    private String pontos = "";
    private Toolbar toolbarCima;
    private String username2 = "";
    private String image_path = "";
    private TextToSpeech t1;

    private String desporto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final String username = getIntent().getExtras().getString("USERNAME");
        username2 = username;
        image_path = getIntent().getExtras().getString("URI");
        //desporto = getIntent().getExtras().getString("Desporto");



        databaseCorrida = FirebaseDatabase.getInstance().getReference("corrida");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        databaseUsers.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    firstName = String.valueOf(child.child("firstName").getValue());
                    lastName = String.valueOf(child.child("lastName").getValue());
                    pontos = String.valueOf(child.child("pontos").getValue());
                    toolbarCima = (Toolbar) findViewById(R.id.toolbarMaps);
                    setSupportActionBar(toolbarCima);
                    getSupportActionBar().setTitle("ActivityGO");

                    getSupportActionBar().setSubtitle("" + firstName.charAt(0) + lastName.charAt(0) + ":" + " " + pontos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        locationListener = new LocationListener() {
            private long pauseOff;

            @Override
            public void onLocationChanged(Location location) {
                if (isStopped == false) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double altitude = location.getAltitude();
                    posicoes.add(latitude);
                    posicoes.add(longitude);
                    // posicoes.add(altitude);

                   /* if (posicoes.size() == 6) {
                        accKm += greatCircleInKilometers(posicoes.get(0), posicoes.get(3), posicoes.get(1), posicoes.get(4), posicoes.get(2), posicoes.get(5));
                        posicoes.remove(0);
                        posicoes.remove(1);
                        posicoes.remove(2);
                    }
*/
                    if (posicoes.size() == 4) {
                        Location location1 = new Location("");
                        location1.setLatitude(posicoes.get(0));
                        location1.setLongitude(posicoes.get(1));

                        Location location2 = new Location("");
                        location2.setLatitude(posicoes.get(2));
                        location2.setLongitude(posicoes.get(3));
                        //accKm += location1.distanceTo(location2);
                        //accKm += distance2(posicoes.get(0), posicoes.get(1), posicoes.get(2), posicoes.get(3));
                        float[] array = new float[5];
                        Location.distanceBetween(posicoes.get(0), posicoes.get(1), posicoes.get(2), posicoes.get(3), array);
                        accKm += array[0];

                        Log.d("ARRAY4", printArray(posicoes));
                        Log.d("ACCKM", "" + accKm);

                        posicoes.remove(0);
                        posicoes.remove(0);

                        pauseOff = SystemClock.elapsedRealtime() - chronometer.getBase();


                        if (accKm > 999 && atingiu == false) {
                            double timeSconverted = Double.valueOf((int) pauseOff / 1000);
                            double time = timeSconverted / 60;
                            int timeInteiro = (int) time;
                            double minutos = time - timeInteiro;
                            double segundos = minutos * 60;

                            if (timeInteiro < 10) {
                                if (segundos < 10) {
                                    chronoTime = "0" + timeInteiro + ":" + "0" + (int) segundos;
                                } else {
                                    chronoTime = "0" + timeInteiro + ":" + (int) segundos;
                                }
                            } else {
                                chronoTime = "" + timeInteiro + ":" + (int) segundos;
                            }
                            atingiu = true;
                        }


                        if (accKm > 1999 && atingiuTwo == false) {
                            double timeSconverted = Double.valueOf((int) pauseOff / 1000);
                            double time = timeSconverted / 60;
                            int timeInteiro = (int) time;
                            double minutos = time - timeInteiro;
                            double segundos = minutos * 60;

                            if (timeInteiro < 10) {
                                if (segundos < 10) {
                                    chronoTimeTwo = "0" + timeInteiro + ":" + "0" + (int) segundos;
                                } else {
                                    chronoTimeTwo = "0" + timeInteiro + ":" + (int) segundos;
                                }
                            } else {
                                chronoTimeTwo = "" + timeInteiro + ":" + (int) segundos;
                            }
                            atingiuTwo = true;
                        }




                        if (accKm > 4999 && atingiuFive == false) {
                            double timeSconverted = Double.valueOf((int) pauseOff / 1000);
                            double time = timeSconverted / 60;
                            int timeInteiro = (int) time;
                            double minutos = time - timeInteiro;
                            double segundos = minutos * 60;

                            if (timeInteiro < 10) {
                                if (segundos < 10) {
                                    chronoTimeFive = "0" + timeInteiro + ":" + "0" + (int) segundos;
                                } else {
                                    chronoTimeFive = "0" + timeInteiro + ":" + (int) segundos;
                                }
                            } else {
                                chronoTimeTwo = "" + timeInteiro + ":" + (int) segundos;
                            }
                            atingiuFive = true;
                        }







                        //   posicoes.remove(0);
                    }
                    Log.d("ARRAY", printArray(posicoes));

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();
                        LatLng latLng = new LatLng(latitude, longitude);
                        arrayMarkers.add(latLng);
                        //marker = mMap.addMarker(new MarkerOptions().position(latLng).title("acc= " + Double.toString(accKm)));
                        // mMap.setMaxZoomPreference(20);
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                        //Log.d("COORDENADAS", latitude + " " + longitude + " " + altitude);

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        TextView tv = (TextView) findViewById(R.id.kmTextViewRun);
                        tv.setText("" + df.format(accKm));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 1, locationListener);

        final Button Stop = (Button) findViewById(R.id.StopRun);
        final Button Start = (Button) findViewById(R.id.StartRun);
        chronometer = findViewById(R.id.chronometer);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });




        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                    isStopped = false;
                    t1.speak("Begining workout", TextToSpeech.QUEUE_FLUSH, null);


                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (running) {
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    chronometer.stop();
                    running = false;
                    isStopped = true;
                    t1.speak("Stopping workout", TextToSpeech.QUEUE_FLUSH, null);


                }

                /*SelectedFragment = new RunFragment();
                FragmentManager fmana = getFragmentManager();
                FragmentTransaction ftransacti = fmana.beginTransaction();
                ftransacti.replace(R.id.fragment_container, SelectedFragment,"StartCorridaFragment");
                ftransacti.addToBackStack("IrCorridaFragment");
                ftransacti.commit();*/
            }
        });

        finalizar = (Button) findViewById(R.id.Finalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopped == true) {
                    if (accKm > 0 ) {
                        Toast.makeText(MapsActivity.this, "Terminou a corrida!", Toast.LENGTH_SHORT).show();

                        Fragment p = new HistoriaStatus();
                        Bundle args = new Bundle();
                        args.putParcelableArrayList("Markers", arrayMarkers);
                        args.putString("DATAS", dateFormat.format(date));
                        args.putLong("TEMPOPACE", (int) pauseOffset / 1000);
                        args.putString("TEMPO", "" + (int) pauseOffset / 1000);
                        args.putDouble("DISTANCIA", accKm);
                        args.putString("USERNAME", username);
                        args.putString("MELHORKM", chronoTime);
                        args.putString("MELHORSEGUNDOKM", chronoTimeTwo);
                        args.putString("MELHORQUINTOKM", chronoTimeFive);
                        p.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.fragmentMap, p).commit();
                        Start.setVisibility(View.GONE);
                        Stop.setVisibility(View.GONE);
                        finalizar.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(MapsActivity.this, "Não pode terminar uma corrida sem percorrer distancia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Tem que carregar primeiro que colocar a actividade em pausa", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = sin(latDistance / 2) * sin(latDistance / 2)
                + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
                * sin(lonDistance / 2) * sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

    public double FlatDistance(double lat1, double lat2, double lon1, double lon2) {

        double latdif = Math.pow(Math.toRadians(lat2 - lat1), 2);
        double longdif = Math.pow(Math.toRadians(lon2 - lon1), 2);
        return Math.sqrt(latdif + longdif);
    }

    static double PI_RAD = Math.PI / 180.0;

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in kilometers.
     * https://software.intel.com/en-us/blogs/2012/11/25/calculating-geographic-distances-in-location-aware-apps
     */
    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }

    private static double distance2(double lat1, double lat2, double lon1, double lon2) {
        double theta = lon1 - lon2;
        double dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta));
        dist = acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public String printArray(ArrayList<Double> coordenadas) {
        String printArray = "";
        for (Double coordenada : coordenadas) {
            printArray += coordenada.toString() + " ";
        }
        return printArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    private void goToSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        i.putExtra("USERNAME", username2);
        i.putExtra("URI", image_path);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                goToSettings();
                break;
        }
        return true;
    }
}