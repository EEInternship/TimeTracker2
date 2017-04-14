package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 4/13/2017.
 */

public class ProfileDataDropdown {
    private ArrayList<ProfileDataLine> profileDataLineArrayList;
    private String date;
    private String totalTime;

    public ProfileDataDropdown() {
    }

    public ArrayList<ProfileDataLine> getProfileDataLineArrayList() {
        return profileDataLineArrayList;
    }

    public void setProfileDataLineArrayList(ArrayList<ProfileDataLine> profileDataLineArrayList) {
        this.profileDataLineArrayList = profileDataLineArrayList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String workTime,String overTime) {
        this.totalTime = workTime;
        //ToDo: workTime + overTime
    }

    public ProfileDataLine getProifleDataLine(int index){
        return profileDataLineArrayList.get(index);
    }

}
