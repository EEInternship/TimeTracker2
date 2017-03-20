package com.eeinternship.timetracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Data.AdapterClass;

/**
 * Created by IsakFe on 20. 03. 2017.
 */

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.IViewHolder> {
    ArrayList<AdapterClass> adapterClassArrayList =new ArrayList<>();

    public MonthlyAdapter(ArrayList<AdapterClass> adapterClasses) {
        this.adapterClassArrayList = adapterClasses;
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        IViewHolder iViewHolder= new IViewHolder(v);

        return iViewHolder;
    }

    @Override
    public void onBindViewHolder(IViewHolder holder, int position) {
        AdapterClass adapterClass= adapterClassArrayList.get(position); // get each item

        holder.day.setText(adapterClass.getDayOfWeek());
        holder.date.setText(adapterClass.getDate());
        holder.hour.setText(adapterClass.getHours());
    }

    @Override
    public int getItemCount() {
        return adapterClassArrayList.size();
    }

    public static class IViewHolder extends RecyclerView.ViewHolder{

        TextView day,date,hour;
        public IViewHolder(View itemView) {
            super(itemView);
            day= (TextView) itemView.findViewById(R.id.day);
            date= (TextView) itemView.findViewById(R.id.date);
            hour= (TextView) itemView.findViewById(R.id.hours);
        }
    }
}
