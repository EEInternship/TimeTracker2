package eeinternship.com.timetracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnOpen,btnStartWork,btnProfile;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        btnOpen=(Button)findViewById(R.id.btn_open_door);
        btnStartWork=(Button)findViewById(R.id.btn_start_work);
        btnProfile=(Button)findViewById(R.id.btn_profile);

        // new window StartWork
        btnStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startWorkActivity=new Intent(getApplication(),StartWorkActivity.class);
                startActivity(startWorkActivity);
            }
        });
    }
}
