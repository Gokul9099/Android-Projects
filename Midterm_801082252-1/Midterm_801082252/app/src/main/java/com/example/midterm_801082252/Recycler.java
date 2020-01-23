package com.example.midterm_801082252;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder>{

    ArrayList<Forecast> sData;

    public Recycler(ArrayList<Forecast> sData) {
        this.sData = sData;
    }

    @NonNull
    @Override
    public Recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler.ViewHolder holder, int position) {
        Forecast s = sData.get(position);
        holder.tv_date.setText(s.dt_txt);
        holder.tv_tempp.setText("Temp: " + s.temp);
        holder.tv_hummmm.setText("Hum:" + s.humidity);
        holder.tv_desccc.setText("Hum:" + s.description);
        Picasso.get().load("http://openweathermap.org/img/wn/"+s.icon+"@2x.png").into(holder.im_immmm);
        holder.song = s;


    }

    @Override
    public int getItemCount() {

//        Log.d( "demossss",sData.size()+"" );
        return sData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        TextView tv_tempp;
        TextView tv_hummmm;
        TextView tv_desccc;
        ImageView im_immmm;
        Forecast song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tempp = itemView.findViewById(R.id.tv_tempp);
            tv_hummmm = itemView.findViewById(R.id.tv_hummmm);
            tv_desccc = itemView.findViewById(R.id.tv_desccc);
            tv_date = itemView.findViewById(R.id.tv_date);
            im_immmm = itemView.findViewById(R.id.im_immmm);

        }
    }
}