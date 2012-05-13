package mj.android.emmenia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class EmmeniaActivity extends Activity {
	
	LinearLayout im;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}
}