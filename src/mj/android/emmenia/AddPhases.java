package mj.android.emmenia;

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
import android.widget.Gallery;  
import android.widget.TextView;

public class AddPhases extends Activity  {
	
	private Gallery mGallery;  
	private ImageAdapter mImageAdapter;
	private Button butSave, butCancel, butFromMinus, butFromPlus, butToMinus, butToPlus;
	private TextView mTextView, mDayFrom, mDayTo;
	Context mContext;
	private long PhaseID;
	DBConnector mDBConnector;
	EmmeniaApp mApp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phase);
        
        Log.w("MY", "AddPhases");
        
        mContext = this;
        mApp = ((EmmeniaApp)this.getApplication());
        mDBConnector = mApp.getmDBConnector();
        
        // Галерея
        mGallery = (Gallery) findViewById(R.id.gallery);
        mImageAdapter = new ImageAdapter(this, mApp.getImgStatus());  
        mGallery.setAdapter(mImageAdapter);
        
        // Текст и дата
        mTextView = (TextView) findViewById(R.id.DescText);
        mDayFrom = (TextView) findViewById(R.id.FromDay);
        mDayTo = (TextView) findViewById(R.id.ToDay);
        
        if (getIntent().hasExtra("OnePhase")) {
        	OnePhase op = (OnePhase) getIntent().getSerializableExtra("OnePhase");
        	mDayFrom.setText(String.valueOf(op.getFrom()));
        	mDayTo.setText(String.valueOf(op.getTo()));
        	mTextView.setText(op.getDesc());
        	mGallery.setSelection(mImageAdapter.getPositionbyResId(op.getIcon()));
        	PhaseID = op.getID();
        }
        else {
        	PhaseID = -1;
        	mGallery.setSelection(mImageAdapter.getCount() / 2);
        }
        
        // Кнопки + и -
        butFromMinus = (Button) findViewById(R.id.butFromMinus);
        butFromPlus = (Button) findViewById(R.id.butFromPlus);
        butToMinus = (Button) findViewById(R.id.butToMinus);
        butToPlus = (Button) findViewById(R.id.butToPlus);
        
        butFromMinus.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	int i = 1;
            	try {
            		i = Integer.parseInt(mDayFrom.getText().toString());
            	}
            	catch (Exception e) {}
            	if (i > 1) {
           			i--;
            	   	mDayFrom.setText(String.valueOf(i));
           		}
            }
         });
        
        butFromPlus.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	int i = 0;
            	try {
            		i = Integer.parseInt(mDayFrom.getText().toString());
            	}
            	catch (Exception e) {}
            	if (i < 99) {
           			i++;
            	   	mDayFrom.setText(String.valueOf(i));
           		}
            }
         });
        
        butToMinus.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	int i = 1;
            	try {
            		i = Integer.parseInt(mDayTo.getText().toString());
            	}
            	catch (Exception e) {}
            	if (i > 1) {
           			i--;
           			mDayTo.setText(String.valueOf(i));
           		}
            }
         });
        
        butToPlus.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	int i = 0;
            	try {
            		i = Integer.parseInt(mDayTo.getText().toString());
            	}
            	catch (Exception e) {}
            	if (i < 99) {
           			i++;
           			mDayTo.setText(String.valueOf(i));
           		}
            }
         });
        
        // Кнопки
        butSave = (Button) findViewById(R.id.butSave);
        butCancel = (Button) findViewById(R.id.butCancel);
        
        butSave.setOnClickListener (new OnClickListener() {
            @Override
			public void onClick(View v) {
            	
            	if (Integer.parseInt(mDayFrom.getText().toString()) > Integer.parseInt(mDayTo.getText().toString()))
            		showAlert ("День 'c' должен быть меньше чем 'по'");
            	
            	Integer ResID = mImageAdapter.getResourceId(mGallery.getSelectedItemPosition());
           		OnePhase op = new OnePhase (PhaseID, Integer.parseInt(mDayFrom.getText().toString()),
           				Integer.parseInt(mDayTo.getText().toString()), mTextView.getText().toString(), mApp.getIndexStatus(ResID));
           		try {           			
	            	if (PhaseID > 0)
						mDBConnector.updatePhase(op);
					else
	            		mDBConnector.insertPhase(op);
           		} catch (Exception e) {
           			showAlert ("Фазы цикла не могут пересекаться.");
           			return;
				}
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
    
    private void showAlert (String mes) {
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
	        alertbox.setTitle("Ошибка");
	        alertbox.setMessage(mes);
	        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	            @Override
				public void onClick(DialogInterface arg0, int arg1) {
	                
	            }
	        });
	        alertbox.show();
    }
}
