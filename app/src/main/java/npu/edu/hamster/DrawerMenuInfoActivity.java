package npu.edu.hamster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DrawerMenuInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu_info);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        String label = getIntent().getStringExtra("label");
        if (label.equals("about")) {
            String url = "http://www.npu.edu/sites/all/themes/npu/images/about/NPU-north2.jpg";
            View v = getLayoutInflater().inflate(R.layout.menu_info, null);
            Picasso.with(this).load(url).into((ImageView) v.findViewById(R.id.menu_info_image));
            viewGroup.addView(v);
        } else if (label.equals("class")) {
            View v = getLayoutInflater().inflate(R.layout.menu_message, null);
            viewGroup.addView(v);
        } else if (label.equals("event")) {
            View v = getLayoutInflater().inflate(R.layout.menu_event, null);
            viewGroup.addView(v);
        } else if (label.equals("mail")) {
            View v = getLayoutInflater().inflate(R.layout.menu_message, null);
            viewGroup.addView(v);
        }
    }
}
