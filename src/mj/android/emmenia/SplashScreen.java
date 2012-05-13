package mj.android.emmenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
	
	LinearLayout im;
	Context mContext;
	
	protected boolean ACTIVE = true;
	protected int SPLASH_TIME = 5000; // time to display the splash screen in ms


	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		mContext = this;
		
		im = (LinearLayout) findViewById(R.id.splashscreen); 
		
		Animation a = AnimationUtils.loadAnimation(this, R.anim.alpha);
		a.reset();
		im.startAnimation(a);
		
		// thread for displaying the SplashScreen
	    Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                int waited = 0;
	                while(ACTIVE && (waited < SPLASH_TIME)) {
	                    sleep(100);
	                    if(ACTIVE) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	                // do nothing
	            } finally {
	                finish();
	                startActivity(new Intent(mContext, EmmeniaActivity.class));
	                stop();
	            }
	        }
	    };
	    splashTread.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	ACTIVE = false;
	    }
	    return true;
	}
}