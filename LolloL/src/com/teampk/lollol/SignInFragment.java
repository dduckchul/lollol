package com.teampk.lollol;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.OpenFireConnection;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.DBConnect.SignInConnection;
import com.teampk.lollol.util.Listener.NewImageClickListener;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;

public class SignInFragment extends Fragment{
	
	private int userlolid = 0;
	private String summonerName = "aaa";
	
	private TextView user_summonerNameView;
	private TextView selected_position;
	private Button signInBtn;
    private Button backToMain;
	private EditText introduce;
	private RelativeLayout posImages; 
//	private CheckBox[] checkBoxes = new CheckBox[6];
	private ImageView [] images = new ImageView[6];
	
	public SignInFragment(){
		super();
		setArguments(new Bundle());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.signin, container, false);

		user_summonerNameView = (TextView)view.findViewById(R.id.user_summonerName);
		selected_position = (TextView)view.findViewById(R.id.selected_position);
		introduce = (EditText)view.findViewById(R.id.introduce_text);
		posImages = (RelativeLayout)view.findViewById(R.id.posImages);
		signInBtn = (Button)view.findViewById(R.id.user_signInBtn);
        backToMain = (Button)view.findViewById(R.id.user_backToMainBtn);
		
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
		final NewImageClickListener imgListener = new NewImageClickListener(colorFilter, getActivity(), selected_position);
		
		for(int i = 0; i < posImages.getChildCount(); i++){
			images[i] = (ImageView)posImages.getChildAt(i);
			images[i].getDrawable().setColorFilter(colorFilter);
			images[i].setOnClickListener(imgListener);
		}
		
		signInBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				signInBtn.setEnabled(false);
				String position = selected_position.getText().toString();
				String introduce_text = introduce.getText().toString();
				
				MemberDTO memberInfo = new MemberDTO();
				memberInfo.setSummoner(summonerName);
				memberInfo.setLolid(userlolid);
				memberInfo.setMy_pos(position);
				memberInfo.setIntroduce(introduce_text);
				
				signInToLolloL(memberInfo);
			}
		});

        backToMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.mainActivity.showFragment(MainActivity.LOGIN, false);

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

	private void signInToLolloL(MemberDTO memberInfo) {
		SignInConnection connection = new SignInConnection(new TaskListener(){
			
			@Override
			public void onTaskComplete(MemberDTO member) {}
			@Override
			public void onTaskComplete(String result) {}
			@Override
			public void onTaskComplete(boolean result) {
				
				if(result == true){
					
					getMyInfo();
					
				} else if(result == false) {
					String signin_failed = getResources().getString(R.string.signin_failed);
					Toast.makeText(getActivity(), signin_failed, Toast.LENGTH_SHORT).show();
					signInBtn.setEnabled(true);
				}
			}

		}, getActivity());
		
		connection.execute(memberInfo);
	}
	
	private void getMyInfo(){
		
		String signin = getResources().getString(R.string.signin);
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(result != null){
					try {
						
						JSONObject json = new JSONObject(result);
						LolloLSharedPref.getInstance(getActivity().getApplicationContext()).putPref("summonerName", json.getString("summonerName"));
                        LolloLSharedPref.getInstance(getActivity().getApplicationContext()).putPref("userNo", json.getInt("memberNum"));
                        LolloLSharedPref.getInstance(getActivity().getApplicationContext()).putPref("userLolId", json.getInt("lolId"));

						signInChat();
						
					} catch (JSONException e) {
						Log.i("Status", "Json Exception : " + e.getMessage());
						String error_membercheck = getResources().getString(R.string.error_membercheck);
						Toast.makeText(getActivity(), error_membercheck, Toast.LENGTH_SHORT).show();
						signInBtn.setEnabled(true);
					}
					
				}else{
					Log.i("Status", "result : "+result);
					String error_membercheck = getResources().getString(R.string.error_membercheck);
					Toast.makeText(getActivity(), error_membercheck, Toast.LENGTH_SHORT).show();
					signInBtn.setEnabled(true);

				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, getActivity(), signin);
		
		qc.execute("checkmember", Integer.toString(userlolid));
	}
	
	private void signInChat(){
		
		String signin_chat = getResources().getString(R.string.signin_chat);
		OpenFireConnection oc = new OpenFireConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				if(result.trim().equals("Success")){
					startActivity(new Intent(getActivity(), LolloLActivity.class));
				} else {
					String signin_chat_failed = getResources().getString(R.string.signin_chat_failed);
					Toast.makeText(getActivity(), signin_chat_failed, Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), LolloLActivity.class));
				}
			}
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, getActivity(), signin_chat);
		
		oc.execute("signIn");
	}
	
	public void settingSummonerName(){
		
		Bundle bundle = this.getArguments();
		userlolid = bundle.getInt("userlolid", 0);
		summonerName = bundle.getString("userSummonerName");
		
		user_summonerNameView.setText(summonerName);
	}
}
