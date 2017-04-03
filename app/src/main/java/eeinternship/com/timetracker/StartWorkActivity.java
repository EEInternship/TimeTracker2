package eeinternship.com.timetracker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;

import Data.Project;
import Data.Ticket;
import Data.UserData;

public class StartWorkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton buttonNewTicket;

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private ArrayList<Project> projectArrayList;
    private PopupMenu popupMenu;

    String[] name, hour;

    ArrayList<Ticket> ticketList = new ArrayList<Ticket>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();
        projectArrayList = userData.getProjectList();
        ticketList = userData.getTicketList();





        buttonNewTicket = (FloatingActionButton) findViewById(R.id.btn_add_ticket);

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_start_work);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setAdapter();



        buttonNewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 popupMenu = new PopupMenu(StartWorkActivity.this, buttonNewTicket);

                for(Project row : projectArrayList){
                    popupMenu.getMenu().add(row.projectName);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ticketList.add(new Ticket("00:00",menuItem.getTitle().toString()));
                        userData.setTicketList(ticketList);
                        applicationTimeTracker.setUserData(userData);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });



    }
    private void setAdapter(){

        adapter = new StartWorkAdapter(ticketList);
        recyclerView.setAdapter(adapter);
    }
}
