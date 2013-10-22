package com.Phoenix.radiobuggi;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.Window;

public class Splash extends Activity {

	private boolean running;
	private int progress;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		// loader = (ProgressWheel)findViewById(R.id.pw_spinner);
		 
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					progress = 0;
					running = true;
					//loader.resetCount();
					while(progress < 361){
						//loader.incrementProgress();
						progress++;
						try {
							Thread.sleep(14);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					running = false;
					if(!running){
						Intent main = new Intent(Splash.this,RadioBuggi.class);
						main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(main);
					}
				};
			}).start();		
	}


}
