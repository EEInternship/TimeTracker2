package eeinternship.com.timetracker;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import Data.Project;
import Data.Ticket;
import Data.UploadSpreadsheetData;
import Data.UserData;

public class StartWorkActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FloatingActionButton buttonOptions, buttonFinishWork, buttonFirstProject, buttonSecondProject, buttonThirdProject, buttonSelectProject;
    TextView labelBtnFirstProject, labelBtnSecondProject, labelBtnThirdProject, labelSelectProject, labelFinishWork;

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private newAdapter mAdapter;

    private ArrayList<Project> featuredProjects;

    // dim
    FrameLayout frameLayoutDim;

    // animation
    Animation fabOpen, fabClose, fabRotate, fabRotateClose, txtOpen, txtClose;
    boolean isOpen = false;

    ArrayList<Ticket> ticketList = new ArrayList<Ticket>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TIME TRACKER");

        frameLayoutDim = (FrameLayout) findViewById(R.id.frame_layout_dim);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();
        ticketList = userData.getTicketList();


        buttonOptions = (FloatingActionButton) findViewById(R.id.btn_options);
        buttonSelectProject = (FloatingActionButton) findViewById(R.id.btn_select_project);
        buttonFinishWork = (FloatingActionButton) findViewById(R.id.btn_finish_work);
        buttonFirstProject = (FloatingActionButton) findViewById(R.id.btn_first_project);
        buttonSecondProject = (FloatingActionButton) findViewById(R.id.btn_second_project);
        buttonThirdProject = (FloatingActionButton) findViewById(R.id.btn_third_project);

        labelSelectProject = (TextView) findViewById(R.id.label_select_project);
        labelFinishWork = (TextView) findViewById(R.id.label_finish_work);
        labelBtnFirstProject = (TextView) findViewById(R.id.name_project_one);
        labelBtnSecondProject = (TextView) findViewById(R.id.name_project_two);
        labelBtnThirdProject = (TextView) findViewById(R.id.name_project_three);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_btn_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_btn_close);

        fabRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation_fab_btn);
        fabRotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation_fab_btn_back);

        txtOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_open);
        txtClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_close);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_start_work);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new newAdapter(this, ticketList);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        (mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
        if (userData.getProjectList() != null) {


            if (userData.getProjectList().size() < 1) {
                buttonFirstProject.setVisibility(View.GONE);
                labelBtnFirstProject.setVisibility(View.GONE);
            } else {
                labelBtnFirstProject.setText(userData.getProjectList().get(0).projectName);
                buttonFirstProject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(userData.getProjectList().get(0).getTicketColor())));
            }

            if (userData.getProjectList().size() < 2) {
                buttonSecondProject.setVisibility(View.GONE);
                labelBtnSecondProject.setVisibility(View.GONE);
            } else {
                labelBtnSecondProject.setText(userData.getProjectList().get(1).projectName);
                buttonSecondProject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(userData.getProjectList().get(1).getTicketColor())));
            }

            if (userData.getProjectList().size() < 3) {
                buttonThirdProject.setVisibility(View.GONE);
                labelBtnThirdProject.setVisibility(View.GONE);
            } else {
                labelBtnThirdProject.setText(userData.getProjectList().get(2).projectName);
                buttonThirdProject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(userData.getProjectList().get(2).getTicketColor())));
            }
            if (userData.getProjectList().size() < 4) {
                buttonSelectProject.setVisibility(View.GONE);
                labelSelectProject.setVisibility(View.GONE);
            }
        }

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    closeMenu();
                } else {
                    openMenu();

                    buttonFirstProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Boolean ticketState=true;
                            for(Ticket ticket:ticketList){
                                if(ticket.getStateStart()==false){
                                    ticketState=false;
                                }
                            }
                            ticketList.add(new Ticket("0:00", userData.getProjectList().get(0).projectName, Ticket.State.Start, Ticket.Selected.First, userData.getProjectList().get(0).getTicketColor(),ticketState));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            mAdapter.notifyDataSetChanged();
                            closeMenu();

                        }
                    });
                    buttonSecondProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boolean ticketState=true;
                            for(Ticket ticket:ticketList){
                                if(ticket.getStateStart()==false){
                                    ticketState=false;
                                }
                            }
                            ticketList.add(new Ticket("00:00", userData.getProjectList().get(1).projectName, Ticket.State.Start, Ticket.Selected.Second, userData.getProjectList().get(1).getTicketColor(),ticketState));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            mAdapter.notifyDataSetChanged();
                            closeMenu();

                        }
                    });
                    buttonThirdProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boolean ticketState=true;
                            for(Ticket ticket:ticketList){
                                if(ticket.getStateStart()==false){
                                    ticketState=false;
                                }
                            }
                            ticketList.add(new Ticket("00:00", userData.getProjectList().get(2).projectName, Ticket.State.Start, Ticket.Selected.Third, userData.getProjectList().get(2).getTicketColor(),ticketState));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            mAdapter.notifyDataSetChanged();
                            closeMenu();
                        }
                    });
                }
            }
        });

        final String[] projectList;
        if(userData.getProjectList().size() >3){
            projectList = new String[userData.getProjectList().size()-3];
        }else{
            projectList = new String[0];
        }

        int distance = 0;
        int projectListLength = 0;
        for (Project data : userData.getProjectList()) {
            if (distance < 3) {
                distance++;
                continue;
            }

            projectList[projectListLength] = data.projectName;
            projectListLength++;
        }

       /* final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setItems(projectList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String selectedProject = projectList[arg1];
                        String color = "#000000";
                        ArrayList<Project> projects = userData.getProjectList();
                        for (Project project : projects) {
                            if (project.projectName == selectedProject)
                                if (project.getTicketColor() != null)
                                    color = project.getTicketColor();
                        }
                        Boolean ticketState=true;
                        for(Ticket ticket : ticketList){
                            if(ticket.getStateStart()==false){
                                ticketState=false;
                            }
                        }
                        ticketList.add(new Ticket("00:00", selectedProject, Ticket.State.Start, Ticket.Selected.Other, color,ticketState));
                        userData.setTicketList(ticketList);
                        applicationTimeTracker.setUserData(userData);
                        mAdapter.notifyDataSetChanged();
                        closeMenu();

                        arg0.cancel();
                    }
                });*/

        buttonSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        buttonFinishWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadSpreadsheetData data = userData.getUploadSpreadsheetData();
                Calendar calender = Calendar.getInstance();
                int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                int cMinute = calender.get(Calendar.MINUTE);
                data.finishTime = new Time(cHourOfDay, cMinute, 00);
                ArrayList<Integer> removePositionList = new ArrayList<Integer>();
                boolean allDone = true;
                int position = 0;
                for (Ticket ticket : userData.getTicketList()) {
                    if (ticket.getDate() != null && ticket.getStartingTime() != null && ticket.getDescription() != null) {
                        if (ticket.getFinishTime() == null) {
                            Calendar calendar = Calendar.getInstance();
                            ticket.setFinishTime(new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
                        }
                        removePositionList.add(position);
                        position--;
                        applicationTimeTracker.addWorkOn(getApplicationContext(), userData.getUserAcount(), ticket);
                    } else {
                        if (ticket.getDescription() == null)
                            Toast.makeText(getApplicationContext(), "Ticket (" + ticket.getProject() + ") was not succesfuly send - Description is null", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(getApplicationContext(), "Ticket (" + ticket.getProject() + ") was not succesfuly send - Did not start", Toast.LENGTH_SHORT).show();
                        allDone = false;
                    }
                    position++;

                }
                if (allDone) {
                    userData.setTicketList(new ArrayList<Ticket>());
                    userData.addUploadRepository(data);
                    applicationTimeTracker.setUserData(userData);
                    applicationTimeTracker.cancelNotificationPerMinute();
                    applicationTimeTracker.addWorkDay(getApplicationContext(), userData.getUserAcount(), userData.getUploadSpreadsheetData());
                    userData.addUploadRepository(new UploadSpreadsheetData());
                    applicationTimeTracker.setUserData(userData);
                    finish();
                } else {
                    for (int location : removePositionList) {
                        ticketList.remove(location);
                        mAdapter.notifyItemRemoved(location);
                        mAdapter.notifyItemRangeChanged(location, mAdapter.getItemCount());
                    }
                    userData.setProfileDataDropdownArrayList(applicationTimeTracker.getWorkDaysAndWorkingOn(getApplicationContext(), userData.getUserAcount()));
                    userData.setTicketList(ticketList);
                    applicationTimeTracker.setUserData(userData);
                    closeMenu();
                }

            }
        });

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    buttonOptions.hide();
                    buttonOptions.setClickable(false);
                } else if (dy < 0) {
                    buttonOptions.show();
                    buttonOptions.setClickable(true);
                }
            }
        });
    }

    void openFabButtonWhenDelete() {
        buttonOptions.show();
        buttonOptions.setClickable(true);
        setFeautered();
    }



    private void closeMenu() {
        frameLayoutDim.setBackgroundColor(getResources().getColor(R.color.undimBackground));
        frameLayoutDim.setEnabled(false);
        frameLayoutDim.setClickable(false);

        buttonOptions.startAnimation(fabRotateClose);

        isOpen = false;

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

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    private void openMenu() {
        frameLayoutDim.setBackgroundColor(getResources().getColor(R.color.dimBackground));
        frameLayoutDim.setEnabled(true);
        frameLayoutDim.setClickable(true);

        buttonOptions.startAnimation(fabRotate);

        isOpen = true;

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

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFeautered() {
        if (userData.getProjectList().size() == 1) {

        } else if (userData.getProjectList().size() == 2) {

        } else if (userData.getProjectList().size() == 3) {

        } else if (userData.getProjectList().size() > 3) {

        }
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            closeMenu();
        } else {
            finish();
        }
    }
}

