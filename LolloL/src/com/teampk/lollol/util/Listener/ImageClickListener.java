package com.teampk.lollol.util.Listener;

import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageClickListener implements OnClickListener {

	public int position;

	private ColorMatrixColorFilter colorFilter;
	
	public ImageClickListener(ColorMatrixColorFilter colorFilter, int position){
		this.colorFilter = colorFilter;
		this.position = position;
	}
	
	@Override
	public void onClick(View v) {
		RelativeLayout posImages = (RelativeLayout)v.getParent();
		ImageView currImageView = (ImageView)v;

		int curr = posImages.indexOfChild(v);

		if(curr == position){

			currImageView.getDrawable().setColorFilter(colorFilter);
			curr = 6;

		} else {

			if(position != 6){
				ImageView prevImageView = (ImageView)posImages.getChildAt(position);
				prevImageView.getDrawable().setColorFilter(colorFilter);
			}
			currImageView.getDrawable().setColorFilter(null);
		}
		position = curr;
	}

}
