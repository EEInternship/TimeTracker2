package com.eeinternship.timetracker;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class StartWorkActivity extends AppCompatActivity {

    private Button btnSelectProject;
    private Button btnStartTime;
    private Button btnNextProject;

    private LinearLayout btnSelectedProject;
    private LinearLayout txtSelectProject;
    private LinearLayout btnStartTimeShadow;
    private LinearLayout txtWhatRUDoing;
    private LinearLayout btnNextProjectShadow;

    private EditText editTextTime;
    private EditText editTextDesc;

    boolean clickedStartStop = false;
    boolean clickedSelectProject = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        btnSelectedProject = (LinearLayout) findViewById(R.id.background_shadow_open);
        txtSelectProject = (LinearLayout) findViewById(R.id.selected_project);
        btnStartTimeShadow = (LinearLayout) findViewById(R.id.shadow_start_time);
        txtWhatRUDoing = (LinearLayout) findViewById(R.id.what_have_been_you_doin);
        btnNextProjectShadow = (LinearLayout) findViewById(R.id.background_shadow_next_project);

        btnStartTime = (Button) findViewById(R.id.button_start_time);
        btnSelectProject = (Button) findViewById(R.id.button_select_project);
        btnNextProject = (Button) findViewById(R.id.button_next_project);

        editTextTime = (EditText) findViewById(R.id.edittext_time_work);
        editTextDesc = (EditText) findViewById(R.id.desc_what_have_been_you_doing);

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));

        // btnStartWork
        btnSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSelectedProject.setVisibility(View.GONE);
                txtSelectProject.setVisibility(View.VISIBLE);

                // button
                Drawable myD = null;
                Resources res = getResources();
                try {
                    myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_start_time_green));
                } catch (Exception e) {
                    // if something goes wrong.
                }
                btnStartTime.setBackground(myD);

                // shadow
                Drawable myDs = null;
                Resources resS = getResources();
                try {
                    myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_start_work_shadow_green));
                } catch (Exception e) {
                    // if something goes wrong.
                }
                btnStartTimeShadow.setBackground(myDs);

                // edittext color
                editTextTime.setTextColor(Color.parseColor("#04b795"));

                clickedSelectProject = true;

                if (clickedSelectProject == true) {
                    btnStartTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickedStartStop == false) {
                                // button
                                Drawable myD = null;
                                Resources res = getResources();
                                try {
                                    myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_start_time_orange));
                                } catch (Exception e) {
                                    // if something goes wrong.
                                }
                                btnStartTime.setBackground(myD);
                                btnStartTime.setText("Stop");

                                // shadow
                                Drawable myDs = null;
                                Resources resS = getResources();
                                try {
                                    myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_start_work_shadow_orange));
                                } catch (Exception e) {
                                    // if something goes wrong.
                                }
                                btnStartTimeShadow.setBackground(myDs);
                                editTextTime.setTextColor(Color.parseColor("#ef4907"));
                                clickedStartStop = true;

                                // green text for WHAT HAVE YOU BEEN DOING
                                Drawable myDWhat = null;
                                Resources resW = getResources();
                                try {
                                    myDWhat = Drawable.createFromXml(resW, resW.getXml(R.xml.design_window_green_left_right_up));
                                } catch (Exception e) {
                                    // if something goes wrong.
                                }
                                txtWhatRUDoing.setBackground(myDWhat);

                                editTextDesc.setEnabled(true);
                                editTextTime.setEnabled(true);

                            } else {
                                // button
                                Drawable myD = null;
                                Resources res = getResources();
                                try {
                                    myD = Drawable.createFromXml(res, res.getXml(R.xml.btn_design_start_time_green));
                                } catch (Exception e) {
                                    // if something goes wrong.
                                }
                                btnStartTime.setBackground(myD);
                                btnStartTime.setText("Start");

                                // shadow
                                Drawable myDs = null;
                                Resources resS = getResources();
                                try {
                                    myDs = Drawable.createFromXml(resS, resS.getXml(R.xml.btn_design_start_work_shadow_green));
                                } catch (Exception e) {
                                    // if something goes wrong.
                                }
                                btnStartTimeShadow.setBackground(myDs);
                                editTextTime.setTextColor(Color.parseColor("#04b795"));
                                clickedStartStop = false;

                                String descFromEditText = "";
                                descFromEditText = editTextDesc.getText().toString();
                                if (clickedStartStop == false && !descFromEditText.matches("")) {
                                    //shadow
                                    Drawable myNxt = null;
                                    Resources resNxt = getResources();
                                    try {
                                        myNxt = Drawable.createFromXml(resNxt, resNxt.getXml(R.xml.btn_design_next_project_shadow_orange));
                                    } catch (Exception e) {
                                        // if something goes wrong.
                                    }
                                    btnNextProjectShadow.setBackground(myNxt);

                                    // button
                                    Drawable myNxtBtn = null;
                                    Resources resNxtBtn = getResources();
                                    try {
                                        myNxtBtn = Drawable.createFromXml(resNxtBtn, resNxtBtn.getXml(R.xml.btn_design_next_project_orange));
                                    } catch (Exception e) {
                                        // if something goes wrong.
                                    }
                                    btnNextProject.setBackground(myNxtBtn);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
