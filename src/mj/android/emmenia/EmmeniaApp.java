package mj.android.emmenia;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class EmmeniaApp extends Application {
	DBConnector mDBConnector;
    
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
}