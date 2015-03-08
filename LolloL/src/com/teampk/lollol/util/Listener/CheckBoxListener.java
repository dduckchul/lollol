package com.teampk.lollol.util.Listener;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class CheckBoxListener implements OnCheckedChangeListener{
	
	private int checkedBoxCount = 0;
	private Context context;

	public CheckBoxListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(checkedBoxCount == 2 && isChecked){
			
			buttonView.setChecked(false);
			Toast.makeText(context.getApplicationContext(), "2개 이상 선택하실 수 없습니다", Toast.LENGTH_SHORT).show();
			
		}else if(isChecked){
			
			checkedBoxCount++;
			
		}else if(isChecked == false){
			
			checkedBoxCount--;
		}
	}
	
	public String checkedList(CheckBox[] checkBoxes){

		String result = "";
		boolean isNull = true;
		int count = 0;
		
		for(CheckBox checkBox : checkBoxes){
			
			if(checkBox.isChecked()){
				switch (count) {
				case 0 :
					result += "Top";
					break;
					
				case 1 :
					result += "Jungle";
					break;
					
				case 2 :
					result += "Mid";
					break;
					
				case 3 :
					result += "AD";
					break;
					
				case 4 :
					result += "Support";
					break;
					
				case 5 :
					result += "AllRounder";
					break;
					
				default : result = "Error";
				}
				result += ",";
				isNull = false;
			}
			count++;
		}
		
		if(isNull == false){
			result = result.substring(0, result.length()-1);
		}

		return result;
	}
			
}
