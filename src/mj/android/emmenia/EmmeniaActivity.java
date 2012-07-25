package mj.android.emmenia;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EmmeniaActivity extends Activity {
	
	DBConnector mDBConnector;
	Context mContext;
	ListView mListView;
	TextView mNextDate;
	TextView mCurDay;
	TextView mPhase;
	ImageView mIconState;
	ListAdapter mListAdapter;
	Button mButStat, mButAdd;
	EmmeniaApp mApp;
	LinearLayout mCalendar;
	ListView mList;
	
	TextView titleMonth;
	GridView mMonth;
	String [] weekDays, months;
	Button toLeft, toRight;
	Calendar rightNow;
	GridAdapter mGridAdapter;
	
	enum ScreenView { SV_LIST, SV_CALENDAR }; 
	ScreenView CURRENT_VIEW;
	
	int ADD_ACTIVITY = 0;
	int UPDATE_ACTIVITY = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = getApplicationContext();
        mApp = ((EmmeniaApp) getApplicationContext());
        mApp.connectToDataBase();
        
        Resources res = getResources();
        months = res.getStringArray(R.array.months);
        weekDays = res.getStringArray(R.array.day_of_month);
        
        mDBConnector = mApp.getmDBConnector();
        
        // Следующая дата
        mNextDate = (TextView)findViewById(R.id.nextDate);
        mCurDay = (TextView)findViewById(R.id.curDay);
        mPhase = (TextView)findViewById(R.id.phase);
        mIconState = (ImageView)findViewById(R.id.iconState);
        
        // Список
        mListView = (ListView)findViewById(R.id.list);
        mListAdapter = new ListAdapter(mContext);
        mListView.setAdapter(mListAdapter);
        registerForContextMenu(mListView);
        
        // Кнопки
        mButStat = (Button)findViewById(R.id.but_stat);
        mButAdd = (Button)findViewById(R.id.but_add);
        
        // Текущее представление - список
        mCalendar = (LinearLayout)findViewById(R.id.calendar);
    	mList = (ListView)findViewById(R.id.list);
    	
    	if (mApp.getFirstScreen().equalsIgnoreCase("list")) {
	        CURRENT_VIEW = ScreenView.SV_LIST;
	        mButStat.setBackgroundResource(R.drawable.b_stat);
	        mList.setVisibility(View.VISIBLE);
			mCalendar.setVisibility(View.INVISIBLE);
    	}
    	else if (mApp.getFirstScreen().equalsIgnoreCase("calendar"))  {
    		CURRENT_VIEW = ScreenView.SV_CALENDAR;
	        mButStat.setBackgroundResource(R.drawable.b_list);
	        mList.setVisibility(View.INVISIBLE);
			mCalendar.setVisibility(View.VISIBLE);    		
    	}
        
        mButStat.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	SwitchCurrentView();
            }
         });
        
        mButAdd.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	actionAdd();
            }
         });
        
        // Календарь
        titleMonth = (TextView)findViewById(R.id.titleMonth);
        mMonth = (GridView)findViewById(R.id.grid);
        
        rightNow = Calendar.getInstance();
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        int currMonth = rightNow.get(Calendar.MONTH);
        int currYear = rightNow.get(Calendar.YEAR);
        
        mGridAdapter = new GridAdapter (this, currMonth, currYear);
        mMonth.setAdapter(mGridAdapter);
        
        titleMonth.setText(months[currMonth] + ", " + String.valueOf(currYear));
        
        toLeft = (Button)findViewById(R.id.toLeft);
        toLeft.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	rightNow.add(Calendar.MONTH, -1);
            	
            	int currMonth = rightNow.get(Calendar.MONTH);
                int currYear = rightNow.get(Calendar.YEAR);
                
                mGridAdapter.setDate(currMonth, currYear);
                mGridAdapter.notifyDataSetChanged();
            	
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
                
                mGridAdapter.setDate(currMonth, currYear);
                mGridAdapter.notifyDataSetChanged();
            	
            	titleMonth.setText(months[currMonth] + ", " + String.valueOf(currYear));
            }
         });
   
        updateData();
    }
    
    private void SwitchCurrentView() {
    	
    	switch (CURRENT_VIEW) {
    		case SV_LIST:
    			mList.setVisibility(View.INVISIBLE);
    			mCalendar.setVisibility(View.VISIBLE);
    			mButStat.setBackgroundResource(R.drawable.b_list);  			
    			CURRENT_VIEW = ScreenView.SV_CALENDAR;
    			break;
    		case SV_CALENDAR:
    			mList.setVisibility(View.VISIBLE);
    			mCalendar.setVisibility(View.INVISIBLE);
    			mButStat.setBackgroundResource(R.drawable.b_stat); 
    			CURRENT_VIEW = ScreenView.SV_LIST;
    			break;    	
    	}
    }
    
    private void actionAdd() {
    	Intent i = new Intent(mContext, AddUpdateActivity.class);
    	startActivityForResult (i, ADD_ACTIVITY);
    }
    
    private int getCurrentDay() {
    	long today = (new Date()).getTime();
        long lastDay = mDBConnector.selectMaxDate();

        if (today < lastDay || lastDay == 0)
        	return -1;
        return (int)((today - lastDay) / 1000 / 60 / 60 / 24) + 1;
    }
    
    private String getNextDate() {
        long lastDay = mDBConnector.selectMaxDate();
        int period = mApp.getPeriod ();
        if (period < 0 || lastDay < 0)
        	return getString (R.string.no_date);
        lastDay += (long)period * 24 * 60 * 60 * 1000;
        return mApp.getDateFormat().format(new Date (lastDay));
    }
    
    @Override 
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()) {
    	case R.id.edit:
    		Intent i = new Intent(mContext, AddUpdateActivity.class);
    		OneEntry md = mDBConnector.select(info.id);
    		i.putExtra("OneEntry", md);
        	startActivityForResult (i, UPDATE_ACTIVITY);
        	break;
    	case R.id.delete:
    		mDBConnector.delete (info.id);
    		break;
    	default:
    		return super.onContextItemSelected(item);
    	}
    	updateData();
		return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; 
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
	        case R.id.add:
	        	actionAdd();
            	break;
            case R.id.settings:
            	startActivity(new Intent(mContext, Preferences.class));
            	break;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        updateData();
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (resultCode == Activity.RESULT_OK) {
        	updateData();
        }        
    }
    
    private void updateData () {
    	mListAdapter.setArrayMyData(mDBConnector.selectAll());
    	mListAdapter.notifyDataSetChanged();
    	
    	mGridAdapter.notifyDataSetChanged();
    	
    	int day = getCurrentDay();
        
        if (day > 0)
        	mCurDay.setText(String.valueOf(day));
        mNextDate.setText(getNextDate());
        
        Pair<String, Integer> pair = mDBConnector.selectPhaseName(day);
        mPhase.setText(pair.first);
        if (pair.second > 0)
        	mIconState.setImageResource(mApp.getIconStatus(pair.second));
        else
        	mIconState.setImageResource(android.R.color.transparent);
    }
}
