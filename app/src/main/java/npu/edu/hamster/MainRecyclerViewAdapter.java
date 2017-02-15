package npu.edu.hamster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import npu.edu.hamster.module.CardModule;
import npu.edu.hamster.module.EventModule;

/**
 * Created by su153 on 2/14/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardModule> cardList;
    private Context context;
    private final int EVENT = 0, NEWS = 1, GRADE = 2, HOMEWORK = 3, COURSE = 4;

    public MainRecyclerViewAdapter(Context context, List<CardModule> list) {
        this.cardList = list;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        CardModule.ContentType type = cardList.get(position).getContentType();
        switch (type) {
            case EVENT:
                return EVENT;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardModule module = cardList.get(position);
        switch (module.getContentType()) {
            case EVENT:
                EventModule event = (EventModule) module;
                EventViewHolder eventHolder = (EventViewHolder) holder;
                eventHolder.vDay.setText(event.getDay());
                eventHolder.vMonth.setText(event.getMonth());
                eventHolder.vContent.setText(event.getContent());
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EVENT:
                View eventView = inflater.inflate(R.layout.card_event, parent, false);
                holder = new EventViewHolder(eventView);
                break;
            default:
                holder = null;
                break;
        }
        return holder;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView vDay;
        protected TextView vMonth;
        protected TextView vContent;

        public EventViewHolder(View v) {
            super(v);
            vDay = (TextView) v.findViewById(R.id.event_day);
            vMonth = (TextView) v.findViewById(R.id.event_month);
            vContent = (TextView) v.findViewById(R.id.event_content);
        }
    }


}
