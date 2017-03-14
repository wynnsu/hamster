package npu.edu.hamster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

import static npu.edu.hamster.module.CardContent.EVENT;
import static npu.edu.hamster.module.CardContent.LOGIN;
import static npu.edu.hamster.module.CardContent.NEWS;

/**
 * Created by su153 on 2/14/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseModule> cardList;
    private Context context;

    public static int LOGIN_REQUEST = 0;
    public static int LOGIN_SUCCESS = 1;

    public MainRecyclerViewAdapter(Context context, List<BaseModule> list) {
        this.cardList = list;
        this.context = context;
        setHasStableIds(true);
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
        return cardList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("CALLING", "OnBindViewHolder, position=" + position);
        BaseModule module = cardList.get(position);
        switch (module.getType()) {
            case CardContent.EVENT:
                EventModule event = (EventModule) module;
                EventViewHolder eventHolder = (EventViewHolder) holder;
                eventHolder.vDay.setText(event.getDay());
                eventHolder.vMonth.setText(event.getMonth());
                eventHolder.vContent.setText(event.getContent());
                break;
            case CardContent.NEWS:
                NewsModule news = (NewsModule) module;
                NewsViewHolder newsHolder = (NewsViewHolder) holder;
                newsHolder.vTitle.setText(news.getTitle());
                newsHolder.vContent.setText(news.getContent());
                Picasso.with(context).load(news.getImgUrl()).into(newsHolder.vImg);
                break;
            case CardContent.LOGIN:
                LoginModule login = (LoginModule) module;
                LoginViewHolder loginHolder = (LoginViewHolder) holder;
                loginHolder.vText.setText(login.getContent());
                String url = login.getImgUrl();
                Drawable res = null;
                loginHolder.vImg.setImageDrawable(null);
                if (url.endsWith("welcome")) {
                    res = ContextCompat.getDrawable(context, R.drawable.ic_done);
                    Log.i("CARD UPDATE", url);
                    loginHolder.itemView.setOnClickListener(null);
                    loginHolder.vImg.setImageDrawable(res);
                    loginHolder.vImg.setVisibility(View.VISIBLE);
                    loginHolder.vProgress.setVisibility(View.GONE);
                } else if (url.endsWith("lock")) {
                    res = ContextCompat.getDrawable(context, R.drawable.ic_lock);
                    Log.i("CARD UPDATE", url);
                    loginHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Activity a = (Activity) v.getContext();
                            int request = LOGIN_REQUEST;
                            Intent intent = new Intent(a, LoginActivity.class);
                            a.startActivityForResult(intent, request);
                        }
                    });
                    loginHolder.vImg.setImageDrawable(res);
                } else if (url.endsWith("login")) {
                    loginHolder.vProgress.setVisibility(View.VISIBLE);
                    loginHolder.vImg.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position);
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
        protected TextView vText;
        protected View vProgress;

        public LoginViewHolder(View v) {
            super(v);
            vImg = (ImageView) v.findViewById(R.id.login_img);
            vText = (TextView) v.findViewById(R.id.login_text);
            vProgress = v.findViewById(R.id.login_progress);
        }
    }

}
