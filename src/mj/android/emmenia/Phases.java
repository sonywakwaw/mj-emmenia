package mj.android.emmenia;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Phases extends Activity {
	
	DBConnector mDBConnector;
	Context mContext;
	ListView mListView;
	myListAdapter mAdapter;
	Button mButAdd;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phase);
        
        mContext = this;
        EmmeniaApp mApp = ((EmmeniaApp) getApplicationContext());
        mApp.connectToDataBase();
        
        mDBConnector = mApp.getmDBConnector();
        
        // Список
        mListView = (ListView)findViewById(R.id.list);
        mAdapter = new myListAdapter(mContext);
        mAdapter.setArrayMyData(mDBConnector.selectAllPhase());
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        
        // Кнопки
        mButAdd = (Button)findViewById(R.id.but_add);
        
        mButAdd.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	actionAdd();
            }
         });
    }
    
    private void actionAdd() {
    	Intent i = new Intent(mContext, AddPhases.class);
    	startActivityForResult (i, 0);
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
    		Intent i = new Intent(mContext, AddPhases.class);
    		OnePhase op = mDBConnector.selectPhase(info.id);
    		i.putExtra("OnePhase", op);
        	startActivityForResult (i, 0);
        	break;
    	case R.id.delete:
    		mDBConnector.deletePhase (info.id);
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
        inflater.inflate(R.menu.phase_menu, menu);
        return true; 
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
	        case R.id.add:
	        	actionAdd();
            	break;
	        case R.id.del:
	        	mDBConnector.deleteAllPhase ();
            	break;
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
    	mAdapter.setArrayMyData(mDBConnector.selectAllPhase());
    	mAdapter.notifyDataSetChanged();
    }
        
    class myListAdapter extends BaseAdapter {
    	private LayoutInflater mLayoutInflater;
    	private ArrayList<OnePhase> arrayMyData;
    	    	
    	public myListAdapter (Context ctx) {
    		mLayoutInflater = LayoutInflater.from(ctx);
    	}
    	
		public void setArrayMyData(ArrayList<OnePhase> arrayMyData) {
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
    		OnePhase op = arrayMyData.get(position);
    		if (op != null) {
    			return op.getID();
    		}
    		return 0;
    	}
    		
    	@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
   		
    		if (convertView == null)
    			convertView = mLayoutInflater.inflate(R.layout.item_phase, null);
    		
    		ImageView vIcon = (ImageView)convertView.findViewById(R.id.Icon);
    		TextView vPeriod = (TextView)convertView.findViewById(R.id.Period);
    		TextView vDesc = (TextView)convertView.findViewById(R.id.Desc);
    		
    		OnePhase op = arrayMyData.get(position);    			

    		vIcon.setImageResource(op.getIcon());
    		vDesc.setText(op.getDesc());
    		
    		String period = "с " + op.getFrom() + " по " + op.getTo();
    		vPeriod.setText(period);
    			
    		return convertView;
    	}
    } 
}
