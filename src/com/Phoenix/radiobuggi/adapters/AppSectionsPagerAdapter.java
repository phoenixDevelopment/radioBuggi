package com.Phoenix.radiobuggi.adapters;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.Phoenix.radiobuggi.*;
import com.Phoenix.radiobuggi.R.id;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter{
	
	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		 switch (i) {
         case 0:
             // The first section of the app is the most interesting -- it offers
             // a launchpad into the other demonstrations in this example application.
        	 Log.i("Fragment index @ 0", String.valueOf(i));
             fragment =  new MainFragment();
             break;
         case 1  : Log.i("Fragment index @ 1 ", String.valueOf(i)); 
        	 fragment =  new AboutFragment();
        	 break;
         case 2 : Log.i("Fragment index @ 2 ", String.valueOf(i)); 
    	 fragment =  new AboutFragment(); 
        	 fragment = new ContactFragment();
         	break;
         	default : Log.i("Fragment index @ Default ", String.valueOf(i)); 
       	 fragment =  new AboutFragment();
         		fragment = new MainFragment();
         	break;
		 }
		 return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	public CharSequence getPageTitle(int position) {
		String title = "";
        switch(position){
        case 0 : title =  "Listen Live";
        	break;
        case 1 : title =  "About Us";
        	break;
        case 2 : title =  "Contact us";
        break;
        default : 
        }
		return title;
    }
	
	
	public static class MainFragment extends Fragment{

		private  AsyncPlayer player = new AsyncPlayer("RadioBuggi");
		private  boolean playing;
		//private AdView adview;
		private AudioManager audioManager;
		private SeekBar volume;
		private Chronometer playTime;
		private String currentPlay= "";
		private long elapsedTime = 0;
		//private final  String MY_AD_UNIT_ID = "ca-app-pub-2566943455372229/6969833557";
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			 View rootView = inflater.inflate(R.layout.fragment_main, container,false);
			 AdRequest adrequest = new AdRequest();
				//adrequest.addTestDevice(AdRequest.TEST_EMULATOR);
				//adview = new AdView(this.getActivity(),AdSize.BANNER,MY_AD_UNIT_ID);
		        AdRequest request = new AdRequest();
		       // LinearLayout adsLayout = (LinearLayout)rootView.findViewById(R.id.ads);
		       // adview.loadAd(request);
		       // adsLayout.addView(adview);
		       // Log.i("Ads", "Ad Loaded");
		        
		        playTime = (Chronometer)rootView.findViewById(R.id.timeElapsed);
				 playTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
					
					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub
						long minutes=((SystemClock.elapsedRealtime()-playTime.getBase())/1000)/60;
						long seconds=((SystemClock.elapsedRealtime()-playTime.getBase())/1000)%60;
						if(minutes <=9){
							currentPlay = "0"+minutes;
						}if(seconds <=9){
							currentPlay = minutes+":0"+seconds;
						}else{
						currentPlay=minutes+":"+seconds;
						}
						chronometer.setText(currentPlay);
						elapsedTime=SystemClock.elapsedRealtime();
					}
				});
				 
			 try {
				parsePlaylist();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 volume = (SeekBar)rootView.findViewById(R.id.volumeBar);
			 audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
			 volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			 volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			 volume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				}
			});
			 
			 final Button play = (Button)rootView.findViewById(R.id.PlayRadio);
			 play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!playing){
						player.play(getActivity().getApplicationContext(), Uri.parse("http://stream.audionow.com:8000/RadioBuggi"), false, AudioManager.STREAM_MUSIC);
						playing = true;
						playTime.setBase(SystemClock.elapsedRealtime());
						playTime.start();
					} 
				}
			});
			 
			 Button stop = (Button)rootView.findViewById(R.id.stopRadio);
			 stop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(playing){
						player.stop();
						playing = false;
						playTime.stop();
						currentPlay = "00:00";
						playTime.setText(currentPlay);
					}
				}
			});
			 return rootView;
		}
		
		private void parsePlaylist() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
			Context con = getActivity().getApplicationContext();
			player.play(con, Uri.parse("http://stream.audionow.com:8000/RadioBuggi"), false, AudioManager.STREAM_MUSIC);
			playing = true;
			playTime.setBase(SystemClock.elapsedRealtime());
			playTime.start();
			Log.i("Async player", "Media Played");		
		}
		
		@Override
		public void onStop(){
			Log.i("OnStop", "In MainFragment");
			if(this.playing){
				player.stop();
				playing=false;
				playTime.stop();
				currentPlay = "00:00";
				playTime.setText(currentPlay);
			}
			super.onStop();
		}
		
		@Override
		public void onResume(){
			Log.i("OnResume", "In Main Fragment");
			if(!playing){
				player.play(getActivity().getApplicationContext(), Uri.parse("http://stream.audionow.com:8000/RadioBuggi"), false, AudioManager.STREAM_MUSIC);
				playing = true;
				playTime.setBase(SystemClock.elapsedRealtime());
				playTime.start();
			}
			super.onResume();
		}
		
		@Override
		public void onPause(){
			Log.i("OnPause", "In Main Fragment");
			if(!playing){
				player.play(getActivity().getApplicationContext(), Uri.parse("http://stream.audionow.com:8000/RadioBuggi"), false, AudioManager.STREAM_MUSIC);
				playing = true;
				playTime.setBase(SystemClock.elapsedRealtime());
				playTime.start();
			}
			super.onPause();
		}
		
	}
	
	public static class AboutFragment extends Fragment{
		
		//private AdView adview;
		//private final  String MY_AD_UNIT_ID = "ca-app-pub-2566943455372229/6969833557";
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			 View rootView = inflater.inflate(R.layout.fragment_about, container,false);
			 TextView text = (TextView)rootView.findViewById(R.id.about);
			 text.setText("This  is  About Page");
			 AdRequest adrequest = new AdRequest();
				//adrequest.addTestDevice(AdRequest.TEST_EMULATOR);
			//	adview = new AdView(this.getActivity(),AdSize.BANNER,MY_AD_UNIT_ID);
		        AdRequest request = new AdRequest();
		      //  LinearLayout adsLayout = (LinearLayout)rootView.findViewById(R.id.ads);
		      //  adview.loadAd(request);
		      //  adsLayout.addView(adview);
		      //  Log.i("Ads", "Ad Loaded");
			 return rootView;
		}
	}
	
	public static class ContactFragment extends Fragment{
		
		//private AdView adview;
		//private final  String MY_AD_UNIT_ID = "ca-app-pub-2566943455372229/6969833557";
		private Button send;
		private EditText subject;
		private EditText message;
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			 View rootView = inflater.inflate(R.layout.fragment_contact, container,false);
			 send = (Button)rootView.findViewById(R.id.buttonSend);
			 subject = (EditText)rootView.findViewById(R.id.editTextSubject);
			 message = (EditText)rootView.findViewById(R.id.editTextMessage);
			 
			 send.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@radiobuggi.ca"});
					email.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
					email.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email, "Choose Email Client : "));
					resetFields();
				}
			});
			 
			// AdRequest adrequest = new AdRequest();
				//adrequest.addTestDevice(AdRequest.TEST_EMULATOR);
				//adview = new AdView(this.getActivity(),AdSize.BANNER,MY_AD_UNIT_ID);
		        AdRequest request = new AdRequest();
		       // LinearLayout adsLayout = (LinearLayout)rootView.findViewById(R.id.ads);
		       // adview.loadAd(request);
		       // adsLayout.addView(adview);
		       // Log.i("Ads", "Ad Loaded");
			 return rootView;
		}
		
		private void resetFields(){
			subject.setText("");
			message.setText("");
		}
	}
}
