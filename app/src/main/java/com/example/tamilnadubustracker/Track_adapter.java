package com.example.tamilnadubustracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Track_adapter extends BaseAdapter {

    List array_bus_name ;
    Context context;
    String selected_bus = "";
    int track;

    public Track_adapter(List array_bus_name, String selected_bus , int track, Context context) {
        this.array_bus_name = array_bus_name;
        this.selected_bus = selected_bus;
        this.context = context;
        this.track = track;

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
        view = inflater.inflate(R.layout.track_list,null);
        TextView text_stop_name = (TextView) view.findViewById(R.id.track_stop_name);
        TextView text_bus = (TextView) view.findViewById(R.id.textView19);
        ImageView img_track_line = (ImageView) view.findViewById(R.id.img_track_line);
        //ImageView img_track_line2 = (ImageView) view.findViewById(R.id.img_track_line);
        ImageView img_track_dot = (ImageView) view.findViewById(R.id.track_img_dot);
        ImageView img_track_bus_image = (ImageView) view.findViewById(R.id.track_bus_img_dot);
        ImageView img_track_bus_image_between = (ImageView) view.findViewById(R.id.track_bus_bus_img_between);

        img_track_bus_image.setVisibility(View.INVISIBLE);
        img_track_bus_image.setImageResource(R.drawable.bus);
        img_track_bus_image_between.setVisibility(View.INVISIBLE);
        img_track_bus_image_between.setImageResource(R.drawable.bus);

        if(!(selected_bus == "")){
            if(track == 1 && array_bus_name.get(i).toString() == selected_bus){
                img_track_bus_image.setVisibility(View.VISIBLE);
            }
            if (track == 0 && array_bus_name.get(i).toString() == selected_bus) {
                img_track_bus_image_between.setVisibility(View.VISIBLE);
            }
        }

        img_track_line.setImageResource(R.drawable.track_line);
        //img_track_line2.setImageResource(R.drawable.track_line);
        img_track_dot.setImageResource(R.drawable.baseline_circle_24);
        text_stop_name.setText(array_bus_name.get(i).toString());
        text_stop_name.setTextSize(16);
        text_bus.setTextSize(12);

        if(i == array_bus_name.size()-1){
            img_track_line.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
