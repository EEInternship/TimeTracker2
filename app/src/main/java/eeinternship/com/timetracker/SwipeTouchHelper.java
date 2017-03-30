package eeinternship.com.timetracker;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by IsakFe on 30. 03. 2017.
 */

public class SwipeTouchHelper extends ItemTouchHelper.SimpleCallback {
    private StartWorkAdapter Startworkadapter;

    public SwipeTouchHelper(StartWorkAdapter startWorkAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.Startworkadapter = startWorkAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Startworkadapter.remove(viewHolder.getAdapterPosition());
    }
}
