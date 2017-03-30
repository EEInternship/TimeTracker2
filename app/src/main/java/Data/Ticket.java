package Data;

import java.util.ArrayList;

/**
 * Created by IsakFe on 29. 03. 2017.
 */

public class Ticket {
    private String time;
    private String project;

    public Ticket(ArrayList<Ticket> arrayList) {
    }

    public Ticket(){

    }

    public Ticket(String hour, String project) {
        this.time = hour;
        this.project = project;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
