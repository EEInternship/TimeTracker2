package eeinternship.com.timetracker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

import Data.Project;
import Data.Ticket;
import Data.UserData;

public class StartWorkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    FloatingActionButton buttonOptions, buttonFinishWork, buttonFirstProject, buttonSecondProject, buttonThirdProject, buttonSelectProject;
    TextView labelBtnFirstProject, labelBtnSecondProject, labelBtnThirdProject, labelSelectProject, labelFinishWork;

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private ArrayList<Project> projectArrayList;
    private PopupMenu popupMenu;

    // animation
    Animation fabOpen, fabClose, fabRotate, fabRotateClose, txtOpen, txtClose;
    boolean isOpen = false;

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

        buttonOptions = (FloatingActionButton) findViewById(R.id.btn_options);
        buttonSelectProject = (FloatingActionButton) findViewById(R.id.btn_select_project);
        buttonFinishWork = (FloatingActionButton) findViewById(R.id.btn_finish_work);
        buttonFirstProject = (FloatingActionButton) findViewById(R.id.btn_first_project);
        buttonSecondProject = (FloatingActionButton) findViewById(R.id.btn_second_project);
        buttonThirdProject=(FloatingActionButton)findViewById(R.id.btn_third_project);


        labelSelectProject = (TextView) findViewById(R.id.label_select_project);
        labelFinishWork = (TextView) findViewById(R.id.label_finish_work);
        labelBtnFirstProject = (TextView) findViewById(R.id.name_project_one);
        labelBtnSecondProject=(TextView)findViewById(R.id.name_project_two);
        labelBtnThirdProject=(TextView)findViewById(R.id.name_project_three);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_btn_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_btn_close);

        fabRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation_fab_btn);
        fabRotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation_fab_btn_back);

        txtOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_open);
        txtClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_close);

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*popupMenu = new PopupMenu(StartWorkActivity.this, buttonNewTicket);

                for (Project row : projectArrayList) {
                    popupMenu.getMenu().add(row.projectName);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ticketList.add(new Ticket("00:00", menuItem.getTitle().toString()));
                        setAdapter();
                        userData.setTicketList(ticketList);
                        applicationTimeTracker.setUserData(userData);

                        return true;
                    }
                });
                popupMenu.show();*/

                if (isOpen) {
                   /* buttonFirstProject.startAnimation(fabClose);
                    labelBtnFirstProject.startAnimation(fabClose);
                    buttonFirstProject.setClickable(true);
*/
                    buttonOptions.startAnimation(fabRotateClose);

                    buttonSelectProject.startAnimation(fabClose);
                    buttonSelectProject.setClickable(false);
                    labelSelectProject.startAnimation(txtClose);

                    buttonFinishWork.startAnimation(fabClose);
                    buttonFinishWork.setClickable(false);
                    labelFinishWork.startAnimation(txtClose);

                    buttonFirstProject.startAnimation(fabClose);
                    buttonFirstProject.setClickable(false);
                    labelBtnFirstProject.startAnimation(txtClose);

                    buttonSecondProject.startAnimation(fabClose);
                    buttonSecondProject.setClickable(false);
                    labelBtnSecondProject.startAnimation(txtClose);

                    buttonThirdProject.startAnimation(fabClose);
                    buttonThirdProject.setClickable(false);
                    labelBtnThirdProject.startAnimation(txtClose);

                    isOpen = false;
                } else {
                    /*buttonFirstProject.startAnimation(fabOpen);
                    labelBtnFirstProject.startAnimation(txtOpen);
                    buttonFirstProject.setClickable(false);
*/
                    buttonOptions.startAnimation(fabRotate);

                    buttonSelectProject.startAnimation(fabOpen);
                    buttonSelectProject.setClickable(true);
                    labelSelectProject.startAnimation(txtOpen);

                    buttonFinishWork.startAnimation(fabOpen);
                    buttonFinishWork.setClickable(true);
                    labelFinishWork.startAnimation(txtOpen);

                    buttonFirstProject.startAnimation(fabOpen);
                    buttonFirstProject.setClickable(true);
                    labelBtnFirstProject.startAnimation(txtOpen);

                    buttonSecondProject.startAnimation(fabOpen);
                    buttonSecondProject.setClickable(true);
                    labelBtnSecondProject.startAnimation(txtOpen);

                    buttonThirdProject.startAnimation(fabOpen);
                    buttonThirdProject.setClickable(true);
                    labelBtnThirdProject.startAnimation(txtOpen);

                    isOpen = true;
                }

            }
        });

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_start_work);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setAdapter();

        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipeLeft(int position) {
                        return true;
                    }

                    @Override
                    public boolean canSwipeRight(int position) {
                        return false;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            ticketList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                    }
                });


        recyclerView.addOnItemTouchListener(swipeTouchListener);

    }

    private void setAdapter() {
        adapter = new StartWorkAdapter(ticketList);
        recyclerView.setAdapter(adapter);
    }
}

