package Data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by IsakFe on 29. 03. 2017.
 */

public class Ticket {
    private Calendar date;
    private Time startingTime;
    private Time finishTime;
    private String description;
    private String time;
    private String project;
    private State state;
    private Selected selected;

    public Ticket(ArrayList<Ticket> arrayList) {
    }

    public Ticket(){

    }

    public Ticket(String hour, String project,State _state,Selected _selected) {
        this.time = hour;
        this.project = project;
        state = _state;
        selected = _selected;
        description = "";
    }

    public void setStartingTime(Time start){startingTime=start;}
public void setFinishTime(Time finish){finishTime = finish;}
    public Time getStartingTime(){return startingTime;}
    public Time getFinishTime(){return finishTime;}

    public String getDescription(){return description;}
    public void setDescription(String desc){description = desc;}

    public void setDate(Calendar cal){date = cal;}
    public Calendar getDate(){return date;}


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setState(State _state){
        state=_state;
    }
    public State getState(){
        return state;
    }

    public String getProject() {
        return project;
    }


    public Selected getSelected(){return selected;}
    public void setSelected(Selected _selected){selected=_selected;}

    public void setProject(String project) {
        this.project = project;
    }
    public enum State{
        Start,Stop,Restart,Done
    }

    public enum Selected{
        First,Second,Third,Other
    }
}
