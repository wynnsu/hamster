package npu.edu.hamster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by su153 on 2/14/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder> {

    private List<NPU_Event> eventList;
    private Context context;

    public MainRecyclerViewAdapter(Context context, List<NPU_Event> eventList) {
        this.eventList = eventList;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        NPU_Event event = eventList.get(position);
        holder.vDay.setText(event.getDay());
        holder.vMonth.setText(event.getMonth());
        holder.vContent.setText(event.getContent());
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new MainViewHolder(eventView);
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        protected TextView vDay;
        protected TextView vMonth;
        protected TextView vContent;

        public MainViewHolder(View v) {
            super(v);
            vDay = (TextView) v.findViewById(R.id.event_day);
            vMonth = (TextView) v.findViewById(R.id.event_month);
            vContent = (TextView) v.findViewById(R.id.event_content);
        }
    }
}
