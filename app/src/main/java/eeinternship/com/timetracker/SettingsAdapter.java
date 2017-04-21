package eeinternship.com.timetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Collections;
import java.util.List;

import Data.ProjectsInSettings;

/**
 * Created by developer on 8.4.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.RecyclerViewHolder> {
    List<ProjectsInSettings> listProjects;
    static Context context;
    private View root;

    private int currentBackgroundColor = 0xffffffff;

    public SettingsAdapter(Context ctx, List<ProjectsInSettings> data) {
        this.context = ctx;
        this.listProjects = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_settings, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
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
                                ToastSporocilo("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("Save", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        root.setBackgroundColor(selectedColor);
    }

    private void ToastSporocilo(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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
            if(Build.VERSION.SDK_INT>21){
                color.setBackground(context.getDrawable(R.drawable.btn_circle_color));
               // change color
                color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C0C0C0")));
            }
        }

        public void bindProject(ProjectsInSettings data) {
            this.name.setText(data.getName());
        }
    }

    public void swap(int firstPosition, int secondPosition) {
        Collections.swap(listProjects, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
}