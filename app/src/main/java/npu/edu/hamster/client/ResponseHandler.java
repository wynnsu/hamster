package npu.edu.hamster.client;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import npu.edu.hamster.MainActivity;
import npu.edu.hamster.MainRecyclerViewAdapter;
import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CardContent;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

/**
 * Created by su153 on 2/26/2017.
 */
public class ResponseHandler extends JsonHttpResponseHandler {
    private CardContent.ContentType type;
    private BaseModule module;
    private ArrayList<BaseModule> moduleList;
    private MainRecyclerViewAdapter adapter;
    private Context context;

    public ResponseHandler(Context context, CardContent.ContentType type, BaseModule module, ArrayList<BaseModule> moduleList, MainRecyclerViewAdapter adapter) {
        this.type = type;
        this.module = module;
        this.moduleList = moduleList;
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        try {
            JSONObject firstObject = response.getJSONObject(0);
            switch (type) {
                case EVENT:
                    EventModule event = (EventModule) module;
                    String[] date = firstObject.getString("date").split("-");
                    event.setMonth(new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])]);
                    event.setDay(date[2]);
                    event.setContent(firstObject.getString("content"));
                    event.setContentType(CardContent.ContentType.EVENT);
                    moduleList.add(0, event);
                    break;
                case NEWS:
                    NewsModule news = (NewsModule) module;
                    news.setTitle(firstObject.getString("title"));
                    news.setImgUrl(firstObject.getString("imageUrl"));
                    news.setContent(firstObject.getString("content"));
                    news.setContentType(CardContent.ContentType.NEWS);
                    moduleList.add(0, news);
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
            if (moduleList.size() >= 3) {
                MainActivity activity = (MainActivity) context;
                activity.showProgress(false);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d("HttpClient", e.getMessage());
        }
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
