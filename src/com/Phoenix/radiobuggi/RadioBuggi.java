package com.Phoenix.radiobuggi;

import java.io.IOException;

import com.Phoenix.radiobuggi.*;
import com.Phoenix.radiobuggi.adapters.AppSectionsPagerAdapter;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.R;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;
import android.app.ActionBar;

@SuppressLint("NewApi")
public class RadioBuggi extends FragmentActivity implements ActionBar.TabListener{
	
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ViewPager mViewPager;
	private PowerManager pm ;
	private WakeLock wakeLock ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.Phoenix.radiobuggi.R.layout.main);
        this.pm = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Radio Running");
        this.wakeLock.acquire();
                
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
     // Set up the action bar.
        final ActionBar actionBar = getActionBar();
     // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
     // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(com.Phoenix.radiobuggi.R.id.radio_pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        	
        	@Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
     // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		 mViewPager.setCurrentItem(tab.getPosition());
	}




	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
	    // TODO Auto-generated method stub
	    this.finish();
	    super.onBackPressed();
	}
	
	@Override
	public void onPause(){
		Log.i("OnPause", "In Main");
		super.onPause();
		if(this.wakeLock.isHeld()){
			this.wakeLock.release();
		}
	}
	
	@Override
	public void onStop(){
		Log.i("OnStop", "In Main");
		if(this.wakeLock.isHeld()){
			this.wakeLock.release();
		}
		super.onStop();
	}
	
	@Override
	public void onResume(){
		Log.i("OnResume", "In Main");
		if(!this.wakeLock.isHeld()){
			this.wakeLock.acquire();
		}
		super.onResume();
	}
}
