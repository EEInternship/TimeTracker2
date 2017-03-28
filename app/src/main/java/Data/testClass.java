package Data;

import java.util.ArrayList;

/**
 * Created by IsakFe on 27. 03. 2017.
 */

public class testClass {
    private String hour;
    private String project;

    public testClass(ArrayList<testClass> list) {}

    public testClass(String hour, String project) {
        this.hour = hour;
        this.project = project;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
