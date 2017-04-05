package RESTtest;

/**
 * Created by Ales on 4. 04. 2017.
 */

public class TestWorkingOn {
    TestProject project;
    String date;
    String starting_time;
    String finish_time;
    String working_hours;
    String description;

    public TestWorkingOn(TestProject project, String date, String starting_time, String finish_time, String working_hours, String description) {
        this.project = project;
        this.date = date;
        this.starting_time = starting_time;
        this.finish_time = finish_time;
        this.working_hours = working_hours;
        this.description = description;
    }

    public TestProject getProject() {
        return project;
    }

    public void setProject(TestProject project) {
        this.project = project;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(String starting_time) {
        this.starting_time = starting_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.project.getProject_name()+" "+
                this.date+" "+
                this.starting_time+" "+
                this.finish_time+" "+
                this.working_hours+" "+
                this.description;
    }
}
