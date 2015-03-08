package com.teampk.lollol;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teampk.lollol.dto.ChampionDTO;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.CheckBoxListener;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;
import com.teampk.lollol.util.adapter.RankStatsListAdapter;

public class MyAccountActivity extends Activity{
	
//	private LinearLayout myAccountLayout;
//	private LinearLayout rankStatsLayout;

	private LinearLayout myAccountButtonsLayout;
	private LinearLayout modifyButtonsLayout;
	private LinearLayout modifyPositionLayout;
	private LinearLayout introduceLayout;
	
	private CheckBox[] checkBoxes = new CheckBox[6];
	private CheckBoxListener chklistener;
	
	private ImageButton modifyButton;
	private ImageButton logoutButton;
//	private ImageView removeAccountBtn;
	
	private ImageButton modifyUpdateButton;
	private ImageButton modifyCancleButton;
	
	private TextView num;
	private TextView my_pos;
	private ImageView tier;
	private TextView division;
	private TextView summoner;
	private TextView introduce;
	private ListView rank_stats;
	
	private EditText editIntroduce;
	
	private String tempPosition;
	private String tempIntroduce;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_info_my);
		
		Intent intent = getIntent();
		String userNo = intent.getStringExtra("userNo");
		
//		myAccountLayout = (LinearLayout)findViewById(R.id.my_account_layout);
//		rankStatsLayout = (LinearLayout)findViewById(R.id.rank_stats_layout);

		myAccountButtonsLayout = (LinearLayout)findViewById(R.id.my_account_buttons_Layout);
		modifyButtonsLayout = (LinearLayout)findViewById(R.id.modify_buttons_layout);
		modifyPositionLayout = (LinearLayout)findViewById(R.id.modify_position_layout);
		introduceLayout = (LinearLayout)findViewById(R.id.introduce_layout);
		
		modifyButton = (ImageButton)findViewById(R.id.modify_btn);
		logoutButton = (ImageButton)findViewById(R.id.logoutBtn);
//		removeAccountBtn = (ImageView)findViewById(R.id.removeAccountBtn);
		
		checkBoxes[0] = (CheckBox)findViewById(R.id.user_position_top);
		checkBoxes[1] = (CheckBox)findViewById(R.id.user_position_jungle);
		checkBoxes[2] = (CheckBox)findViewById(R.id.user_position_mid);
		checkBoxes[3] = (CheckBox)findViewById(R.id.user_position_ad);
		checkBoxes[4] = (CheckBox)findViewById(R.id.user_position_support);
		checkBoxes[5] = (CheckBox)findViewById(R.id.user_position_allRounder);
		
		modifyUpdateButton = (ImageButton)findViewById(R.id.modify_updateBtn);
		modifyCancleButton = (ImageButton)findViewById(R.id.modify_cancleBtn);
		
		num = (TextView)findViewById(R.id.num);
		my_pos = (TextView)findViewById(R.id.my_pos);
		tier = (ImageView)findViewById(R.id.tier);
		division = (TextView)findViewById(R.id.division);
		summoner = (TextView)findViewById(R.id.summoner);
		introduce = (TextView)findViewById(R.id.introduce);
		rank_stats = (ListView)findViewById(R.id.rank_stats);
		
		chklistener = new CheckBoxListener(MyAccountActivity.this);
		
		for(CheckBox checkBox : checkBoxes){
			checkBox.setOnCheckedChangeListener(chklistener);
		}
		
		modifyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tempPosition = my_pos.getText().toString();
				tempIntroduce = introduce.getText().toString();
				
				tier.setVisibility(View.GONE);
				division.setVisibility(View.GONE);
				myAccountButtonsLayout.setVisibility(View.GONE);
				
				modifyPositionLayout.setVisibility(View.VISIBLE);
				modifyButtonsLayout.setVisibility(View.VISIBLE);
				
				editIntroduce = new EditText(MyAccountActivity.this);
				InputFilter[] filters = new InputFilter[1];
				filters[0] = new InputFilter.LengthFilter(40);
				editIntroduce.setFilters(filters);

				String[] positions = tempPosition.split(",");
				
				for(CheckBox checkBox : checkBoxes){
					checkBox.setChecked(false);
				}
				
				for(int i = 0; i < positions.length; i++){
					switch(positions[i]){
					case "Top" : 
						checkBoxes[0].setChecked(true);
						break;
						
					case "Jungle" : 
						checkBoxes[1].setChecked(true);
						break;
						
					case "Mid" : 
						checkBoxes[2].setChecked(true);
						break;
					
					case "AD" : 
						checkBoxes[3].setChecked(true);
						break;
						
					case "Support" : 
						checkBoxes[4].setChecked(true);
						break;
						
					case "AllRounder" : 
						checkBoxes[5].setChecked(true);
						break;
						
					default : break;
					}
				}
				
				editIntroduce.setText(tempIntroduce);

				introduceLayout.addView(editIntroduce, introduceLayout.indexOfChild(introduce));
				
				my_pos.setVisibility(View.GONE);
				introduce.setVisibility(View.GONE);
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alert = new AlertDialog.Builder(MyAccountActivity.this);
				alert.setTitle("로그아웃");
				alert.setMessage("로그아웃 하시겠습니까?");
				alert.setNegativeButton("Cancel", null);
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();

						Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(mainActivity);

						LolloLSharedPref.getInstance(MyAccountActivity.this).clearSharedPref();
						LolloLActivity.lollolActivity.finish();
						finish();
					}
				});
				
				alert.show();
			}
		});
		
//		removeAccountBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder alert = new AlertDialog.Builder(MyAccountActivity.this);
//				alert.setTitle("회원 탈퇴");
//				alert.setMessage("정말 탈퇴하시겠습니까?");
//				alert.setNegativeButton("Cancel", null);
//				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//						dialog.dismiss();
//						session.closeAndClearTokenInformation();
//
//						if(session.isClosed()){
//							Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
//							LolloLSharedPref.getInstance(MyAccountActivity.this).clearSharedPref();
//							startActivity(mainActivity);
//							LolloLActivity.lollolActivity.finish();
//							finish();
//						}
//					}
//				});
//				alert.show();
//			}
//		});
		
		modifyUpdateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				InputMethodManager imm = (InputMethodManager)MyAccountActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				View focusedView = MyAccountActivity.this.getCurrentFocus();
				
				if(focusedView != null){
					imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
				
				String modifyIntro = editIntroduce.getText().toString();
				String modifyChkStr = chklistener.checkedList(checkBoxes);
				
				QueryConnection qc = new QueryConnection(new TaskListener() {
					
					@Override
					public void onTaskComplete(String result) {
						if(result.trim().equals("1")){
							Toast.makeText(MyAccountActivity.this, "개인정보가 업데이트 되었습니다", Toast.LENGTH_SHORT).show();
						} else if(result.trim().equals("0")){
							Toast.makeText(MyAccountActivity.this, "개인정보가 업데이트 실패", Toast.LENGTH_SHORT).show();							
						} else {
							Toast.makeText(MyAccountActivity.this, "DB연결 실패?", Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onTaskComplete(boolean result) {}
					@Override
					public void onTaskComplete(MemberDTO member) {}
					
				}, MyAccountActivity.this, null);
				
				if(!(modifyIntro.equals(tempIntroduce) && modifyChkStr.equals(tempPosition))){

					qc.execute("updateMyAccount", modifyChkStr, modifyIntro);

					my_pos.setText(modifyChkStr);
					introduce.setText(modifyIntro);
				}
				
				myAccountButtonsLayout.setVisibility(View.VISIBLE);

				modifyButtonsLayout.setVisibility(View.GONE);
				modifyPositionLayout.setVisibility(View.GONE);
				
				my_pos.setVisibility(View.VISIBLE);
				introduce.setVisibility(View.VISIBLE);
				tier.setVisibility(View.VISIBLE);
				division.setVisibility(View.VISIBLE);
				
				introduceLayout.removeView(editIntroduce);
			}
		});

		modifyCancleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				myAccountButtonsLayout.setVisibility(View.VISIBLE);

				modifyButtonsLayout.setVisibility(View.GONE);
				modifyPositionLayout.setVisibility(View.GONE);
				
				my_pos.setVisibility(View.VISIBLE);
				introduce.setVisibility(View.VISIBLE);
				tier.setVisibility(View.VISIBLE);
				division.setVisibility(View.VISIBLE);
				
				introduceLayout.removeView(editIntroduce);
			}
		});
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				try {
					JSONObject jsonOb = new JSONObject(result);
					num.setText(jsonOb.getString("num"));
					my_pos.setText(jsonOb.getString("my_pos"));
					division.setText(jsonOb.getString("tier") + " " + jsonOb.getString("division"));
					summoner.setText(jsonOb.getString("summoner"));
					
					String tierStr = jsonOb.getString("tier").toLowerCase(Locale.US);
					Resources rc = getResources();
					
					tier.setImageDrawable(rc.getDrawable(rc.getIdentifier("tier_"+tierStr, "drawable", "com.teampk.lollol")));

					if(jsonOb.getString("introduce").isEmpty() || jsonOb.getString("introduce").equals("null")){
						introduce.setText("자기소개가 없습니다");
					} else {
						introduce.setText(jsonOb.getString("introduce"));
					}
					
					ArrayList<ArrayList<ChampionDTO>> topRankChamps = decodeRankChamps(jsonOb.getString("rank_champs"));
					ArrayList<ChampionDTO> rank_champ_resultArray = new ArrayList<ChampionDTO>();
					int totalCount = 0;
					
					if(topRankChamps != null){
						int i = 0;
						
						for(ArrayList<ChampionDTO> rankChamps : topRankChamps){
							int id = rankChamps.get(i).getId();
							int count = rankChamps.get(i).getCount();
							String ChampName = LolloLActivity.db.getChamp(id).getName();
							
							rank_champ_resultArray.add(new ChampionDTO(id, count, ChampName));
							
							i++;
						}
					}
					
					for(int i = 0; i < rank_champ_resultArray.size(); i++){
						int champCounts = rank_champ_resultArray.get(i).getCount();
						totalCount = champCounts + totalCount;
					}
					
					String temp = "Total Played : " + Integer.toString(totalCount) + " Games";
					TextView totalPlayed = new TextView(MyAccountActivity.this);
					totalPlayed.setText(temp);
					totalPlayed.setTypeface(null, Typeface.BOLD);
					
					rank_stats.addFooterView(totalPlayed);
					rank_stats.setAdapter(new RankStatsListAdapter(MyAccountActivity.this, rank_champ_resultArray, totalCount));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, MyAccountActivity.this, null);
		
		qc.execute("getDetailInfo", userNo);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public ArrayList<ArrayList<ChampionDTO>> decodeRankChamps(String jsonChamps){
		if(jsonChamps != null){
			ArrayList<ArrayList<ChampionDTO>> rankChampArr = new ArrayList<ArrayList<ChampionDTO>>();
			ArrayList<ChampionDTO> a = new ArrayList<ChampionDTO>();
	
			try {
				JSONArray jsonArr = new JSONArray(jsonChamps);
				for(int i = 0; i < jsonArr.length(); i++){
					JSONObject jsonOb = jsonArr.getJSONObject(i);
					a.add(new ChampionDTO(jsonOb.getInt("id"), jsonOb.getInt("count")));
					rankChampArr.add(a);
				}
	
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return rankChampArr;
		} else {
			return null;
		}
	}

}
