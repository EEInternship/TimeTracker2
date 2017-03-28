package com.eeinternship.timetracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Data.testClass;

/**
 * Created by IsakFe on 27. 03. 2017.
 */

public class StartWorkAdapter extends RecyclerView.Adapter<StartWorkAdapter.IViewHolder> {
    ArrayList<testClass>adapter=new ArrayList<>();

    public StartWorkAdapter(ArrayList<testClass>adapterC){
        this.adapter=adapterC;
    }

    @Override
    public StartWorkAdapter.IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_start_work,parent,false);
        IViewHolder iViewHolder=new IViewHolder(v);

        return iViewHolder;
    }

    @Override
    public void onBindViewHolder(StartWorkAdapter.IViewHolder holder, int position) {
        testClass aC=adapter.get(position);
        holder.timeWork.setText(aC.getHour());
        holder.projectName.setText(aC.getProject());
    }

    @Override
    public int getItemCount() {
        return adapter.size();
    }

    public class IViewHolder extends RecyclerView.ViewHolder {
        TextView projectName,timeWork;
        public IViewHolder(View itemView) {
            super(itemView);
            projectName=(TextView)itemView.findViewById(R.id.name_project);
            timeWork=(TextView)itemView.findViewById(R.id.time_work);
        }
    }
}