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
    public void onBindViewHolder(final IViewHolder holder, final int position) {
        Ticket TC = adapter.get(position);
        holder.projectName.setText(TC.getProject());
        holder.timeWork.setText(TC.getTime());

        final CountDownTimer projectTimeTracker = new CountDownTimer(1000000000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                Calendar current = Calendar.getInstance();
                Time diff = new Time ( current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), current.get(Calendar.SECOND));
                long difference = diff.getTime() - holder.startTime.getTime();
                Time differenceTime = new Time(difference);
                int workTimeHours = differenceTime.getHours()-1;
                int workTimeMinutes = differenceTime.getMinutes();
                setTextView(workTimeHours,workTimeMinutes,holder.timeWork,holder.showTimer);
            }

            @Override
            public void onFinish() {

            }
        };
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                if(holder.startWork){
                    holder.startTime = new Time(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                    holder.startWork = false;
                    holder.imageButton.setBackgroundResource(R.drawable.img_stop_btn);
                    projectTimeTracker.start();

                }else{
                    holder.showTimer = false;
                    projectTimeTracker.cancel();
                    holder.finishTime = new Time(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                    holder.imageButton.setVisibility(View.GONE);
                    long differenceLong = holder.finishTime.getTime() - holder.startTime.getTime();
                    Time workTime = new Time(differenceLong);
                    if(workTime.getMinutes()<10)
                        holder.timeWork.setText(workTime.getHours()-1+":0"+workTime.getMinutes());
                    else
                        holder.timeWork.setText(workTime.getHours()-1+":"+workTime.getMinutes());

                }



            }


        });


    }

    public void setTextView(int hours, int minutes, TextView textView,boolean doWork){
        if(!doWork )
            return;

        if(minutes<10)
            textView.setText(hours+":0"+minutes);
        else
            textView.setText(hours + ":"+minutes);
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
        boolean startWork = true;
        boolean showTimer = true;
        public IViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.project_name);
            timeWork = (TextView) itemView.findViewById(R.id.hour_min);
            imageButton = (ImageButton) itemView.findViewById(R.id.btn_start_work);
            if(startWork)
                imageButton.setBackgroundResource(R.drawable.img_start_btn);

        }
    }
}
