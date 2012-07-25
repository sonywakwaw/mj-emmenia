package mj.android.emmenia;

import java.text.SimpleDateFormat;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class EmmeniaApp extends Application {
	DBConnector mDBConnector;
	Integer[] imgEmoticons = {R.drawable.e01, R.drawable.e02, R.drawable.e03, R.drawable.e04, R.drawable.e05, 
			R.drawable.e06, R.drawable.e07};
	Integer[] imgStatus = {R.drawable.i_l_blue, R.drawable.i_l_green, R.drawable.i_l_red, R.drawable.i_l_yellow, 
			R.drawable.i_gray, R.drawable.i_yellow, R.drawable.i_red, R.drawable.i_green, R.drawable.i_blue};
	
	public Integer[] getImgEmoticons () {return imgEmoticons;}
	public Integer[] getImgStatus () {return imgStatus;}
	
	public Integer getIconEmoticons (int i) {return imgEmoticons[i];}
	public Integer getIconStatus (int i) {return imgStatus[i];}
	
	public SimpleDateFormat getDateFormat () {
		return new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public int getIndexEmoticons (Integer obj) {
		for (int i = 0; i < imgEmoticons.length; i++)
			if (imgEmoticons[i].equals(obj))
				return i;
		return -1;
	}
	public int getIndexStatus (Integer obj) {
		for (int i = 0; i < imgStatus.length; i++)
			if (imgStatus[i].equals(obj))
				return i;
		return -1;
	}
    
    public DBConnector getmDBConnector() {
        return mDBConnector;
    }
 
    public void connectToDataBase() {
    	mDBConnector = new DBConnector (this);
    }
    
    public int getPeriod () {
    	// Настройки
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean prefAutoCalc = settings.getBoolean("autoCalc", true);
        if (prefAutoCalc)
        	return mDBConnector.selectAvg();
        Integer prefPeriod = Integer.getInteger(settings.getString("period", null));
        if (prefPeriod != null && prefPeriod > 0)
        	return prefPeriod;
        return -1;    	
    }
    
    public String getFirstScreen () {
    	// Настройки
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = settings.getString("firstscreen", "list");
        return pref;
    }
}