package com.eeinternship.timetracker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MonthlyOverview extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Contact> list = new ArrayList<Contact>();

    String[] dayy, datee, hourss;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_overview);

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));

        dayy = getResources().getStringArray(R.array.day_name);
        datee = getResources().getStringArray(R.array.date_name);
        hourss = getResources().getStringArray(R.array.hour_name);

        int count = 0;
        for (String d : dayy
                ) {
            Contact con = new Contact(d, datee[count], hourss[count]);
            count++;

            list.add(con);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new MonthlyAdapter(list);
        recyclerView.setAdapter(adapter);

    }


}
