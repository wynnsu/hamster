package npu.edu.hamster.client;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by su153 on 2/19/2017.
 */

public class NPURestClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/cs595/api/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("HttpClient","Start getting data from"+getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
        Log.d("HttpClient","Finish getting data: "+responseHandler.toString());
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}