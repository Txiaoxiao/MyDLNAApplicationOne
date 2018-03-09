package com.example.asus.mydlnaapplicationone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.asus.mydlnaapplicationone.Adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView navigation;
    List<Fragment> listFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_device:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_content:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_console:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_setting:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)(findViewById(R.id.viewpager));
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        initViewPager();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private void initViewPager() {
        listFragment = new ArrayList<>();
        listFragment.add(new DeviceFragment());
        listFragment.add(new ContentFragment());
        listFragment.add(new ConsoleFragment());
        listFragment.add(new SettingFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),this,listFragment);
        viewPager.setAdapter(adapter);
    }

}

