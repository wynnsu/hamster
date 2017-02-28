package npu.edu.hamster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CardContent;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.NewsModule;

/**
 * Created by su153 on 2/14/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseModule> cardList;
    private Context context;
    private final int EVENT = 0, NEWS = 1, GRADE = 2, HOMEWORK = 3, COURSE = 4, LOGIN = 5;

    public MainRecyclerViewAdapter(Context context, List<BaseModule> list) {
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
        CardContent.ContentType type = cardList.get(position).getContentType();
        switch (type) {
            case EVENT:
                return EVENT;
            case NEWS:
                return NEWS;
            case LOGIN:
                return LOGIN;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardContent module = cardList.get(position);
        switch (module.getContentType()) {
            case EVENT:
                EventModule event = (EventModule) module;
                EventViewHolder eventHolder = (EventViewHolder) holder;
                eventHolder.vDay.setText(event.getDay());
                eventHolder.vMonth.setText(event.getMonth());
                eventHolder.vContent.setText(event.getContent());
                break;
            case NEWS:
                NewsModule news = (NewsModule) module;
                NewsViewHolder newsHolder = (NewsViewHolder) holder;
                newsHolder.vTitle.setText(news.getTitle());
                newsHolder.vContent.setText(news.getContent());
                Picasso.with(context).load(news.getImgUrl()).into(newsHolder.vImg);
                break;
            case LOGIN:
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
            case NEWS:
                View newsView = inflater.inflate(R.layout.card_news, parent, false);
                holder = new NewsViewHolder(newsView);
                break;
            case LOGIN:
                View loginView = inflater.inflate(R.layout.card_login, parent, false);
                holder = new LoginViewHolder(loginView);
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

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImg;
        protected TextView vTitle;
        protected TextView vContent;

        public NewsViewHolder(View v) {
            super(v);
            vImg = (ImageView) v.findViewById(R.id.news_image);
            vTitle = (TextView) v.findViewById(R.id.news_title);
            vContent = (TextView) v.findViewById(R.id.news_content);
        }
    }

    public static class LoginViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImg;

        public LoginViewHolder(View v) {
            super(v);
            vImg = (ImageView) v.findViewById(R.id.login_img);
        }
    }

}
