package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 4/13/2017.
 */

public class ProfileDataDropdown {
    private ArrayList<ProfileDataLine> profileDataLineArrayList;
    private String date;
    private String totalTime;
    private String startingTime;
    private String finishTime;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setTotalTime(String workTime, String overTime) {
        this.totalTime = workTime;
        //ToDo: workTime + overTime
    }

    public ProfileDataLine getProifleDataLine(int index){
        return profileDataLineArrayList.get(index);
    }

}
