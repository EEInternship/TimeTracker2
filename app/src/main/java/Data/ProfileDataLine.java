package Data;

import java.util.Calendar;

/**
 * Created by Klemen on 4/13/2017.
 */

public class ProfileDataLine {
    private String projectName;
    private String workDescription;
    private String startingTime;
    private String finishTime;
    private String workTime;
    private String projectColor = "#1E90FF";
    private String date;
    private String id;
    public ProfileDataLine(){}

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public void calculateTime(){
        String[] arrFinish = finishTime.split(":");
        String[] arrStarting = finishTime.split(":");
        int hours = Integer.parseInt(arrFinish[0]) - Integer.parseInt(arrStarting[0]) ;
        int minutes = Integer.parseInt(arrFinish[1]) - Integer.parseInt(arrStarting[1]) ;
        this.workTime = hours + ":" + minutes + ":00";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(String projectColor) {
        this.projectColor = projectColor;
    }
}


