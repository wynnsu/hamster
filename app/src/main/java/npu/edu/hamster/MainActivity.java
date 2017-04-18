package npu.edu.hamster;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import npu.edu.hamster.client.NPURestClient;
import npu.edu.hamster.client.ResponseHandler;
import npu.edu.hamster.module.ActivityModule;
import npu.edu.hamster.module.AttendanceModule;
import npu.edu.hamster.module.BaseModule;
import npu.edu.hamster.module.CourseModule;
import npu.edu.hamster.module.EventModule;
import npu.edu.hamster.module.GradeModule;
import npu.edu.hamster.module.LoginModule;
import npu.edu.hamster.module.NewsModule;

import static npu.edu.hamster.MainRecyclerViewAdapter.LOGIN_REQUEST;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<BaseModule> moduleList;
    ArrayList<BaseModule> removedList;
    private RecyclerView mainRecyclerView;
    private View mProgressView;
    private EventModule event;
    private NewsModule news;
    private LoginModule login;
    private AttendanceModule attend;
    private GradeModule grade;
    private ActivityModule activity;
    private CourseModule course;
    private boolean isLogin;
    private MainRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLogin = false;
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        moduleList = new ArrayList<>();
        removedList = new ArrayList<>();
        mProgressView = findViewById(R.id.main_progress);

        showProgress(true);

        event = new EventModule();
        news = new NewsModule();
        login = new LoginModule();
        attend = new AttendanceModule();
        grade = new GradeModule();
        activity = new ActivityModule();
        course = new CourseModule();
        adapter = new MainRecyclerViewAdapter(this, moduleList);

        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(moduleList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                BaseModule moduleRemoved = moduleList.get(position);
                removedList.add(moduleRemoved);
                moduleList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, moduleList.size());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mainRecyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        moduleList.add(news);
        moduleList.add(event);
        moduleList.add(login);
        NPURestClient.get("news", null, new ResponseHandler(this, news, moduleList.indexOf(news), adapter));
        NPURestClient.get("event", null, new ResponseHandler(this, event, moduleList.indexOf(event), adapter));
        login.setContent(getString(R.string.login_prompt));
        login.setImgUrl("lock");
        showProgress(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST && resultCode == MainRecyclerViewAdapter.LOGIN_SUCCESS) {
            login.setImgUrl("login");
            String studentID = data.getStringExtra("id");
            String base64Password = data.getStringExtra("password");
            Log.i("ID", studentID);
            Log.i("PASSWORD", base64Password);
            NPURestClient.get("student/" + studentID, null, new ResponseHandler(this, login, moduleList.indexOf(login), adapter));
            adapter.notifyItemChanged(moduleList.indexOf(login));
            moduleList.add(activity);
            moduleList.add(grade);
            moduleList.add(attend);
            NPURestClient.get("student/" + studentID + "/activity/coming", null, new ResponseHandler(this, activity, moduleList.indexOf(activity), adapter));
            NPURestClient.get("student/" + studentID + "/activity/latest", null, new ResponseHandler(this, grade, moduleList.indexOf(grade), adapter));
            NPURestClient.get("student/" + studentID + "/attendance", null, new ResponseHandler(this, attend, moduleList.indexOf(attend), adapter));
            adapter.notifyDataSetChanged();
        }
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
        if (id == R.id.action_reset) {
            Collections.reverse(removedList);
            moduleList.addAll(removedList);
            removedList.clear();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, DrawerMenuInfoActivity.class);
        if (id == R.id.nav_about) {
            intent.putExtra("label", "about");
            startActivity(intent);
        } else if (id == R.id.nav_class) {
            intent.putExtra("label", "class");
            startActivity(intent);
        } else if (id == R.id.nav_event) {
            intent.putExtra("label", "event");
            startActivity(intent);
        } else if (id == R.id.nav_mail) {
            intent.putExtra("label", "mail");
            startActivity(intent);
        } else if (id == R.id.nav_map) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            startActivity(mapIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainRecyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
