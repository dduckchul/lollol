package com.teampk.lollol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.adapter.MatchedListAdapter;

public class MatchedFragment extends ListFragment{
	
	public String jsonResult;
	public int num;
	
	public MatchedFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.matched, container, false);

        jsonResult = getArguments().getString("jsonString");
		
		if(jsonResult != null){
			
			ArrayList<MemberDTO> members = decodeJsonString(jsonResult);
			ArrayAdapter<MemberDTO> adapter = new MatchedListAdapter(getActivity(), R.layout.matched, members);
			setListAdapter(adapter);

			TextView matched_total = (TextView)view.findViewById(R.id.matched_total);
			matched_total.setText(" ("+Integer.toString(adapter.getCount())+" / 100)");
			
		}
		
		return view;
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MemberDTO member = (MemberDTO) getListAdapter().getItem(position);
		Intent info = new Intent(getActivity(), ChattingActivity.class);
		info.putExtra("partnerNo", Integer.toString(member.getNum()));
		info.putExtra("partnerName", member.getSummoner());
		startActivity(info);
	}
	
	public ArrayList<MemberDTO> decodeJsonString(String jsonResult){
		
		ArrayList<MemberDTO> resultMembers = new ArrayList<MemberDTO>();
		
		try {
			JSONArray jsonarr = new JSONArray(jsonResult);
			for(int i = 0; i < jsonarr.length(); i++){
				JSONObject jsonob = jsonarr.getJSONObject(i);
				MemberDTO member = new MemberDTO(jsonob.getInt("num"), jsonob.getString("summoner"), 
							jsonob.getString("my_pos"), jsonob.getString("tier"), jsonob.getString("division"), jsonob.getInt("totalrank"));
				resultMembers.add(member);
			}
			
		} catch (JSONException e) {
			resultMembers = null;
			e.printStackTrace();
		}

		return resultMembers;
	}
}
