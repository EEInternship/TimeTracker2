package Data;

import java.util.ArrayList;

/**
 * Created by IsakFe on 29. 03. 2017.
 */

public class TestClass {
    private String hour;
    private String project;

    public TestClass(ArrayList<TestClass> arrayList) {
    }

    public TestClass(String hour, String project) {
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
