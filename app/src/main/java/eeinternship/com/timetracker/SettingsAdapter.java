package eeinternship.com.timetracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static eeinternship.com.timetracker.R.id.parent;

/**
 * Created by developer on 8.4.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.RecyclerViewHolder> {
    String[] nameProject;
    Context mContext;

    public SettingsAdapter(String[] nameProject) {
        this.nameProject = nameProject;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_settings, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.name.setText(nameProject[position]);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mContext=v.getContext();
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.settings_dialog);

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameProject.length;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.project_name_settings);
        }
    }

}