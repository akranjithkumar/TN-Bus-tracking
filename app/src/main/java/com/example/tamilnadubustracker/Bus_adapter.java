package com.example.tamilnadubustracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class Bus_adapter extends BaseAdapter {
    List array_bus_name,array_bus_type ;
    Context context;

    public Bus_adapter(Context context,List array_bus_name, List array_bus_type) {
        this.array_bus_name = array_bus_name;
        this.array_bus_type = array_bus_type;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array_bus_name.size();
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
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.bus_list,null);

        TextView text_bus_name = (TextView) view.findViewById(R.id.bus_list_bus_name);
        TextView text_bus_type = (TextView) view.findViewById(R.id.bus_list_bus_type);
        ImageView img_bus = (ImageView) view.findViewById(R.id.bus_list_image);
        ImageView img_more = (ImageView) view.findViewById(R.id.bus_list_img_menu);
        ConstraintLayout bus_list_layout = (ConstraintLayout) view.findViewById(R.id.bus_list_layout);

        text_bus_name.setText(array_bus_name.get(i).toString());
        text_bus_name.setTextSize(16);
        text_bus_type.setText(array_bus_type.get(i).toString());

        img_bus.setImageResource(R.drawable.bus_image);
        img_more.setImageResource(R.drawable.more_icon);

        bus_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Track_bus.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bus_name",array_bus_name.get(i).toString());
                intent.putExtra("bus_type",array_bus_type.get(i).toString());
                context.getApplicationContext().startActivity(intent);
            }
        });


        return view;
    }
}

