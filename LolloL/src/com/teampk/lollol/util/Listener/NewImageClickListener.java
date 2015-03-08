package com.teampk.lollol.util.Listener;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewImageClickListener implements OnClickListener {

	private ColorMatrixColorFilter colorFilter;
	private Context context;
	private TextView selected_position;
	
	private boolean [] positionClicked = new boolean[6];
			
	public NewImageClickListener(ColorMatrixColorFilter colorFilter, Context context, TextView selected_position){
		this.colorFilter = colorFilter;
		this.context = context;
		this.selected_position = selected_position;
	}
	
	@Override
	public void onClick(View v) {

		RelativeLayout imgLayout = (RelativeLayout)v.getParent();
		int position = imgLayout.indexOfChild(v);
		ImageView currImageView = (ImageView)v;
		
		if(checkPositionClickedLimit()){
			
			if(positionClicked[position]){
				
				positionClicked[position] = false;
				currImageView.getDrawable().setColorFilter(colorFilter);
				selected_position.setText(getPositionString());
				
			} else {
				
				Toast.makeText(context, "2개이상 선택할 수 없습니다", Toast.LENGTH_SHORT).show();

			}
			
		} else {
			
			if(positionClicked[position]){

				positionClicked[position] = false;
				currImageView.getDrawable().setColorFilter(colorFilter);
				selected_position.setText(getPositionString());

			} else {
				
				positionClicked[position] = true;
				currImageView.getDrawable().setColorFilter(null);
				selected_position.setText(getPositionString());

			}
		};
	}

	private boolean checkPositionClickedLimit() {
		
		int limit = 0;
		
		for(int i=0; i < positionClicked.length; i++){
			if(positionClicked[i]){
				limit++;
			}
		}
		
		boolean result = (limit>=2) ? true : false;
		
		return result;
	}
	
	private String getPositionString(){
		
		String positionString = "";
		boolean isNull = true;

		for(int i=0; i < positionClicked.length; i++){
			if(positionClicked[i]){
				switch(i){
				case 0 :
					positionString += "Top";
					break;
					
				case 1 :
					positionString += "Jungle";
					break;
					
				case 2 :
					positionString += "Mid";
					break;
					
				case 3 :
					positionString += "AD";
					break;
					
				case 4 :
					positionString += "Support";
					break;
					
				case 5 :
					positionString += "AllRounder";
					break;
					
				default : positionString = "Error";
				}
				positionString += ",";
				isNull = false;			
			}
		}
		
		if(isNull == false){
			positionString = positionString.substring(0, positionString.length()-1);
		}
		
		return positionString.trim();
	}
}
