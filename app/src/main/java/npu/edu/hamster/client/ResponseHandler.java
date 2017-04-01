package npu.edu.hamster.client;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import npu.edu.hamster.MainRecyclerViewAdapter;
import npu.edu.hamster.R;
import npu.edu.hamster.module.ActivityModule;
import npu.edu.hamster.module.AttendanceModule;
import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CardContent;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.GradeModule;
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

/**
 * Created by su153 on 2/26/2017.
 */
public class ResponseHandler extends JsonHttpResponseHandler {
    private BaseModule module;
    private MainRecyclerViewAdapter adapter;
    private Context context;
    private int position;

    public ResponseHandler(Context context, BaseModule module, int position, MainRecyclerViewAdapter adapter) {
        this.module = module;
        this.adapter = adapter;
        this.context = context;
        this.position = position;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.i("SUCCESS", "array returned");
        try {
            Log.i("SUCCESS", Integer.toString(response.length()));
            JSONObject firstObject = response.getJSONObject(0);
            int type = module.getType();
            StringBuilder sb;
            switch (type) {
                case CardContent.EVENT:
                    EventModule event = (EventModule) module;
                    String[] date = firstObject.getString("date").split("-");
                    event.setMonth(new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1]);
                    event.setDay(date[2]);
                    event.setContent(firstObject.getString("content"));
                    module = event;
                    break;
                case CardContent.NEWS:
                    NewsModule news = (NewsModule) module;
                    news.setTitle(firstObject.getString("title"));
                    news.setImgUrl(firstObject.getString("imageUrl"));
                    news.setContent(firstObject.getString("content"));
                    module = news;
                    break;
                case CardContent.LOGIN:
                    LoginModule login = (LoginModule) module;
                    String name = firstObject.getString("name");
                    login.setContent(context.getString(R.string.login_success) + name);
                    login.setImgUrl("welcome");
                    module = login;
                    break;
                case CardContent.ATTENDANCE:
                    Map<String,String> map=new HashMap<>();
                    AttendanceModule attend = (AttendanceModule) module;
                    for (int i = 0; i < response.length(); i++) {
                        String title=response.getJSONObject(i).getString("title");
                        String attends=response.getJSONObject(i).getString("attendance");
                        map.put(title,attends);
                    }
                    attend.setAttendanceMap(map);
                    module = attend;
                    break;
                case CardContent.ACTIVITY:
                    ActivityModule activity = (ActivityModule) module;
                    long dateMilli = firstObject.getLong("due");
                    Calendar cal = Calendar.getInstance();
                    long dateNow = cal.getTimeInMillis();
                    long diff = dateMilli - dateNow;

                    cal.setTimeInMillis(dateMilli);
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));

                    sb = new StringBuilder();
                    int days = (int) (diff / (24 * 60 * 60 * 1000));
                    if (days > 7) {
                        sb.append("More than 1 week");
                    } else {
                        sb.append(days + " days left");
                    }
                    Log.i("COMING EVENT CARD", sb.toString());
                    activity.setTitle("Due: "+firstObject.getString("title"));
                    activity.setContent(sb.toString());
                    module = activity;
                    break;
                case CardContent.GRADE:
                    GradeModule grade = (GradeModule) module;
                    Log.i("GRADE", "Got grades");
                    sb = new StringBuilder();
                    sb.append("Week ");
                    sb.append(firstObject.getInt("week"));
                    grade.setTitle(sb.toString());
                    double points = (double) firstObject.get("points");
                    double total = (double) firstObject.get("total");
                    sb = new StringBuilder();
                    sb.append(firstObject.getString("title"));
                    sb.append(" ");
                    sb.append(points + "/" + total);
                    grade.setContent(sb.toString());
                    if (Double.compare(points, 0.0) == 0) {
                        grade.setIconUrl("pending");
                    } else {
                        grade.setIconUrl("done");
                    }
                    module = grade;
                    break;
                default:
                    break;
            }
            adapter.notifyItemChanged(position);
        } catch (JSONException e) {
            Log.d("HttpClient", e.getMessage());
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int type = module.getType();
            StringBuilder sb;
            switch (type) {
                case CardContent.LOGIN:
                    LoginModule login = (LoginModule) module;
                    String name = response.getString("name");
                    login.setContent("Welcome, " + name + "!");
                    login.setImgUrl("welcome");
                    Log.i("SUCCESS", ((LoginModule) module).getContent());
                    module = login;
                    break;
                case CardContent.ACTIVITY:
                    ActivityModule activity = (ActivityModule) module;
                    sb = new StringBuilder();
                    sb.append(response.getString("due"));
                    activity.setContent(sb.toString());
                    module = activity;
                    break;
                default:
                    break;
            }
            adapter.notifyItemChanged(position);
        } catch (JSONException e) {
            Log.d("HttpClient", e.getMessage());
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.e("HttpClient", "Success with String: " + responseString);

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("HttpClient", "Failure with response: " + responseString);
        int type = module.getType();
        switch (type) {
            case CardContent.GRADE:
                GradeModule grade = (GradeModule) module;
                grade.setContent(responseString);
                module = grade;
                break;
            default:
                break;
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("HttpClient", "Failure to get response");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        Log.e("HttpClient", "Failure to get response");
    }
}
