package npu.edu.hamster.client;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;

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
                    AttendanceModule attend = (AttendanceModule) module;
                    sb = new StringBuilder();
                    for (int i = 0; i < response.length(); i++) {
                        String str =(String) response.get(i);
                        sb.append(str);
                    }
                    attend.setContent(sb.toString());
                    module = attend;
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
