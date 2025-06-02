package com.example.tamilnadubustracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class Insert_data extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);
        EditText edit_dist_name,edit_bus_name,edit_bus_stop,edit_all_bus_stop,edit_bus_detail_name,edit_us_detail_type,edit_lat,edit_lon;
        Button btn_save_bus_stop,btn_save_all_stop,btn_save_bus_detials,btn_map;
        edit_dist_name =  findViewById(R.id.edit_dist_name);
        edit_bus_name =  findViewById(R.id.edit_bus_name);
        edit_bus_stop =  findViewById(R.id.edit_bus_sto);
        edit_bus_detail_name = findViewById(R.id.edit_bus_detial_name2);
        edit_us_detail_type = findViewById(R.id.edit_bus_detial_type);
        edit_lat = findViewById(R.id.edit_latitude);
        edit_lon = findViewById(R.id.edit_longitude);

        edit_all_bus_stop =  findViewById(R.id.edit_stop_name);

        btn_save_bus_stop =  findViewById(R.id.save_bus_stop);
        btn_save_all_stop = findViewById(R.id.btn_save_all_sto);
        btn_save_bus_detials = findViewById(R.id.btn_save_bus_detials);
        btn_map = findViewById(R.id.location_btn);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("District");


        btn_save_bus_detials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String str_bus_detial_name = edit_bus_detail_name.getText().toString();
                    String str_bus_detial_type = edit_us_detail_type.getText().toString();
                    HashMap hash_bus_details = new HashMap();
                    hash_bus_details.put("Bus_name",str_bus_detial_name);
                    hash_bus_details.put("Bus_type",str_bus_detial_type);
                    String key= databaseReference.push().getKey();
                    databaseReference.child("Erode").child("Bus").child(key).setValue(hash_bus_details);
                    String s = "0.0";
                    HashMap hash_lat_lon = new HashMap();
                    hash_lat_lon.put("lat",s);
                    hash_lat_lon.put("lon",s);
                    databaseReference.child("Erode").child("Bus").child(key).child("Location").setValue(hash_lat_lon);
                    HashMap hash_status = new HashMap();
                    hash_status.put("real_status",s);
                    hash_status.put("check_status",s);
                    databaseReference.child("Erode").child("Bus").child(key).child("status").setValue(hash_status);


            }
        });

        btn_save_bus_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_bus_name= String.valueOf(edit_bus_name.getText());
                String str_stop_name= String.valueOf(edit_bus_stop.getText());
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("stop_name" ,str_stop_name);
                hashMap.put("bus_name",str_bus_name);
                databaseReference.child("Erode").child("Search").push().setValue(hashMap);
            }
        });

        btn_save_all_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap hashMap_all_stop = new HashMap();
                String str_all_stop= String.valueOf(edit_all_bus_stop.getText());
                String str_lat= String.valueOf(edit_lat.getText());
                String str_lon= String.valueOf(edit_lon.getText());
                hashMap_all_stop.put("stop",str_all_stop);
                hashMap_all_stop.put("lat",str_lat);
                hashMap_all_stop.put("lon",str_lon);
                databaseReference.child("Erode").child("Bus_stop").push().setValue(hashMap_all_stop);

            }
        });



    }
}