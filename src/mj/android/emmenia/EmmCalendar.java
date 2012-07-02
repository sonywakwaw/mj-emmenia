package mj.android.emmenia;

import java.io.Serializable;
import java.util.Calendar;

import android.content.Context;

public class EmmCalendar implements Serializable{
	
	private Calendar today;
	private Calendar firstDay;
	private Calendar lastDay;
	private int currMonth;
	private int currYear;
	private Day[] calendarDays;
	EmmeniaApp mApp;
	DBConnector mDBConnector;
	Context mContext;
	
	Calendar [] dates;
	
	private static int COUNT_DAYS = 7 * 6;
	
	public EmmCalendar (Context context) {
		
		mContext = context;
		
		// Сегодня
		today = Calendar.getInstance();
		today.set(Calendar.AM_PM, Calendar.AM);
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
		// Текущий месяц и год
		currMonth = today.get(Calendar.MONTH);
		currYear = today.get(Calendar.YEAR);
		
		mApp = ((EmmeniaApp) mContext.getApplicationContext());
        mDBConnector = mApp.getmDBConnector();        
        
		generateFirstDay ();
		
		//dates = mDBConnector.selectNearDates(firstDay.getTime().getTime(), lastDay.getTime().getTime());
		//generateDays ();
	}
	
	private void generateFirstDay () {
		firstDay = (Calendar)today.clone();
		firstDay.set(Calendar.DAY_OF_MONTH, 1);
		firstDay.set(Calendar.MONTH, currMonth);
		firstDay.set(Calendar.YEAR, currYear);
		firstDay.add(Calendar.DAY_OF_MONTH, (-1) * toLocale(firstDay.get(Calendar.DAY_OF_WEEK)));
		
		lastDay = (Calendar)firstDay.clone();
		lastDay.set(Calendar.AM_PM, Calendar.PM);
		lastDay.set(Calendar.HOUR, 11);
		lastDay.set(Calendar.MINUTE, 59);
		lastDay.set(Calendar.SECOND, 59);
		lastDay.add(Calendar.DAY_OF_MONTH, COUNT_DAYS - 1);
	}
	
	private void generateDays () {
		Calendar currDay = (Calendar) firstDay.clone();
		for (int i = 0; i < COUNT_DAYS; i++) {
			calendarDays[i] = new Day (currDay, 0);
		}
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
}