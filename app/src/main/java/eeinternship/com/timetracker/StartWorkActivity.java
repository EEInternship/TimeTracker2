package eeinternship.com.timetracker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import Data.TestClass;

public class StartWorkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String[] name, hour;

    ArrayList<TestClass> arrayList = new ArrayList<TestClass>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        name = getResources().getStringArray(R.array.day_name);
        hour = getResources().getStringArray(R.array.hour_name);
        int count = 0;
        for (String Name : name) {
            TestClass classT = new TestClass(hour[count], Name);
            count++;
            arrayList.add(classT);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_start_work);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new StartWorkAdapter(arrayList);
        recyclerView.setAdapter(adapter);
    }
}
