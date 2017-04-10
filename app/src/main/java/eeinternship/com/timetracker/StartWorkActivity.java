package eeinternship.com.timetracker;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SimpleOnItemTouchListener;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import Data.Project;
import Data.Ticket;
import Data.UploadSpreadsheetData;
import Data.UserData;

public class StartWorkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    FloatingActionButton buttonOptions, buttonFinishWork, buttonFirstProject, buttonSecondProject, buttonThirdProject, buttonSelectProject;
    TextView labelBtnFirstProject, labelBtnSecondProject, labelBtnThirdProject, labelSelectProject, labelFinishWork;

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setAdapter();

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    closeMenu();
                    isOpen = false;
                } else {
                    openMenu();
                    buttonFirstProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ticketList.add(new Ticket("0:00", labelBtnFirstProject.getText().toString(), Ticket.State.Start, Ticket.Selected.First));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            adapter.notifyDataSetChanged();
                            closeMenu();
                        }
                    });
                    buttonSecondProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ticketList.add(new Ticket("00:00", labelBtnSecondProject.getText().toString(), Ticket.State.Start, Ticket.Selected.Second));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            adapter.notifyDataSetChanged();
                            closeMenu();

                        }
                    });
                    buttonThirdProject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ticketList.add(new Ticket("00:00", labelBtnThirdProject.getText().toString(), Ticket.State.Start, Ticket.Selected.Third));
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            adapter.notifyDataSetChanged();
                            closeMenu();
                        }
                    });
                }
            }
        });
        final String[] projectList = new String[userData.getProjectList().size()];
        int projectListLength = 0;
        for (Project data : userData.getProjectList()) {
            projectList[projectListLength] = data.projectName;
            projectListLength++;
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setItems(projectList,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String selectedProject = projectList[arg1].toString();
                        ticketList.add(new Ticket("00:00", selectedProject, Ticket.State.Start, Ticket.Selected.Other));
                        userData.setTicketList(ticketList);
                        applicationTimeTracker.setUserData(userData);
                        adapter.notifyDataSetChanged();
                        closeMenu();

                        arg0.cancel();
                    }
                });

        buttonSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
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
                userData.setTicketList(new ArrayList<Ticket>());
                userData.addUploadRepository(data);
                applicationTimeTracker.setUserData(userData);
                finish();
                //ToDo Clear UploadRepository
                //TODO Send data to Database
            }
        });


        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        } else {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipeLeft(int position) {
                        return false;
                    }

                    @Override
                    public boolean canSwipeRight(int position) {
                        return true;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            ticketList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                            buttonOptions.show();
                            buttonOptions.setClickable(true);
                            userData.setTicketList(ticketList);
                            applicationTimeTracker.setUserData(userData);
                            //TODO send to Database
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        recyclerView.addOnItemTouchListener(swipeTouchListener);

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

    private void setAdapter() {
        adapter = new StartWorkAdapter(ticketList);
        recyclerView.setAdapter(adapter);
    }

    private void closeMenu() {
        frameLayoutDim.setBackgroundColor(getResources().getColor(R.color.undimBackground));
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

        setViewAndChildrenEnabled(recyclerView, true);
    }

    private void openMenu() {
        frameLayoutDim.setBackgroundColor(getResources().getColor(R.color.dimBackground));
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

        setViewAndChildrenDisabled(recyclerView, false);
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

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenDisabled(child, enabled);
            }
        }
    }

    private static void setViewAndChildrenDisabled(View view, boolean disabled) {
        view.setEnabled(disabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, disabled);
            }
        }
    }

}

