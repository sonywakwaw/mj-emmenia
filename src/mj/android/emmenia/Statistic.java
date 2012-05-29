package mj.android.emmenia;

import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


public class Statistic extends Activity {
	
	TextView titleMonth;
	GridView mMonth;
	Context mContext;
	DBConnector mDBConnector;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);
        
        mContext = this;
        EmmeniaApp mApp = ((EmmeniaApp) getApplicationContext());
        mDBConnector = mApp.getmDBConnector();
        
        titleMonth = (TextView)findViewById(R.id.title_month);
        mMonth = (GridView)findViewById(R.id.month);
        
        GridAdapter mAdapter = new GridAdapter (this, 4);
        mMonth.setAdapter(mAdapter);
        
        Calendar rightNow = Calendar.getInstance();
        long lastDay = mDBConnector.selectMaxDate();
        int period = mApp.getPeriod ();
        
        Log.w("MY", "date: " + rightNow.toString());
        Log.w("MY", "lastDay: " + lastDay);
        Log.w("MY", "period: " + period);
        
        int CountDaysInMonth = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("MY", "CountDaysInMonth: " + CountDaysInMonth);
        
        int startDay = rightNow.get(Calendar.DAY_OF_WEEK);
        Log.w("MY", "startDay: " + startDay);
    }
    
    class GridAdapter extends BaseAdapter
    {
    	private Context mContext;
    	int curMonth;
    	int CountDaysInMonth;
    	int startWeekDay;
    	Calendar rightNow;
    	String[] months, weeks;
    	
        public GridAdapter(Context context, int month)
        {
            mContext = context;
            curMonth = month;
            
            rightNow = Calendar.getInstance();
        	rightNow.set(Calendar.DAY_OF_MONTH, 1);
        	rightNow.set(Calendar.MONTH, curMonth);
        	
        	CountDaysInMonth = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
            startWeekDay = rightNow.get(Calendar.DAY_OF_WEEK);

            Resources res = getResources();
            months = res.getStringArray(R.array.months);
            weeks = res.getStringArray(R.array.day_of_month);
        }
        
        @Override
    	public int getCount() {
        	
    		return 6 * 7 + 7;
    	}

    	@Override
    	public Object getItem(int position) {
    		// TODO Auto-generated method stub
    		return null;
    	}

    	@Override
    	public long getItemId(int position) {
    		// TODO Auto-generated method stub
    		return 0;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		TextView view;
    		
            if (convertView == null)
                view = new TextView(mContext);
            else
                view = (TextView)convertView;
            
            if (position < 7)
            {
            	view.setText(weeks[position]);
            	return view;
            }
            view.setText(String.valueOf(position));
            
            return view;
    	}
    }
}
