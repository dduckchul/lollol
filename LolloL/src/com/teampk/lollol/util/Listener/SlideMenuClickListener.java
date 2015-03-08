package com.teampk.lollol.util.Listener;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teampk.lollol.R;
import com.teampk.lollol.LolloLActivity;
import com.teampk.lollol.MyAccountActivity;
import com.teampk.lollol.util.LolloLSharedPref;
import com.teampk.lollol.util.RangeSeekBar;
import com.teampk.lollol.util.RangeSeekBar.OnRangeSeekBarChangeListener;

public class SlideMenuClickListener implements ListView.OnItemClickListener{

	private Context context;
	private ImageView[] images = new ImageView[6];

    private RangeSeekBar<Integer> seekBar;
	
	public SlideMenuClickListener(Context context){
		this.context = context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		displayMenu(position, view, context);
		
	}

	public void displayMenu(int position, View view, final Context context){

        final LolloLSharedPref lolloLSharedPref = new LolloLSharedPref(context);

		switch(position){
	
		case 0 : 
			final int totalRank = lolloLSharedPref.getPref("totalRank", -1);
			int downToMyRank = 	lolloLSharedPref.getPref("downToMyRank", -1);
			int upToMyRank = lolloLSharedPref.getPref("upToMyRank", -1);
			int findPosition = lolloLSharedPref.getPref("findPosition", -1);

            initializeSeekBar(totalRank, downToMyRank, upToMyRank);

			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.popup_dialog_fliter);
			dialog.setTitle(R.string.menu_filter);
			dialog.getWindow().setBackgroundDrawableResource(R.color.background);
			
			LinearLayout dialogLayout = (LinearLayout)dialog.findViewById(R.id.dialog_layout);
			
			final TextView range_min_text = (TextView)dialog.findViewById(R.id.range_min_text);
			final TextView range_max_text = (TextView)dialog.findViewById(R.id.range_max_text);
			range_min_text.setText(totalRankToString(seekBar.getSelectedMinValue()));
			range_max_text.setText(totalRankToString(seekBar.getSelectedMaxValue()));
			Button applyBtn = (Button)dialog.findViewById(R.id.applyBtn);
			Button cancleBtn = (Button)dialog.findViewById(R.id.cancleBtn);
			
			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.setSaturation(0);
			ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
			final ImageClickListener imgListener = new ImageClickListener(colorFilter, findPosition);

			RelativeLayout posImages = (RelativeLayout)dialog.findViewById(R.id.posImages);
			for(int i = 0; i < posImages.getChildCount(); i++){
				images[i] = (ImageView)posImages.getChildAt(i);

				if(i != findPosition){
				    images[i].getDrawable().setColorFilter(colorFilter);
				} else if(i == findPosition){
                    images[i].getDrawable().setColorFilter(null);
                }

				images[i].setOnClickListener(imgListener);
			}

			applyBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    lolloLSharedPref.putPref("downToMyRank", totalRank - seekBar.getSelectedMinValue());
                    lolloLSharedPref.putPref("upToMyRank", seekBar.getSelectedMaxValue() - totalRank);
                    lolloLSharedPref.putPref("findPosition", imgListener.position);

					LolloLActivity.lollolActivity.findMyDuo();
					LolloLActivity.lollolActivity.mDrawerLayout.closeDrawer(LolloLActivity.lollolActivity.navigationDrawer);
					
					dialog.dismiss();
				}
			});
			
			cancleBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
	
				@Override
				public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
						Integer minValue, Integer maxValue) {
					
					if(totalRank - minValue < 0){
						seekBar.setSelectedMinValue(totalRank);
					}
					
					if(maxValue  - totalRank < 0){
						seekBar.setSelectedMaxValue(totalRank);
					}
					
					if(minValue > 30 && minValue < 35){
						seekBar.setSelectedMinValue(30);
					}
					
					if(maxValue > 30 && maxValue < 35){
						seekBar.setSelectedMaxValue(30);
					}
					
					range_min_text.setText(totalRankToString(seekBar.getSelectedMinValue()));
					range_max_text.setText(totalRankToString(seekBar.getSelectedMaxValue()));
				}
			});
			
			dialogLayout.addView(seekBar, 3, params);
			dialog.show();
			break;
		
		case 1 : 
			
			Intent info = new Intent(context, MyAccountActivity.class);
			info.putExtra("userNo", Integer.toString(lolloLSharedPref.getPref("userNo", 0)));
			context.startActivity(info);
			break;
		
		case 2 : 
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.teampk.lollol");
			shareIntent.setType("text/html");
			context.startActivity(Intent.createChooser(shareIntent, "친구에게 LolloL을 공유하세요"));
			break;
		
		case 3 :
			
			Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show();
			break;
			
		case 4 : 
			Dialog dialog_qna = new Dialog(context);
			dialog_qna.setContentView(R.layout.popup_dialog_qna);
			dialog_qna.setTitle("문의 하기 / About Us...");
			dialog_qna.getWindow().setBackgroundDrawableResource(R.color.background);
			dialog_qna.show();
			break;
		
		default : break;
		}
	}

    private void initializeSeekBar(int totalRank, int downToMyRank, int upToMyRank) {

        if(totalRank >= 1 && totalRank <= 5){
            seekBar = new RangeSeekBar<Integer>(1, 10, context);
        } else if(totalRank >= 6 && totalRank <= 10){
            seekBar = new RangeSeekBar<Integer>(1, 15, context);
        } else if(totalRank >= 11 && totalRank <= 15) {
            seekBar = new RangeSeekBar<Integer>(6, 20, context);
        } else if(totalRank >= 16 && totalRank <= 20) {
            seekBar = new RangeSeekBar<Integer>(11, 25, context);
        } else if(totalRank >= 21 && totalRank <= 25) {
            seekBar = new RangeSeekBar<Integer>(16, 30, context);
        } else if(totalRank == 30) {
            seekBar = new RangeSeekBar<Integer>(21, 35, context);
        } else if(totalRank == 35){
            seekBar = new RangeSeekBar<Integer>(30, 35, context);
        } else if(totalRank == 0){
            seekBar = new RangeSeekBar<Integer>(1, 15, context);
        } else {
            seekBar = new RangeSeekBar<Integer>(1, 35, context);
        }

        if(totalRank >= 0 && totalRank <=2){
            seekBar.setSelectedMinValue(1);
        } else {
            seekBar.setSelectedMinValue(totalRank - downToMyRank);
        }

        if(totalRank == 30){
            seekBar.setSelectedMaxValue(35);
        } else if(totalRank == 35){
            seekBar.setSelectedMinValue(30);
            seekBar.setSelectedMaxValue(35);
        } else {
            seekBar.setSelectedMaxValue(totalRank + upToMyRank);
        }

        seekBar.setNotifyWhileDragging(true);
    }

    public String totalRankToString(int totalRank){
		
		String tier = null;
		String division = null;
		
		int minusOne = totalRank-1;
		
		if(totalRank == 0){
			
			return "UNRANKED";
		
		} else {
			
			switch(minusOne / 5){
			case 0 : tier = "BRONZE"; break;
			case 1 : tier = "SILVER"; break;
			case 2 : tier = "GOLD"; break;
			case 3 : tier = "PLATINUM"; break;
			case 4 : tier = "DIAMOND"; break;
			case 5 : tier = "MASTER"; break;
			case 6 : tier = "CHALLENGER"; break;
			default : tier = "UNKNOWN RANK"; break;
			}
			
			if(tier.equals("MASTER") || tier.equals("CHALLENGER")){
				return tier;
			}
			
			switch(minusOne % 5){
			case 0 : division = "V"; break;
			case 1 : division = "IV"; break;
			case 2 : division = "III"; break;
			case 3 : division = "II"; break;
			case 4 : division = "I"; break;
			default : division = "UNKNOWN DIV"; break;
			}
			
			return tier + " " + division;
		}
	}
}