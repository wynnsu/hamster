package npu.edu.hamster;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import npu.edu.hamster.client.NPURestClient;
import npu.edu.hamster.module.CardModule;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.NewsModule;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<CardModule> moduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView cardList = (RecyclerView) findViewById(R.id.main_recycler);
        moduleList = new ArrayList<>();

        final EventModule event = new EventModule();
        final NewsModule news = new NewsModule();
        final MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(this, moduleList);
        cardList.setAdapter(adapter);
        cardList.setLayoutManager(new LinearLayoutManager(this));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NPURestClient.get("news", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    JSONObject firstNews = (JSONObject) response.get(0);

                    news.setTitle(firstNews.getString("title"));
                    news.setImgUrl(firstNews.getString("imageUrl"));
                    news.setContent(firstNews.getString("content"));
                    news.setContentType(CardModule.ContentType.NEWS);
                    moduleList.add(news);
                    adapter.notifyDataSetChanged();
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
        });

        NPURestClient.get("event", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    JSONObject firstEvent = (JSONObject) response.get(0);
//                    EventModule event = new EventModule();

                    String[] date = firstEvent.getString("date").split("-");
                    event.setMonth(new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])]);
                    event.setDay(date[2]);
                    event.setContent(firstEvent.getString("content"));
                    event.setContentType(CardModule.ContentType.EVENT);
                    moduleList.add(event);
                    adapter.notifyDataSetChanged();

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
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
