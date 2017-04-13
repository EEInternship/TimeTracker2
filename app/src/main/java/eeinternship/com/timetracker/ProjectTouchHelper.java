package eeinternship.com.timetracker;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by IsakFe on 13. 04. 2017.
 */

public class ProjectTouchHelper extends ItemTouchHelper.SimpleCallback {
    private SettingsAdapter projectAdapter;

    public ProjectTouchHelper(SettingsAdapter p){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0);
        this.projectAdapter = p;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        projectAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //projectAdapter.remove(viewHolder.getAdapterPosition());
    }
}