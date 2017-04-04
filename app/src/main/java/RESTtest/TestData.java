package RESTtest;

import java.util.ArrayList;

/**
 * Created by Ales on 4. 04. 2017.
 */

public class TestData {
    TestWorkDay work_day;
    ArrayList<TestWorkingOn> working_on;

    public TestData(TestWorkDay work_day, ArrayList<TestWorkingOn> working_on) {
        this.work_day = work_day;
        this.working_on = working_on;
    }

    public TestWorkDay getWork_day() {
        return work_day;
    }

    public void setWork_day(TestWorkDay work_day) {
        this.work_day = work_day;
    }

    public ArrayList<TestWorkingOn> getWorking_on() {
        return working_on;
    }

    public void setWorking_on(ArrayList<TestWorkingOn> working_on) {
        this.working_on = working_on;
    }

    @Override
    public String toString() {
        String string = this.work_day.toString()+"\n";
        for(int i=0;i<this.working_on.size();i++)
        {
            string+="\t"+this.working_on.get(i).toString()+"\n";
        }
        return string;
    }
}
