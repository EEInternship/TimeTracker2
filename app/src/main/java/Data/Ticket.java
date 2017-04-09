package Data;

import java.util.ArrayList;

/**
 * Created by IsakFe on 29. 03. 2017.
 */

public class Ticket {
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
    }

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
