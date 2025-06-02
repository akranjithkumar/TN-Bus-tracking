import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Bus_adapter extends RecyclerView.Adapter<Bus_adapter.Bus_view_holder> {

    @NonNull
    @Override
    public Bus_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.Bus_list,parent,false);
        Bus_adapter bus_ad = new Bus_adapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Bus_view_holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Bus_view_holder extends RecyclerView.ViewHolder {
        public TextView text_bus_name;
        public Bus_view_holder(@NonNull View itemView) {
            super(itemView);
            text_bus_name = itemView.findViewById(R.id.bus_list_bus_name);
        }
    }
}
