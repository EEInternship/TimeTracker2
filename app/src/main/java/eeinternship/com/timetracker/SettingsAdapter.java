package eeinternship.com.timetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;

import Data.Project;
import Data.Ticket;
import Data.UserData;

/**
 * Created by developer on 8.4.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.RecyclerViewHolder> {
    private ArrayList<Project> listProjects;
    static Context context;
    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;

    private int currentBackgroundColor = 0xffffffff;

    public SettingsAdapter(Context ctx, ArrayList<Project> data) {
        this.context = ctx;
        this.listProjects = data;
        applicationTimeTracker = (ApplicationTimeTracker) ((SettingsActivity) ctx).getApplication();
        userData = applicationTimeTracker.getUserData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_settings, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final Project selectedProject = listProjects.get(position);
        if (selectedProject.getTicketColor() != null)
            holder.color.setBackgroundColor((Color.parseColor(selectedProject.getTicketColor())));
        holder.bindProject(listProjects.get(position));
        holder.color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(context)
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton("Save", new ColorPickerClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
                                holder.color.setBackgroundColor(Color.parseColor(hexColor));
                                // holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));

                                selectedProject.setTicketColor(hexColor);
                                userData.addProjectList(new ArrayList<Project>());
                                userData.addProjectList(listProjects);
                                ArrayList<Ticket> tickets = userData.getTicketList();
                                if (tickets != null) {
                                    for (Ticket ticket : tickets) {
                                        for (Project project : userData.getProjectList()) {
                                            if (ticket.getProject().equals(project.projectName))
                                                ticket.setColor(project.getTicketColor());
                                        }
                                    }
                                }
                                userData.setTicketList(tickets);
                                applicationTimeTracker.setUserData(userData);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .noSliders()
                        .build()
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listProjects.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton color;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.project_name_settings);
            color = (ImageButton) itemView.findViewById(R.id.color_project);
        }

        public void bindProject(Project data) {
            this.name.setText(data.projectName);
        }
    }

    public void swap(int firstPosition, int secondPosition) {
        Collections.swap(listProjects, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
        userData.addProjectList(listProjects);
        applicationTimeTracker.setUserData(userData);
    }
}