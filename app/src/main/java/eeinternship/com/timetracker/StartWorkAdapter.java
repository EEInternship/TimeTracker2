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
        final Ticket TC = adapter.get(position);

        holder.startWork = TC.getState();
        holder.projectName.setText(TC.getProject());

        if(holder.startWork != Ticket.State.Done){
            if(holder.startWork == Ticket.State.Start){
                holder.showTimer = true;
                holder.imageButton.setBackgroundResource(R.drawable.img_start_btn);
                TC.setTime("0:00");
            }
            else if(holder.startWork == Ticket.State.Stop)
                holder.imageButton.setBackgroundResource(R.drawable.img_stop_btn);
            else if(holder.startWork == Ticket.State.Restart)
                holder.imageButton.setBackgroundResource(R.drawable.img_recreate_btn);

            holder.timeWork.setText(TC.getTime());
            holder.imageButton.setVisibility(View.VISIBLE);
        }

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

                if(holder.startWork == Ticket.State.Start){
                    holder.startTime = new Time(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                    holder.startWork = Ticket.State.Stop;
                    TC.setState(holder.startWork);
                    adapter.set(position,TC);
                    holder.imageButton.setBackgroundResource(R.drawable.img_stop_btn);
                    projectTimeTracker.start();

                }else if(holder.startWork == Ticket.State.Stop){
                    holder.showTimer = false;
                    projectTimeTracker.cancel();
                    holder.finishTime = new Time(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                    long differenceLong = holder.finishTime.getTime() - holder.startTime.getTime();
                    Time workTime = new Time(differenceLong);
                    TC.setState(holder.startWork);
                    adapter.set(position,TC);
                    if(workTime.getMinutes()<10)
                        holder.timeWork.setText(workTime.getHours()-1+":0"+workTime.getMinutes());
                    else
                        holder.timeWork.setText(workTime.getHours()-1+":"+workTime.getMinutes());


                    String timeWork = holder.timeWork.getText().toString();

                    TC.setTime(timeWork);
                    adapter.set(position,TC);

                    holder.imageButton.setBackgroundResource(R.drawable.img_recreate_btn);
                    holder.startWork = Ticket.State.Restart;
                }else if(holder.startWork == Ticket.State.Restart){
                    holder.imageButton.setVisibility(View.GONE);
                    adapter.add(new Ticket("0:00",TC.getProject(), Ticket.State.Start));
                    notifyItemChanged(adapter.size()-1);
                    holder.startWork = Ticket.State.Done;
                    TC.setState(holder.startWork);
                    adapter.set(position,TC);
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
        Ticket.State startWork = Ticket.State.Start;
        boolean showTimer = true;
        public IViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.project_name);
            timeWork = (TextView) itemView.findViewById(R.id.hour_min);
            imageButton = (ImageButton) itemView.findViewById(R.id.btn_start_work);
            if(startWork == Ticket.State.Start)
                imageButton.setBackgroundResource(R.drawable.img_start_btn);

        }
    }


}
