package mj.android.emmenia;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

class GridAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;  
	private int currMonth, currYear;
	String [] weekDays;
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
        
        Resources res = context.getResources();
        weekDays = res.getStringArray(R.array.day_of_month);
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
	    //ImageView phase = (ImageView)convertView.findViewById(R.id.phase);
	    
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
        
        
        
        /*if (position == 12)
        	phase.setImageResource(R.drawable.i_green);
        if (position == 13)
        	phase.setImageResource(R.drawable.i_l_green);
        if (position == 24)
        	phase.setImageResource(R.drawable.i_red);
        if (position == 28)
        	phase.setImageResource(R.drawable.i_yellow);
        */
        
        date.add(Calendar.DAY_OF_MONTH, 1);
                    
        return convertView;
	}
}