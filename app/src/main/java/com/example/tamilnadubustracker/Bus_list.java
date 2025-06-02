package com.example.tamilnadubustracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bus_list extends AppCompatActivity {
FirebaseDatabase database;
String res;
DatabaseReference databaseReference;
List array_all_stop = new ArrayList<>();
List array_hold_bus_from = new ArrayList<>();

List array_select_bus = new ArrayList<>();
List array_select_bus_name = new ArrayList<>();
List array_select_bus_type = new ArrayList<>();
List array_check_search_bus = new ArrayList<>();

HashMap hash_select_bus = new HashMap();

AutoCompleteTextView auto_comp_from,autoCompTo;
String str_atuo_to;
String str_atuo_from ;
ArrayAdapter<String> adapter_total_stop;
Bus_adapter bus_adapter;

ListView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        ImageView img_settings = findViewById(R.id.settings_img);
        TextView text = findViewById(R.id.textView4);
        Button btn_find_bus = findViewById(R.id.btn_find_bus);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_no_result);

        database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = database.getReference().child("District").child("Erode");

        auto_comp_from = findViewById(R.id.autoCompFrom);
        autoCompTo = findViewById(R.id.autoComp_to);
        recyclerView = findViewById(R.id.recycler_view_bus_list);

        bus_adapter = new Bus_adapter(getApplicationContext(),array_select_bus_name,array_select_bus_type);



        adapter_total_stop = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, array_all_stop);
        auto_comp_from.setAdapter(adapter_total_stop);
        autoCompTo.setAdapter(adapter_total_stop);
        auto_comp_from.setThreshold(1);
        autoCompTo.setThreshold(1);




        autoCompTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_atuo_to = autoCompTo.getText().toString();
                search_bus();
            }
        });
        auto_comp_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_atuo_from = auto_comp_from.getText().toString();
                search_bus();
            }
        });


        btn_find_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
                if(array_select_bus.size() == 0)
                {
                   lottieAnimationView.setVisibility(View.VISIBLE);
                   lottieAnimationView.setAnimation(R.raw.no_results);
                   lottieAnimationView.playAnimation();
                   recyclerView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    lottieAnimationView.setAnimation(R.raw.no_results);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    lottieAnimationView.playAnimation();
                    recyclerView.setAdapter(bus_adapter);
                }


            }



        });


        databaseReference.child("Bus_stop").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String str_stop_name = snapshot.child("stop").getValue().toString();
                        if(!array_all_stop.contains(str_stop_name)){
                            array_all_stop.add(str_stop_name);
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



        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_insert_data = new Intent(getApplicationContext(), Insert_data.class);
                startActivity(intent_insert_data);
            }
        });


    }

    private void search_bus() {
        array_hold_bus_from.clear();
        array_select_bus.clear();
        array_select_bus_name.clear();
        array_select_bus_type.clear();
        databaseReference.child("Search").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String str_bus_name = snapshot.child("bus_name").getValue().toString();
                        String str_stop_name = snapshot.child("stop_name").getValue().toString();
                        if(str_stop_name.equals(str_atuo_from)){
                            if(!array_hold_bus_from.contains(str_bus_name)){
                                array_hold_bus_from.add(str_bus_name);

                            }
                        }

                    }
                    databaseReference.child("Search").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot1, @Nullable String previousChildName) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot3 : snapshot1.getChildren()) {
                                    String str_bus_name = snapshot1.child("bus_name").getValue().toString();
                                    String str_stop_name = snapshot1.child("stop_name").getValue().toString();
                                    if(str_stop_name.equals(str_atuo_to)){
                                        if(array_hold_bus_from.contains(str_bus_name) &&  !array_select_bus.contains(str_bus_name)){
                                            array_select_bus.add(str_bus_name);
                                        }
                                    }

                                }
                                //text.setText(array_select_bus.toString());
                                iterate_search();
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

    private void iterate_search() {
        int i = 0;
        array_check_search_bus.clear();
        while (i<array_select_bus.size()){
            databaseReference.child("Bus").orderByChild("Bus_name").equalTo(array_select_bus.get(i).toString()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        hash_select_bus.clear();
                        String str_bus_name = snapshot.child("Bus_name").getValue().toString();
                        String str_bus_type = snapshot.child("Bus_type").getValue().toString();
                        if(!array_check_search_bus.contains(str_bus_name)){
                            array_check_search_bus.add(str_bus_name);
                            array_select_bus_name.add(str_bus_name);
                            array_select_bus_type.add(str_bus_type);
                        }
                        else {

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
            i++;
        }
    }

}

