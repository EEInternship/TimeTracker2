package eeinternship.com.timetracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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