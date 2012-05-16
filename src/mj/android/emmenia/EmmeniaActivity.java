package mj.android.emmenia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EmmeniaActivity extends Activity {
	
	DBConnector mDBConnector;
	Context mContext;
	ListView mListView;
	TextView mNextDate;
	TextView mCurDay;
	ImageView mIconState;
	myListAdapter mAdapter;
	Button mButStat, mButAdd;
	
	int ADD_ACTIVITY = 0;
	int UPDATE_ACTIVITY = 1;
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.w("MY", "EmmeniaActivity");
        
        mContext = this;        
        mDBConnector = new DBConnector (this);
        
        // Следующая дата
        mNextDate = (TextView)findViewById(R.id.nextDate);
        mCurDay = (TextView)findViewById(R.id.curDay);
        mIconState = (ImageView)findViewById(R.id.iconState);
        
        mCurDay.setText(getCurrentDay());
        mNextDate.setText(getNextDate());
        
        // Список
        mListView = (ListView)findViewById(R.id.list);
        mAdapter = new myListAdapter(mContext);
        mAdapter.setArrayMyData(mDBConnector.selectAll());
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        
        // Кнопки
        mButStat = (Button)findViewById(R.id.but_stat);
        mButAdd = (Button)findViewById(R.id.but_add);
        
        mButStat.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	actionStat();
            }
         });
        
        mButAdd.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	actionAdd();
            }
         });
    }
    
    private void actionStat() {
    	
    }
    
    private void actionAdd() {
    	Intent i = new Intent(mContext, AddUpdateActivity.class);
    	startActivityForResult (i, ADD_ACTIVITY);
    }
    
    private String getCurrentDay() {
    	long today = (new Date()).getTime();
        long lastDay = mDBConnector.selectMaxDate();

        if (today < lastDay || lastDay == 0)
        	return getString (R.string.no_period);
        return String.valueOf(((today - lastDay) / 1000 / 60 / 60 / 24) + 1);
    }
    
    private String getNextDate() {
        long lastDay = mDBConnector.selectMaxDate();
        int period = getPeriod ();
        Log.w("MY", "period " + period);
        if (period < 0 || lastDay < 0)
        	return getString (R.string.no_date);
        lastDay += (long)period * 24 * 60 * 60 * 1000;
        return DATE_FORMAT.format(new Date (lastDay));
    }
    
    private int getPeriod () {
    	// Настройки
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean prefAutoCalc = settings.getBoolean("autoCalc", false);
        if (prefAutoCalc)
        	return mDBConnector.selectAvg();

        Integer prefPeriod = Integer.getInteger(settings.getString("period", null));
        if (prefPeriod > 0)
        	return prefPeriod;
        return -1;    	
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
            case R.id.stat:
            	actionStat();
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
        	OneEntry md = (OneEntry) data.getExtras().getSerializable("OneEntry");
        	if (requestCode == UPDATE_ACTIVITY)
        		mDBConnector.update(md);
        	else
        		mDBConnector.insert(md);
        	updateData();
        }        
    }
    
    private void updateData () {
    	mAdapter.setArrayMyData(mDBConnector.selectAll());
    	mAdapter.notifyDataSetChanged();
    	
    	mCurDay.setText(getCurrentDay());
        mNextDate.setText(getNextDate());
    }
        
    class myListAdapter extends BaseAdapter {
    	private LayoutInflater mLayoutInflater;
    	private ArrayList<OneEntry> arrayMyData;
    	    	
    	public myListAdapter (Context ctx) {
    		mLayoutInflater = LayoutInflater.from(ctx);
    	}
    	
		public void setArrayMyData(ArrayList<OneEntry> arrayMyData) {
			this.arrayMyData = arrayMyData;
		}
    	
    	@Override
		public int getCount () {
    		return arrayMyData.size();
    	}
    		
    	@Override
		public Object getItem (int position) {
    		return position;
    	}
    		
    	@Override
		public long getItemId (int position) {
    		OneEntry md = arrayMyData.get(position);
    		if (md != null) {
    			return md.getID();
    		}
    		return 0;
    	}
    		
    	@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
   		
    		if (convertView == null)
    			convertView = mLayoutInflater.inflate(R.layout.item, null);
    		
    		ImageView vIcon = (ImageView)convertView.findViewById(R.id.Icon);
    		TextView vTitle = (TextView)convertView.findViewById(R.id.Title);
    		TextView vDate = (TextView)convertView.findViewById(R.id.Date);
    		TextView vDays = (TextView)convertView.findViewById(R.id.Days);
    		
    		 
    		
    		OneEntry md = arrayMyData.get(position);    			
   			vDate.setText(DATE_FORMAT.format(md.getDate()));
   			vTitle.setText(md.getTitle());
   			vIcon.setImageResource(md.getIcon());
   			int days = md.getDays();
   			if (days < 0)
   				vDays.setText("-");
   			else
   				vDays.setText(String.valueOf(days));
    			
    		return convertView;
    	}
    } 
}