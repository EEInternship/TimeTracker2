package com.eeinternship.timetracker;

import android.app.Application;

import Data.UserData;

/**
 * Created by Klemen on 3/20/2017.
 */

public class ApplicationTimeTracker extends Application{
    private UserData userData;

    @Override
    public void onCreate() {
        super.onCreate();
        if (userData == null){
            userData = new UserData();
        }
    }



    public UserData getUserData(){
        return userData;
    }

    public void setUserData(UserData data) {
        this.userData = data;
    }
}
