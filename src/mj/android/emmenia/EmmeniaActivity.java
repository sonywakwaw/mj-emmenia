package mj.android.emmenia;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class EmmeniaActivity extends Activity {
	
	LinearLayout im;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		im = (LinearLayout) findViewById(R.id.splashscreen); 
		
		Animation a = AnimationUtils.loadAnimation(this, R.anim.alpha);
		a.reset();
		a.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation a) {
				//
			}
			public void onAnimationRepeat(Animation a) {
				//
			}
			public void onAnimationEnd(Animation a) {
				//im.setBackgroundResource(R.drawable.rrr);
			}
		});

		im.startAnimation(a);
	}
}