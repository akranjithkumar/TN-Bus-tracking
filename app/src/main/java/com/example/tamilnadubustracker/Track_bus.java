package com.example.tamilnadubustracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Track_bus extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    List array_stop_name = new ArrayList<>();
    ListView listView;
    Switch aswitch;
    String get_key;
    Track_adapter track_adapter;
    private final static int REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView text_bus_name, track_bus_head,text_display_status;
    private LocationCallback locationCallback;
    ImageView btn_relad;

    double latitude = 11.2809891;
    double longitude = 77.6788417;

    String st_from;
    String st_to;



    String get_check_status = " ";

    String status = " ";
    String status_one = " ";


    TextToSpeech speech;
    List list_lat = new ArrayList();
    List list_lon = new ArrayList();
    String str_bus_name;
    boolean location_check = false;

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("Search").orderByChild("bus_name").equalTo(str_bus_name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String str_stop_name = snapshot.child("stop_name").getValue().toString();
                        if (!array_stop_name.contains(str_stop_name)) {
                            array_stop_name.add(str_stop_name);
                            databaseReference.child("Bus_stop").orderByChild("stop").equalTo(str_stop_name).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            String str_lat = snapshot.child("lat").getValue().toString();
                                            String str_lon = snapshot.child("lon").getValue().toString();
                                            if (!list_lat.contains(str_lat)) {
                                                list_lat.add(str_lat.toString());
                                                list_lon.add(str_lon.toString());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                    track_adapter = new Track_adapter(array_stop_name, "", 5, getApplicationContext());
                    listView.setAdapter(track_adapter);
                    st_from = array_stop_name.get(0).toString();
                    st_to = array_stop_name.get(array_stop_name.size() - 1).toString();
                    text_bus_name.setText(st_from + " - " + st_to);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_bus);

        track_bus_head = findViewById(R.id.text_track_bus);
        text_display_status = findViewById(R.id.text_display_status);
        btn_relad = findViewById(R.id.btn_reload);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        aswitch = findViewById(R.id.switch3);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        database = FirebaseDatabase.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = database.getReference().child("District").child("Erode");


        Intent intent = getIntent();
        str_bus_name = intent.getStringExtra("bus_name");
        text_bus_name = findViewById(R.id.track_stop_name);
        listView = findViewById(R.id.track_list_view);

        track_bus_head.setText(str_bus_name);

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });



        btn_relad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speech.speak(str_bus_name + " " + text_display_status.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        if(!(str_bus_name == null)){
            databaseReference.child("Bus").orderByChild("Bus_name").equalTo(str_bus_name).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    get_key = snapshot.getKey().toString();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    location_check = true;

                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            if (locationResult != null) {
                                Location location = locationResult.getLastLocation();
                                updateTextview(location);
                            }
                        }

                    };

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        startLocationUpdates();
                    } else {
                        ActivityCompat.requestPermissions(Track_bus.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
                else {
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        });


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!(get_key == null)){
                    databaseReference.child("Bus").child(get_key).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().exists()){
                                    DataSnapshot snapshot = task.getResult();
                                    String check_status =snapshot.child("check_status").getValue().toString();
                                    String real_status = snapshot.child("real_status").getValue().toString();
                                    if(!(get_check_status.equals(check_status))){
                                        get_check_status = check_status;
                                        if(check_status.equals("in")){
                                            int i = array_stop_name.indexOf(real_status);
                                            text_display_status.setText(" Reached " + real_status);
                                            speech.speak(str_bus_name + " Reached " + real_status, TextToSpeech.QUEUE_FLUSH, null);
                                            track_adapter = new Track_adapter(array_stop_name, array_stop_name.get(i).toString(), 1, getApplicationContext());
                                            listView.setAdapter(track_adapter);
                                            listView.setSelection(i);
                                            listView.smoothScrollToPosition(i);
                                        }
                                        else if(check_status.equals("out")){
                                            int i = array_stop_name.indexOf(real_status);
                                            text_display_status.setText(" Left " + real_status);
                                            speech.speak(str_bus_name + " Left " + real_status, TextToSpeech.QUEUE_FLUSH, null);
                                            track_adapter = new Track_adapter(array_stop_name, array_stop_name.get(i).toString(), 0, getApplicationContext());
                                            listView.setAdapter(track_adapter);
                                            listView.setSelection(i);
                                            listView.smoothScrollToPosition(i);
                                        }
                                    }
                                }
                            }
                        }
                    });

                }
                handler.postDelayed(this,200);
            }
        };
        runnable.run();

    }


    private void updateTextview(Location location) {
        if (location != null) {
            String text = "Latitude: " + location.getLatitude() +
                    ", Longitude: " + location.getLongitude();

            if(!(get_key == null)){

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("lat" ,location.getLatitude());
                hashMap.put("lon",location.getLongitude());
                databaseReference.child("Bus").child(get_key).child("Location").setValue(hashMap);

                int pos = 0;
                //Toast.makeText(this, list_lat.toString(), Toast.LENGTH_SHORT).show();
                while (pos<array_stop_name.size()){

                    double db_lat = Double.parseDouble(list_lat.get(pos).toString());
                    double db_lon = Double.parseDouble(list_lon.get(pos).toString());

                    double get_lat = location.getLatitude();
                    double get_lon = location.getLongitude();

                    double degree = 0.0008983111749 ;
                    double finalpluslon = (db_lon + degree);
                    double finalminuslon = (db_lon - degree);
                    double finalplus_lat = (db_lat + degree);
                    double finalminus_lat = (db_lat - degree);


                    if (((get_lat <= finalplus_lat && get_lat >= finalminus_lat) && (get_lon <= finalpluslon && get_lon >= finalminuslon))) {
                        if ((!status_one.equals(array_stop_name.get(pos).toString()))){
                            status_one = array_stop_name.get(pos).toString();
                            HashMap<String,Object> hashMap_status = new HashMap<>();
                            hashMap_status.put("real_status" ,array_stop_name.get(pos).toString());
                            hashMap_status.put("check_status","in");
                            //speech.speak(str_bus_name + " not yet started from " + array_stop_name.get(pos).toString(), TextToSpeech.QUEUE_FLUSH, null);
                            databaseReference.child("Bus").child(get_key).child("status").setValue(hashMap_status);

                        }
                    }
                    else {
                        if(status_one.equals(array_stop_name.get(pos).toString())) {
                            //speech.speak(str_bus_name + " ghj " + array_stop_name.get(pos).toString(), TextToSpeech.QUEUE_FLUSH, null);
                            HashMap<String,Object> hashMap_status = new HashMap<>();
                            hashMap_status.put("real_status" ,array_stop_name.get(pos).toString());
                            hashMap_status.put("check_status","out");
                            //speech.speak(str_bus_name + " not yet started from " + array_stop_name.get(pos).toString(), TextToSpeech.QUEUE_FLUSH, null);
                            databaseReference.child("Bus").child(get_key).child("status").setValue(hashMap_status);
                        }

                    }
                    pos++;
                }
5

            }

        }
    }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 1) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
            }
        }

        private void startLocationUpdates () {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
                locationRequest.setInterval(200);
                locationRequest.setFastestInterval(200);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }

    @Override
    protected void onStop() {
        super.onStop();
        if(location_check == true){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

    }
}
