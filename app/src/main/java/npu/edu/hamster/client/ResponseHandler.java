package npu.edu.hamster.client;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import npu.edu.hamster.MainRecyclerViewAdapter;
import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CardContent;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

import static npu.edu.hamster.module.CardContent.EVENT;
import static npu.edu.hamster.module.CardContent.LOGIN;

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
                    login.setContent("Welcome, " + name);
                    login.setImgUrl("welcome");
                    module = login;
                    break;
                case LOGIN:
                    LoginModule login=(LoginModule)module;
//                    if(firstObject)
                    login.setContent("Click to login with your student ID and password to unlock student portal.");
                    login.setImgUrl("");
                    moduleList.add(login);
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
            switch (type) {
                case CardContent.LOGIN:
                    LoginModule login = (LoginModule) module;
                    String name = response.getString("name");
                    login.setContent("Welcome, " + name + "!");
                    login.setImgUrl("welcome");
                    Log.i("SUCCESS", ((LoginModule) module).getContent());
                    module = login;
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
        Log.i("SUCCESS", responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("HttpClient", "Failure with response: " + responseString);
        if (throwable.getCause() instanceof ConnectTimeoutException) {
            Log.d("HttpClient", throwable.getMessage());
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("HttpClient", "Failure to get response");
        if (throwable.getCause() instanceof ConnectTimeoutException) {
            Log.d("HttpClient", throwable.getMessage());
        }
    }

}
