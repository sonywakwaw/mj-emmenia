package mj.android.emmenia;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	int bg;

	private Integer[] mImageIds;  

	public ImageAdapter(Context c, Integer[] imgs) {
		mContext = c;
		TypedArray attr = mContext.obtainStyledAttributes(R.styleable.MyGallery);
		bg = attr.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
		attr.recycle();
		mImageIds = imgs;
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