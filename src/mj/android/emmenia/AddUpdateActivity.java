package mj.android.emmenia;

import java.util.Date;

import android.app.Activity;  
import android.app.AlertDialog;
import android.content.Context;  
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;  
import android.util.Log;
import android.view.View;  
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Gallery;  
import android.widget.TextView;

public class AddUpdateActivity extends Activity  {
	
	private Gallery mGallery;  
	private ImageAdapter mImageAdapter;
	private Button butSave, butCancel;
	private TextView mTextView;
	private DatePicker mDatePicker;
	Context mContext;
	private long EntryID;
	DBConnector mDBConnector;
	EmmeniaApp mApp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_update);
        
        mContext = this;
        mApp = ((EmmeniaApp)this.getApplication());
        mDBConnector = mApp.getmDBConnector();
        
        // Галерея
        mGallery = (Gallery) findViewById(R.id.gallery);
        mImageAdapter = new ImageAdapter(this, mApp.getImgEmoticons());  
        mGallery.setAdapter(mImageAdapter);
        
        // Текст и дата
        mTextView = (TextView) findViewById(R.id.DescText);
        mDatePicker = (DatePicker) findViewById(R.id.Date);
        
        if (getIntent().hasExtra("OneEntry")) {
        	OneEntry md = (OneEntry) getIntent().getSerializableExtra("OneEntry");
        	Date d = new Date (md.getDate());
        	mDatePicker.updateDate(d.getYear() + 1900, d.getMonth(), d.getDate());
        	mGallery.setSelection(mImageAdapter.getPositionbyResId(md.getIcon()));
        	mTextView.setText(md.getTitle());
        	EntryID = md.getID();
        }
        else {
        	EntryID = -1;
        	mGallery.setSelection(mImageAdapter.getCount() / 2);
        }
        
        // Кнопки
        butSave = (Button) findViewById(R.id.butSave);
        butCancel = (Button) findViewById(R.id.butCancel);
        
        butSave.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	Date date = new Date(mDatePicker.getYear()-1900, mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
            	Integer ResID = mImageAdapter.getResourceId(mGallery.getSelectedItemPosition());
           		OneEntry md = new OneEntry (EntryID, date.getTime(), mTextView.getText().toString(), mApp.getIndexEmoticons(ResID));
           		
           		try {
            	if (EntryID > 0)
					mDBConnector.update(md);
				else
            		mDBConnector.insert(md);
           		} catch (Exception e) {
           			Log.w("MY", "-3-");
           	        AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
           	        alertbox.setTitle("Ошибка");
           	        alertbox.setMessage("Запись на эту дату уже существует");
           	        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
           	            @Override
           				public void onClick(DialogInterface arg0, int arg1) {
           	                
           	            }
           	        });
           	        alertbox.show();
           			return;
				}
           		Log.w("MY", "-4-");
            	Intent intent = getIntent();
            	setResult(RESULT_OK, intent);
            	finish();
            }
         });

        butCancel.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	setResult(RESULT_CANCELED, new Intent());
            	finish();
            }
         });
    }
}
