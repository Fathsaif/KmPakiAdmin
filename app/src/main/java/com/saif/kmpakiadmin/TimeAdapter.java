package com.saif.kmpakiadmin;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mosaad on 23/11/2017.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.viewHolder> {
    private ArrayList<ModelData> data;
    private Context mContext;
    Calendar calendar;
    Calendar startCalendar;
    private CountDownTimer countDownTimer;

    public TimeAdapter(ArrayList<ModelData> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        calendar = Calendar.getInstance();
        startCalendar = Calendar.getInstance();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        ModelData book = data.get(position);
        holder.title.setText(book.getSummary());
        Log.d("saifbook",book.date+"/"+book.getSummary()+"/"+book.getId());
        calendar.set(book.getYear(),book.getMonth(),book.getDay(),00,00,00);
        holder.timer(((calendar.getTimeInMillis())-(startCalendar.getTimeInMillis()))-TimeUnit.DAYS.toMillis(31));
        long j = calendar.getTimeInMillis()-startCalendar.getTimeInMillis();
        int days = (int) TimeUnit.MILLISECONDS.toDays(j);
        Log.d("saif=date",calendar.getTime()+"");
        Log.d("saif",TimeUnit.MILLISECONDS.toDays(j)+"");
        }

    @Override
    public int getItemCount() {
        if (data==null)return 0;

        return data.size();

    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_title)TextView title;
        @BindView(R.id.txt_hours_count)TextView hoursCounterTv;
        @BindView(R.id.txt_min_count)TextView minCounterTv;
        @BindView(R.id.txt_sec_count)TextView secCounterTv;
        @BindView(R.id.txt_days_count)TextView daysCounterTv;
        @BindView(R.id.day_progress)DonutProgress dayProgress;
        @BindView(R.id.hours_progress)DonutProgress hourProgress;
        @BindView(R.id.mins_progress)DonutProgress minProgress;
        @BindView(R.id.sec_progress)DonutProgress secProgress;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void timer(long milisecond){
            countDownTimer = new CountDownTimer(milisecond,1000) {
                @Override
                public void onTick(long l) {

                    long days = TimeUnit.MILLISECONDS.toDays(l);
                    dayProgress.setMax(356);
                    dayProgress.setDonut_progress(String.valueOf(days));
                    l -= TimeUnit.DAYS.toMillis(days);
                    daysCounterTv.setText(""+days);
                    long hours = TimeUnit.MILLISECONDS.toHours(l);
                    hourProgress.setMax(24);
                    hourProgress.setDonut_progress(String.valueOf(hours));
                    l -= TimeUnit.HOURS.toMillis(hours);
                    hoursCounterTv.setText(""+hours);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(l);
                    minProgress.setMax(60);
                    minProgress.setDonut_progress(String.valueOf(minutes));
                    l -= TimeUnit.MINUTES.toMillis(minutes);
                    minCounterTv.setText(""+ minutes);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(l);
                    secProgress.setMax(60);
                    secProgress.setDonut_progress(String.valueOf(seconds));
                    secCounterTv.setText(""+ seconds);

                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }


}
