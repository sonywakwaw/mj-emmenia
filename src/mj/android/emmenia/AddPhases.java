package mj.android.emmenia;

import android.app.Activity;  
import android.app.AlertDialog;
import android.content.Context;  
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;  
import android.util.Log;
import android.view.View;  
import android.view.ViewGroup;  
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;  
import android.widget.Button;
import android.widget.Gallery;  
import android.widget.ImageView;  
import android.widget.TextView;

public class AddPhases extends Activity  {
	
	private Gallery mGallery;  
	private ImageAdapter mImageAdapter;
	private Button butSave, butCancel, butFromMinus, butFromPlus, butToMinus, butToPlus;
	private TextView mTextView, mDayFrom, mDayTo;
	Context mContext;
	private long PhaseID;
	DBConnector mDBConnector;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phase);
        
        mContext = this;
        EmmeniaApp mApp = ((EmmeniaApp)this.getApplication());
        mDBConnector = mApp.getmDBConnector();
        
        // Галерея
        mGallery = (Gallery) findViewById(R.id.gallery);
        mImageAdapter = new ImageAdapter(this);  
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
            	int i = 1;
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
            	int i = 1;
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
           		OnePhase op = new OnePhase (PhaseID, Integer.parseInt((String)mDayFrom.getText()),
           				Integer.parseInt((String)mDayTo.getText()), mTextView.getText().toString(), mImageAdapter.getResourceId(mGallery.getSelectedItemPosition()));
           		           		
           		try {
            	/*if (PhaseID > 0)
					mDBConnector.update(op);
				else
            		mDBConnector.insert(op);*/
           		} catch (Exception e) {
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
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        int bg;

        private int[] mImageIds = {  
        		R.drawable.i_l_blue, R.drawable.i_l_green, R.drawable.i_l_red, R.drawable.i_l_yellow, 
        		R.drawable.i_gray, R.drawable.i_yellow, R.drawable.i_red, R.drawable.i_green, R.drawable.i_blue};  

        public ImageAdapter(Context c) {
            mContext = c;
            TypedArray attr = mContext.obtainStyledAttributes(R.styleable.MyGallery);
            bg = attr.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            attr.recycle();
        }

        @Override
		public int getCount() {
            return mImageIds.length;
        }

        @Override
		public Object getItem(int position) {
            return position;
        }

        @Override
		public long getItemId(int position) {
            return position;
        }
        
        public int getResourceId(int position) {
        	
        	int id = mImageIds[position];
            return id;
        }
        
        public int getPositionbyResId(int ResId) {
        	
        	for (int i = 0; i < mImageIds.length; i++)
        		if (mImageIds[i] == ResId)
        			return i;
            return 0;
        }
        
        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(mImageIds[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setBackgroundResource(bg);
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            return imageView;
        }
    }
}
