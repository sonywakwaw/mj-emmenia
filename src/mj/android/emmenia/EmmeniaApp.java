package mj.android.emmenia;

import android.app.Application;

public class EmmeniaApp extends Application {
	DBConnector mDBConnector;
    
    public DBConnector getmDBConnector() {
        return mDBConnector;
    }
 
    public void connectToDataBase() {
    	mDBConnector = new DBConnector (this);
    }
}