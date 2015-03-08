package com.teampk.lollol;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChampionDTO;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.adapter.RankStatsListAdapter;

public class InfoActivity extends Activity{
	
	public String jsonResult;
	
	private TextView num;
	private TextView my_pos;
	private ImageView tier;
	private TextView division;
	private TextView summoner;
	private TextView introduce;
	private ListView rank_stats;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detail_info);
		
		num = (TextView)findViewById(R.id.num);
		my_pos = (TextView)findViewById(R.id.my_pos);
		tier = (ImageView)findViewById(R.id.tier);
		division = (TextView)findViewById(R.id.division);
		summoner = (TextView)findViewById(R.id.summoner);
		introduce = (TextView)findViewById(R.id.introduce);
		rank_stats = (ListView)findViewById(R.id.rank_stats);

		Intent intent = getIntent();
		String userNo = intent.getStringExtra("userNo");
		final String userName = intent.getStringExtra("userName");
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				try {
					JSONObject jsonOb = new JSONObject(result);
					num.setText(jsonOb.getString("num"));
					my_pos.setText(jsonOb.getString("my_pos"));
					
					String tierStr = jsonOb.getString("tier").toLowerCase(Locale.US);
					Resources rc = getResources();
					
					tier.setImageDrawable(rc.getDrawable(rc.getIdentifier("tier_"+tierStr, "drawable", "com.teampk.lollol")));
					division.setText(jsonOb.getString("tier") + " " + jsonOb.getString("division"));
					
					Log.i("introduce", "introduce : " + jsonOb.getString("introduce"));
					
					if(jsonOb.getString("introduce").isEmpty() || jsonOb.getString("introduce").equals("null")){
						introduce.setText("자기소개가 없습니다");
					} else {
						introduce.setText(jsonOb.getString("introduce"));
					}
					
					if(userName != null){
						summoner.setText(userName);						
					} else {
						summoner.setVisibility(View.GONE);
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

                            if(ChampName != null){
                                rank_champ_resultArray.add(new ChampionDTO(id, count, ChampName));
                            } else {
                                rank_champ_resultArray.add(new ChampionDTO(id, count, null));
                            }

							i++;
						}
					}
					
					for(int i = 0; i < rank_champ_resultArray.size(); i++){
						int champCounts = rank_champ_resultArray.get(i).getCount();
						totalCount = champCounts + totalCount;
					}
					
					String temp = "Total Played : " + Integer.toString(totalCount) + " Games";
					TextView totalPlayed = new TextView(InfoActivity.this);
					totalPlayed.setText(temp);
					totalPlayed.setTypeface(null, Typeface.BOLD);
					
					rank_stats.addFooterView(totalPlayed);
					rank_stats.setAdapter(new RankStatsListAdapter(InfoActivity.this, rank_champ_resultArray, totalCount));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, InfoActivity.this, null);
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
