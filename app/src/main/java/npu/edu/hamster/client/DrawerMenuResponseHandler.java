package npu.edu.hamster.client;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import npu.edu.hamster.R;
import npu.edu.hamster.module.CourseModule;

/**
 * Created by su153 on 4/18/2017.
 */

public class DrawerMenuResponseHandler extends JsonHttpResponseHandler {
    private Context context;
    private ListView view;
    private ViewGroup group;

    public ListView getView() {
        return view;
    }

    public void setView(ListView view) {
        this.view = view;
    }


    public DrawerMenuResponseHandler(Context context, ListView view, ViewGroup group) {
        this.context = context;
        this.view = view;
        this.group = group;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                CourseModule mod = new CourseModule();
                mod.setTitle(obj.getString("title"));
                mod.setClassroom(obj.getString("classroom"));
                mod.setInstructor(obj.getString("instructor"));
                mod.setNumber(obj.getJSONObject("id").getString("courseNumber"));
                mod.setSemester(obj.getJSONObject("id").getString("semester"));
                mod.setTime(obj.getString("time"));
                View v = view.inflate(getContext(), R.layout.menu_class_single, group);
                ((TextView) v.findViewById(R.id.menu_class_title)).setText(mod.getTitle());
                ((TextView) v.findViewById(R.id.menu_class_classroom)).setText(mod.getClassroom());
                ((TextView) v.findViewById(R.id.menu_class_instructor)).setText(mod.getInstructor());
                ((TextView) v.findViewById(R.id.menu_class_number)).setText(mod.getNumber());
                ((TextView) v.findViewById(R.id.menu_class_time)).setText(mod.getTime());
                view.addView(v);
            }
        } catch (JSONException e) {
            Log.d("HttpClient", e.getMessage());
        }
        view.deferNotifyDataSetChanged();
    }
}
