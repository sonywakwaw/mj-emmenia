package mj.android.emmenia;

import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Statistic extends Activity {
	
	TextView titleMonth;
	GridView mMonth;
	Context mContext;
	String [] weekDays, months;
	Button toLeft, toRight;
	Calendar rightNow;
	GridAdapter mAdapter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);
        
        mContext = this;
        Resources res = getResources();
        months = res.getStringArray(R.array.months);
        weekDays = res.getStringArray(R.array.day_of_month);
        
        titleMonth = (TextView)findViewById(R.id.titleMonth);
        mMonth = (GridView)findViewById(R.id.calendar);
        
        rightNow = Calendar.getInstance();
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        int currMonth = rightNow.get(Calendar.MONTH);
        int currYear = rightNow.get(Calendar.YEAR);
        
        mAdapter = new GridAdapter (this, currMonth, currYear);
        mMonth.setAdapter(mAdapter);
        
        titleMonth.setText(months[currMonth] + ", " + String.valueOf(currYear));
        
        toLeft = (Button)findViewById(R.id.toLeft);
        toLeft.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	rightNow.add(Calendar.MONTH, -1);
            	
            	int currMonth = rightNow.get(Calendar.MONTH);
                int currYear = rightNow.get(Calendar.YEAR);
                
            	mAdapter.setDate(currMonth, currYear);
            	mAdapter.notifyDataSetChanged();
            	
            	titleMonth.setText(months[currMonth] + ", " + String.valueOf(currYear));
            }
         });
        
        toRight = (Button)findViewById(R.id.toRight);
        toRight.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	rightNow.add(Calendar.MONTH, 1);
            	
            	int currMonth = rightNow.get(Calendar.MONTH);
                int currYear = rightNow.get(Calendar.YEAR);
                
            	mAdapter.setDate(currMonth, currYear);
            	mAdapter.notifyDataSetChanged();
            	
            	titleMonth.setText(months[currMonth] + ", " + String.valueOf(currYear));
            }
         });
    }
    
    private int toLocale (int week) {
    	switch (week) {
    		case Calendar.MONDAY:
    			return 0;
    		case Calendar.TUESDAY:
    			return 1;
    		case Calendar.WEDNESDAY:
    			return 2;
    		case Calendar.THURSDAY:
    			return 3;
    		case Calendar.FRIDAY:
    			return 4;
    		case Calendar.SATURDAY:
    			return 5;
    		case Calendar.SUNDAY:
    			return 6;
    	}
    	return -1;
    }
    
    class GridAdapter extends BaseAdapter
    {
    	private Context mContext;
    	private LayoutInflater mLayoutInflater;  
    	private int currMonth, currYear;
    	Calendar date;
    	Calendar rightNow;
    	
        public GridAdapter(Context context, int month, int year)
        {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext); 
            
            currMonth = month;
            currYear = year;
            
            date = Calendar.getInstance();
            rightNow = (Calendar) date.clone();
            setDate (currMonth, currYear);
            
            EmmCalendar ec = new EmmCalendar(mContext);
        }
        
        public void setDate (int month, int year) {
        	date.set(Calendar.DAY_OF_MONTH, 1);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.YEAR, year);
            
            int dw = date.get(Calendar.DAY_OF_WEEK);
            date.add(Calendar.DAY_OF_MONTH, (-1) * toLocale(dw));
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
    		
    		if (convertView == null)  
    	        convertView = mLayoutInflater.inflate(R.layout.item_calendar, null);  
    		
    	    TextView view = (TextView)convertView.findViewById(R.id.item);  
    	    LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout);
    	    ImageView phase = (ImageView)convertView.findViewById(R.id.phase);
    	    
            if (position < 7)
            {
            	view.setText(weekDays[position]);
            	if (position > 4)
            		layout.setBackgroundResource(R.drawable.weekend);
            	else
            		layout.setBackgroundResource(R.drawable.workday);
            	return convertView;
            }
                        
            int day = date.get(Calendar.DAY_OF_MONTH);
            int month = date.get(Calendar.MONTH);
            
            view.setText(String.valueOf(day));
            if (month == currMonth) {
	            if ((position - 5)%7 == 0 || (position - 6)%7 == 0)
	            	layout.setBackgroundResource(R.drawable.weekend_day);
	            else
	            	layout.setBackgroundResource(R.drawable.workday_day);
	            
	            if (date.compareTo(rightNow) == 0)
	            	layout.setBackgroundResource(R.drawable.current_day);
            }
            else
            	layout.setBackgroundResource(R.drawable.not_month);
            
            if (position == 12)
            	phase.setImageResource(R.drawable.i_green);
            if (position == 13)
            	phase.setImageResource(R.drawable.i_l_green);
            if (position == 24)
            	phase.setImageResource(R.drawable.i_red);
            if (position == 28)
            	phase.setImageResource(R.drawable.i_yellow);
            
            
            date.add(Calendar.DAY_OF_MONTH, 1);
                        
            return convertView;
    	}
    }
}
