package mj.android.emmenia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Preferences extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
	CheckBoxPreference autoCalc;
	EditTextPreference period;
	Preference additional;
	Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        
        Log.w("MY", "Preferences");
        
        mContext = this;
        
        autoCalc = (CheckBoxPreference)this.findPreference("autoCalc");
        period = (EditTextPreference)this.findPreference("period");
        additional = this.findPreference("additional");
        
        // слушатель
        autoCalc.setOnPreferenceChangeListener(this);
        period.setOnPreferenceChangeListener(this);
        
        // пишем в summary текущее значение
        period.setSummary(period.getText() + " (дн.)");
        
        additional.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
			public boolean onPreferenceClick(Preference preference) {
            	Intent i = new Intent(mContext, Phases.class);
            	startActivityForResult (i, 0);
            	return true;
            }
        });
    }
    
    @Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
    {
    	// название (android:key) настройки, которая была изменена
    	String Key = preference.getKey();
    	
    	// если это коллекция картинок
    	if (Key.equals("period"))
    	{
    		period.setSummary(newValue.toString() + " (дн.)");
    		return true;
    	}
    	
    	// если это цвет
/*    	if (Key.equals("BackgroundColor"))
    	{
    		int i = ((ListPreference)preference).findIndexOfValue(newValue.toString());
    		preference.setSummary(colValue[i]);
    		return true;
    	}
    	
    	// для всех остальных настроек (хотя у нас их и нет), ставим пришедшее значение в summary
        preference.setSummary((CharSequence)newValue);*/
        return true;
    }
}