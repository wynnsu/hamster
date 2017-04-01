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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import npu.edu.hamster.module.ActivityModule;
import npu.edu.hamster.module.AttendanceModule;
import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CardContent;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.GradeModule;
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

import static npu.edu.hamster.module.CardContent.ACTIVITY;
import static npu.edu.hamster.module.CardContent.ATTENDANCE;
import static npu.edu.hamster.module.CardContent.EVENT;
import static npu.edu.hamster.module.CardContent.GRADE;
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
        String url = "";
        Drawable res;
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
            case CardContent.ATTENDANCE:
                AttendanceModule attend = (AttendanceModule) module;
                AttendanceViewHolder attendHolder = (AttendanceViewHolder) holder;
                if (attendHolder.vLayout.getChildCount() > 0) {
                    break;
                }
                Map<String, String> map = attend.getAttendanceMap();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.attendance_course, null);
                    TextView title = (TextView) v.findViewById(R.id.attend_title);
                    String key = entry.getKey();
                    String val = entry.getValue();
                    title.setText(key);
                    LinearLayout layout = (LinearLayout) v.findViewById(R.id.attend_queue);
                    String[] attendList = val.split(" ");
                    for (String s : attendList) {
                        View vb = vi.inflate(R.layout.attendance_block, null);
                        ImageView iv = (ImageView) vb.findViewById(R.id.attend_icon_view);
                        if (s.equals("P")) {
                            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_box));
                        } else {
                            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear));
                        }
                        layout.addView(vb);
                    }
                    attendHolder.vLayout.addView(v);
                }
                break;
            case CardContent.ACTIVITY:
                ActivityModule activity = (ActivityModule) module;
                ActivityViewHolder activityHolder = (ActivityViewHolder) holder;
                activityHolder.vTitle.setText(activity.getTitle());
                activityHolder.vContent.setText(activity.getContent());
                break;
            case CardContent.GRADE:
                GradeModule grade = (GradeModule) module;
                GradeViewHolder gradeHolder = (GradeViewHolder) holder;
                gradeHolder.vTitle.setText(grade.getTitle());
                gradeHolder.vContent.setText(grade.getContent());
                gradeHolder.vIcon.setImageDrawable(null);
                url = grade.getIconUrl();
                if (url == null) {
                    break;
                }
                if (url.endsWith("pending")) {
                    res = ContextCompat.getDrawable(context, R.drawable.ic_radio_button_unchecked);
                    gradeHolder.vIcon.setImageDrawable(res);
                } else if (url.endsWith("done")) {
                    res = ContextCompat.getDrawable(context, R.drawable.ic_circle);
                    gradeHolder.vIcon.setImageDrawable(res);
                }
                break;
            case CardContent.LOGIN:
                LoginModule login = (LoginModule) module;
                LoginViewHolder loginHolder = (LoginViewHolder) holder;
                loginHolder.vText.setText(login.getContent());
                url = login.getImgUrl();
                loginHolder.vImg.setImageDrawable(null);
                if (url.endsWith("welcome")) {
                    res = ContextCompat.getDrawable(context, R.drawable.ic_free_breakfast);
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
            case ATTENDANCE:
                View attendView = inflater.inflate(R.layout.card_attendance, parent, false);
                holder = new AttendanceViewHolder(attendView);
                break;
            case GRADE:
                View gradeView = inflater.inflate(R.layout.card_grade, parent, false);
                holder = new GradeViewHolder(gradeView);
                break;
            case ACTIVITY:
                View activityView = inflater.inflate(R.layout.card_activity, parent, false);
                holder = new ActivityViewHolder(activityView);
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

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout vLayout;

        public AttendanceViewHolder(View v) {
            super(v);
            vLayout = (LinearLayout) v.findViewById(R.id.attend_container);
        }
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected ImageView vIcon;
        protected TextView vContent;

        public GradeViewHolder(View v) {
            super(v);
            vContent = (TextView) v.findViewById(R.id.grade_content);
            vTitle = (TextView) v.findViewById(R.id.grade_title);
            vIcon = (ImageView) v.findViewById(R.id.grade_icon);
        }
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vContent;

        public ActivityViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.activity_title);
            vContent = (TextView) v.findViewById(R.id.activity_content);
        }
    }

}
