package mj.android.emmenia;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private ArrayList<OneEntry> arrayMyData;
	private Context mContext;
	private EmmeniaApp mApp;
	    	
	public ListAdapter (Context ctx) {
		mContext = ctx;
		mLayoutInflater = LayoutInflater.from(mContext);
		arrayMyData = new ArrayList<OneEntry> ();
		
		mApp = ((EmmeniaApp) mContext);
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
			vDate.setText(mApp.getDateFormat().format(md.getDate()));
			vTitle.setText(md.getTitle());
			vIcon.setImageResource(mApp.getIconEmoticons((md.getIcon())));
			int days = md.getDays();
			if (days < 0)
				vDays.setText("-");
			else
				vDays.setText(String.valueOf(days));
			
		return convertView;
	}
} 