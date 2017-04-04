package eeinternship.com.timetracker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import Data.UserData;
import Data.TestProject;


public class ApplicationTimeTracker extends Application{
    private UserData userData;

    @Override
    public void onCreate() {
        super.onCreate();
        if (userData == null){
            userData = new UserData();
        }
        userData.scenariData();
    }



    public UserData getUserData(){
        return userData;
    }

    public void setUserData(UserData data) {
        this.userData = data;
    }

    public void getActiveProjects(Context context){
        Log.i("Running:", "Fetching active project list");
        Ion.with(context)
                .load("GET","https://nameless-oasis-70424.herokuapp.com/getactiveprojects/?format=json")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if(result != null){
                            Log.i("Info: Result size:",String.valueOf(result.size()));
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            for(int i=0;i<result.size();i++) {
                                TestProject project = gson.fromJson(result.get(i), TestProject.class);
                                if (project == null) {
                                    Log.e("Error","Object is null!");
                                } else {
                                    Log.e("Error",i+" "+project.getProject_name());
                                }
                            }
                        } else {
                            Log.e("Error","Result is empty!");
                        }
                    }
                });
    }
}
