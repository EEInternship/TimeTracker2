package eeinternship.com.timetracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Data.ProjectsInSettings;

public class SettingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    RecyclerView.LayoutManager layoutManager;

    TextView nameOfProject;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROJECTS");

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_settings);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SettingsAdapter projectsA=new SettingsAdapter(this,getData());
        recyclerView.setAdapter(projectsA);

        ItemTouchHelper.Callback callback = new ProjectTouchHelper(projectsA);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<ProjectsInSettings> getData(){
        List<ProjectsInSettings> movieList = new ArrayList<>();
        movieList.add(new ProjectsInSettings("Harry Potter"));
        movieList.add(new ProjectsInSettings("Twilight"));
        movieList.add(new ProjectsInSettings("Star Wars"));
        movieList.add(new ProjectsInSettings("Star Trek"));
        movieList.add(new ProjectsInSettings("Galaxy Quest"));
        return movieList;
    }
}
