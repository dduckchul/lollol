package com.teampk.lollol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teampk.lollol.dto.ChampionDTO;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.PushServiceConnection;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.TaskListener;

public class RequestFragment extends Fragment{

	public String jsonResult;
	public int screenWidth;
	public int screenHeight;
	public int screenCenter;
	public int x_cord, y_cord, x, y;
	public int likes = 0;
	public int listNum = 0;
	
	public FragmentManager fManager;
	public FragmentTransaction fTransaction;
	
	private RelativeLayout parentView;
	
	public RequestFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        jsonResult = getArguments().getString("jsonString");

		if(jsonResult != null){
			ArrayList<MemberDTO> members = decodeJsonString(jsonResult);
			
			DisplayMetrics displayMetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			
			screenWidth = displayMetrics.widthPixels;
			screenHeight = displayMetrics.heightPixels;
			screenCenter = screenWidth / 2;
						
			View view = inflater.inflate(R.layout.request_list, container, false);
			parentView = (RelativeLayout)view.findViewById(R.id.parentView);
			
			for(MemberDTO member : members){
				View cardView = makeResultCards(member, container, inflater);
				parentView.addView(cardView);
			}
			
			ImageButton likeBtn = (ImageButton)view.findViewById(R.id.likeBtn);
			ImageButton infoBtn = (ImageButton)view.findViewById(R.id.infoBtn);
			ImageButton dislikeBtn = (ImageButton)view.findViewById(R.id.dislikeBtn);
			
			likeBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int cardSequence = parentView.getChildCount();
					if(cardSequence > 0){
						
						View currentCard = parentView.getChildAt(cardSequence-1);
						TextView num = (TextView)currentCard.findViewById(R.id.num);
						
						QueryConnection qc = makeQueryConnection(num.getText().toString());
						
						AnimationSet moveAndFadeOut = new AnimationSet(true);
						TranslateAnimation trans = new TranslateAnimation(0, screenCenter, parentView.getY(), parentView.getY());
						AlphaAnimation alpha = new AlphaAnimation(1, 0);
						trans.setDuration(500);
						alpha.setStartOffset(500);
						alpha.setDuration(500);
						moveAndFadeOut.addAnimation(trans);
						moveAndFadeOut.addAnimation(alpha);
						moveAndFadeOut.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {}
							@Override
							public void onAnimationRepeat(Animation animation) {}
							@Override
							public void onAnimationEnd(Animation animation) {
								int childCount = parentView.getChildCount();
								
								if(childCount < 1){
									LayoutInflater inflater = (LayoutInflater)getActivity().
											getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									ViewGroup container = (ViewGroup)parentView.getParent();
									findMore(container, inflater);
								}
							}
						});
						qc.execute("responseToList", num.getText().toString(), "1");
						currentCard.startAnimation(moveAndFadeOut);
						parentView.removeView(currentCard);
					}
				}
			});
			
			infoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int cardSequence = parentView.getChildCount();

					if(cardSequence > 0){
						View currentCard = parentView.getChildAt(cardSequence-1);
						TextView num = (TextView)currentCard.findViewById(R.id.num);

						Intent info = new Intent(getActivity(), InfoActivity.class);
						info.putExtra("userNo", num.getText().toString());
						startActivity(info);
					}
				}
			});
			
			dislikeBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int cardSequence = parentView.getChildCount();
					if(cardSequence > 0){
						
						View currentCard = parentView.getChildAt(cardSequence-1);
						TextView num = (TextView)currentCard.findViewById(R.id.num);

						QueryConnection qc = makeQueryConnection(num.getText().toString());
	
						AnimationSet moveAndFadeOut = new AnimationSet(true);
						TranslateAnimation trans = new TranslateAnimation(0, -screenCenter, parentView.getY(), parentView.getY());
						AlphaAnimation alpha = new AlphaAnimation(1, 0);
						trans.setDuration(500);
						alpha.setStartOffset(500);
						alpha.setDuration(500);
						moveAndFadeOut.addAnimation(trans);
						moveAndFadeOut.addAnimation(alpha);
						moveAndFadeOut.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {}
							@Override
							public void onAnimationRepeat(Animation animation) {}
							@Override
							public void onAnimationEnd(Animation animation) {
								int childCount = parentView.getChildCount();
								
								if(childCount < 1){
									LayoutInflater inflater = (LayoutInflater)getActivity().
											getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									ViewGroup container = (ViewGroup)parentView.getParent();
									findMore(container, inflater);
								}
							}
						});
						qc.execute("responseToList", num.getText().toString(), "2");
						currentCard.startAnimation(moveAndFadeOut);
						parentView.removeView(currentCard);
					}
				}
			});
			
			return view;
			
		} else {
			
			View view = inflater.inflate(R.layout.request_list, container, false);
			return view;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public View makeResultCards(MemberDTO member, ViewGroup container, LayoutInflater inflater){
		
		View view = inflater.inflate(R.layout.request_list_result, container, false);
		
		final int partnerNum = member.getNum();
		Resources rc = getResources();

		ArrayList<ArrayList<ChampionDTO>> topRankChamps = decodeRankChamps(member.getRank_champs_short());
		ArrayList<ChampionDTO> rank_champ_resultArray = new ArrayList<ChampionDTO>();
		
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
		
		TextView num = (TextView)view.findViewById(R.id.num);
		TextView time = (TextView)view.findViewById(R.id.time);
		TextView my_pos = (TextView)view.findViewById(R.id.my_pos);
		ImageView tier = (ImageView)view.findViewById(R.id.tier);
		TextView division = (TextView)view.findViewById(R.id.division);
		LinearLayout rank_champ_layout = (LinearLayout)view.findViewById(R.id.champ_layout);
		
		for(int i = 0; i< rank_champ_resultArray.size(); i++){
			View rank_champ_view = inflater.inflate(R.layout.rank_champ_array, container, false);
			
			TextView champ_name = (TextView)rank_champ_view.findViewById(R.id.rank_champ_name);
			TextView champ_count = (TextView)rank_champ_view.findViewById(R.id.rank_champ_count);
			ImageView champ_image = (ImageView)rank_champ_view.findViewById(R.id.rank_champ_image);
			
			int champ_id  = rank_champ_resultArray.get(i).getId();

			champ_name.setText(rank_champ_resultArray.get(i).getName());
			champ_count.setText(Integer.toString(rank_champ_resultArray.get(i).getCount()));
            if(rank_champ_resultArray.get(i).getName() != null){
                champ_image.setImageDrawable(rc.getDrawable(rc.getIdentifier("champ_"+champ_id, "drawable", "com.teampk.lollol")));
            } else {
                champ_image.setImageDrawable(rc.getDrawable(rc.getIdentifier("champ_empty", "drawable", "com.teampk.lollol")));
            }

			rank_champ_layout.addView(rank_champ_view);
		}
		
		num.setText(Integer.toString(partnerNum));
		
		String passedTime = getTimePassed(member.getRequest_time());
		time.setText(passedTime);
		
		my_pos.setText(member.getMy_pos());
		tier.setImageDrawable(rc.getDrawable
				(rc.getIdentifier("tier_"+member.getTier().toLowerCase(Locale.US), "drawable", "com.teampk.lollol")));
		division.setText(member.getTier() + " " + member.getDivision());
		
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				
				QueryConnection qc = makeQueryConnection(Integer.toString(partnerNum));
				parentView.requestDisallowInterceptTouchEvent(true);
				x_cord = (int)event.getRawX();
				v.setX(x_cord - screenCenter);
				
				switch(event.getAction()){
				
				case MotionEvent.ACTION_DOWN : 
					x = (int)event.getX();
					break;
					
				case MotionEvent.ACTION_MOVE : 
					
					x_cord = (int)event.getRawX();
					v.setX(x_cord - screenCenter);
					
					if(x_cord > screenWidth - (screenCenter / 4)){
						likes = 2;
					}else if(x_cord < screenCenter / 4){
						likes = 1;
					} else {
						likes = 0;
					}
					
					break;
					
				case MotionEvent.ACTION_UP :
					
					x_cord = (int)event.getRawX();
					y_cord = (int)event.getRawY();

					if(likes == 0){
						v.setX(0);
					} else if(likes == 1){
						AlphaAnimation alpha = new AlphaAnimation(1, 0);
						alpha.setDuration(500);
						alpha.setAnimationListener(new Animation.AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {}
							@Override
							public void onAnimationRepeat(Animation animation) {}
							@Override
							public void onAnimationEnd(Animation animation) {
								parentView.removeView(v);
								int childCount = parentView.getChildCount();
								
								if(childCount < 1){
									LayoutInflater inflater = (LayoutInflater)getActivity().
											getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									ViewGroup container = (ViewGroup)parentView.getParent();
									findMore(container, inflater);
								}
							}
						});
						qc.execute("responseToList", Integer.toString(partnerNum), "2");
						v.startAnimation(alpha);
						
					} else if(likes == 2){
						AlphaAnimation alpha = new AlphaAnimation(1, 0);
						alpha.setDuration(500);
						alpha.setAnimationListener(new Animation.AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {}
							@Override
							public void onAnimationRepeat(Animation animation) {}
							@Override
							public void onAnimationEnd(Animation animation) {
								parentView.removeView(v);
								int childCount = parentView.getChildCount();
								
								if(childCount < 1){
									LayoutInflater inflater = (LayoutInflater)getActivity().
											getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									ViewGroup container = (ViewGroup)parentView.getParent();
									findMore(container, inflater);
								}
							}
						});
						qc.execute("responseToList", Integer.toString(partnerNum), "1");
						v.startAnimation(alpha);
					}
					parentView.requestDisallowInterceptTouchEvent(false);
					v.performClick();
					break;
				default : break;
				}
				return true;
			}
		});

		return view;
	}

	public ArrayList<MemberDTO> decodeJsonString(String jsonResult){
		
		ArrayList<MemberDTO> resultMembers = new ArrayList<MemberDTO>();
		
		try {
			JSONArray jsonarr = new JSONArray(jsonResult);
			for(int i = 0; i < jsonarr.length(); i++){
				JSONObject jsonob = jsonarr.getJSONObject(i);
				if(jsonob.getString("rank_champs_short") != null){
					MemberDTO member = new MemberDTO(jsonob.getInt("num"), jsonob.getString("my_pos"), 
							jsonob.getString("tier"), jsonob.getString("division"), jsonob.getString("rank_champs_short"), jsonob.getString("request_time"));
					resultMembers.add(member);
				}else{
					MemberDTO member = new MemberDTO(jsonob.getInt("num"), jsonob.getString("my_pos"), 
							jsonob.getString("tier"), jsonob.getString("division"), null , jsonob.getString("request_time"));
					resultMembers.add(member);
				}
			}
			
		} catch (JSONException e) {
			resultMembers = null;
			e.printStackTrace();
		}
		return resultMembers;
	}
	
	public ArrayList<ArrayList<ChampionDTO>> decodeRankChamps(String jsonChamps){
		if(jsonChamps != null){
			ArrayList<ArrayList<ChampionDTO>> rankChampArr = new ArrayList<ArrayList<ChampionDTO>>();
			ArrayList<ChampionDTO> a = new ArrayList<ChampionDTO>();
	
			try {
				
				JSONArray jsonArr = new JSONArray(jsonChamps);
				
				if(jsonArr.length() < 5){
					
					for(int i = 0; i < jsonArr.length(); i++){
						JSONObject jsonOb = jsonArr.getJSONObject(i);
						a.add(new ChampionDTO(jsonOb.getInt("id"), jsonOb.getInt("count")));
						rankChampArr.add(a);
					}
					
				} else {
					
					for(int i = 0; i < 5; i++){
						JSONObject jsonOb = jsonArr.getJSONObject(i);
						a.add(new ChampionDTO(jsonOb.getInt("id"), jsonOb.getInt("count")));
						rankChampArr.add(a);
					}
					
				}
	
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return rankChampArr;
		} else {
			return null;
		}
	}
	
	public void findMore(final ViewGroup container, final LayoutInflater inflater) {
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(!result.trim().equals("[]")){
					jsonResult = result;
					ArrayList<MemberDTO> members = decodeJsonString(jsonResult);
					
					for(MemberDTO member : members){
						View cardView = makeResultCards(member, container, inflater);
						parentView.addView(cardView);
					}
					
				} else {
					
					LolloLActivity.lollolActivity.getRequestList();
					
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, getActivity(), null);
		
		qc.execute("getRequestList", Integer.toString(listNum));
	}
	
	public QueryConnection makeQueryConnection(final String num) {

		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				if(result == null || result.trim().equals("")){
					
				} else {
					String[] response = result.split(",");
					if(response[0].equals("newCouple")){

						PushServiceConnection pc = new PushServiceConnection(getActivity());
						pc.execute("sendGcmToPartner", num);

//						Log.i("status", response[1]);
						Toast.makeText(getActivity(), response[1] + "님과 듀오가 되었습니다!", Toast.LENGTH_LONG).show();
						
						LolloLActivity.lollolActivity.findMyDuo();
						LolloLActivity.lollolActivity.getRequestList();
						LolloLActivity.lollolActivity.getMatchedList();
						
					}else if(response[0].equals("Reject")){
//						Log.i("status", response[1]);
						Toast.makeText(getActivity(), "거절합니닷!", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, getActivity(), null);
		
		return qc;
	}
	
	private String getTimePassed(String request_time) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		
		try {
			
			ft.setLenient(false);
			Date requestDate = ft.parse(request_time);
			
			long requestTime = requestDate.getTime();
			long currTime = System.currentTimeMillis();
			long diff = currTime - requestTime;
			Date currDate = new Date(currTime);
			
			Log.i("Time", "RequestTime :"+requestTime);
			Log.i("Time", "currTime :"+currTime);
			Log.i("Time", "diff: "+diff);
			
			Log.i("Date", "currDate : "+ft.format(currDate));
			Log.i("Date", "requestDate :"+ft.format(requestDate));
			
			long day = diff/(1000*60*60*24);
			long hour = diff/(1000*60*60);
			long minute = diff/(1000*60);
			
			if(day >= 1){
				
				return Long.toString(day) + "일 전";
				
			} else if(hour >= 1){
				
				return Long.toString(hour) + "시간 전";
				
			} else {
				
				return Long.toString(minute) + "분 전";
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			return "시간 ERROR!";
		}
		
	}

}
