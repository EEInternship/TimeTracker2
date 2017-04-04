package Data;

import java.sql.Time;

/**
 * Created by Klemen on 16. 03. 2017.
 */

public class Project {
    public String projectName;
    public Time startingTime;
    public Time finishTime;
    private Time totalWork;
    public String description;

    public Project(){}

    public void setTotalWork(){
        totalWork = new Time(finishTime.getHours()-startingTime.getHours(),finishTime.getMinutes()-startingTime.getMinutes(),finishTime.getSeconds()-startingTime.getSeconds());
    }
}
