package eeinternship.com.timetracker;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import Data.Ticket;

/**
 * Created by IsakFe on 29. 03. 2017.
 */

public class StartWorkAdapter extends RecyclerView.Adapter<StartWorkAdapter.IViewHolder> {
    ArrayList<Ticket> adapter = new ArrayList<>();




    public StartWorkAdapter(ArrayList<Ticket> adapterC) {
        this.adapter = adapterC;
    }

    public StartWorkAdapter.IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_start_work, parent, false);
        IViewHolder iViewHolder = new IViewHolder(v);

        return iViewHolder;
    }

    @Override
    public void onBindViewHolder(final IViewHolder holder, int position) {
        Ticket TC = adapter.get(position);
        holder.timeWork.setText(TC.getTime());
        holder.projectName.setText(TC.getProject());

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.working){
                    holder.imageButton.setBackgroundResource(R.drawable.img_stop_btn);
                    holder.working = true;
                    Calendar calendar = Calendar.getInstance();
                    holder.startTime = new Time(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                    CountDownTimer projectTimeTracker = new CountDownTimer(1000000000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            Calendar currentTime = Calendar.getInstance();
                            int workTimeHours = currentTime.get(Calendar.HOUR_OF_DAY) - holder.startTime.getHours();
                            int workTimeMinutes = currentTime.get(Calendar.MINUTE) - holder.startTime.getMinutes();
                            if(workTimeMinutes<10)
                                holder.timeWork.setText(workTimeHours + ":0" + workTimeMinutes);
                            else
                                holder.timeWork.setText(workTimeHours + ":" + workTimeMinutes);
                        }

                        public void onFinish() {}
                    };
                    projectTimeTracker.start();
                }
                else{
                    holder.imageButton.setBackgroundResource(R.drawable.img_start_btn);
                    holder.working = false;
                }
                notifyDataSetChanged();
            }
        });

    }




    @Override
    public int getItemCount() {
        return adapter.size();
    }

    public class IViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, timeWork;
        ImageButton imageButton;
        Time startTime;
        Time finishTime;
        boolean working;
        public IViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.project_name);
            timeWork = (TextView) itemView.findViewById(R.id.hour_min);
            imageButton = (ImageButton) itemView.findViewById(R.id.btn_start_work);
            working = false;

        }
    }
}
