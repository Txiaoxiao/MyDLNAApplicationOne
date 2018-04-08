package com.example.asus.mydlnaapplicationone;

import android.net.wifi.WifiManager;
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

import com.example.asus.mydlnaapplicationone.GlobalVariables.Globals;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView navigation;
    List<Fragment> listFragment;
    ImagesFragment imageFragment;

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                //position is current page
                //call this method when finish scrolling.

                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //position: current page

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state = 1: scrolling
                //state = 2: finish scrolling
                //state = 0: done nothing
            }
    };

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
               /* case R.id.navigation_console:
                    viewPager.setCurrentItem(2);
                    return true;*/
                case R.id.navigation_setting:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Globals globalVariables = Globals.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock = wifiManager.createMulticastLock("multicastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();
        viewPager =
                (ViewPager)(findViewById(R.id.viewpager));
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        initViewPager();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);
    }



    private void initViewPager() {
        listFragment = new ArrayList<>();
        listFragment.add(new DeviceFragment());
        listFragment.add(new ContentFragment());
        //listFragment.add(new ConsoleFragment());
        listFragment.add(new SettingFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),this,listFragment);
        viewPager.setAdapter(adapter);
    }

}

