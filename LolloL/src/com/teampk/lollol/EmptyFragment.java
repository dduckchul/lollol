package com.teampk.lollol;

import com.teampk.lollol.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EmptyFragment extends Fragment{
	
	int page;
	
	public EmptyFragment(){};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.empty, container, false);
		
		TextView emptyMessage = (TextView)view.findViewById(R.id.empty_message);
		ImageButton emptyRefresh = (ImageButton)view.findViewById(R.id.empty_refresh);

		switch(page){
		
		case 0 : emptyMessage.setText(R.string.emptyFindMsg);
		break;
		
		case 1 : emptyMessage.setText(R.string.emptyRequestMsg);
		break;
		
		case 2 : emptyMessage.setText(R.string.emptyMatchedMsg);
		break;
		
		case 3 : emptyMessage.setText(R.string.limitFindMsg);
		break;
		
		default : break;
		}
		
		emptyRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				switch(page){
				case 0 : LolloLActivity.lollolActivity.findMyDuo();
				Toast.makeText(LolloLActivity.lollolActivity.getApplicationContext(), "새로고침 완료", Toast.LENGTH_SHORT).show();
				break;
				
				case 1 : LolloLActivity.lollolActivity.getRequestList();
				Toast.makeText(LolloLActivity.lollolActivity.getApplicationContext(), "새로고침 완료", Toast.LENGTH_SHORT).show();
				break;
				
				case 2 : LolloLActivity.lollolActivity.getMatchedList();
				Toast.makeText(LolloLActivity.lollolActivity.getApplicationContext(), "새로고침 완료", Toast.LENGTH_SHORT).show();
				break;
				
				default : break;
				
				}
			}
		});
		
		
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
}
