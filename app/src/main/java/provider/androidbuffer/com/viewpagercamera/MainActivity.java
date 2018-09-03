package provider.androidbuffer.com.viewpagercamera;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import provider.androidbuffer.com.viewpagercamera.Adapter.MainAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainAdapter);
    }
}
