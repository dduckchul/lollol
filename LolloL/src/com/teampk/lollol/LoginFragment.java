package com.teampk.lollol;

import org.json.JSONException;
import org.json.JSONObject;

import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.ConfirmSummoner;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginFragment extends Fragment{
	
    public LolloLSharedPref lolloLSharedPref;

	private EditText lolid;
	private EditText lolpw;

	private CheckBox loginInfoCheckBox;
	private ImageButton verifyBtn;
	
	private String accountId;
	private String accountPw;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        lolloLSharedPref = new LolloLSharedPref(getActivity().getApplicationContext());

		View view = inflater.inflate(R.layout.login, container, false);
		
		lolid = (EditText)view.findViewById(R.id.user_lolid);
		lolpw = (EditText)view.findViewById(R.id.user_lolpw);
		
		loginInfoCheckBox = (CheckBox)view.findViewById(R.id.login_info_chkbox);
		verifyBtn = (ImageButton)view.findViewById(R.id.user_verifyBtn);
		
		if(lolloLSharedPref.getPref("lolAccountSaved", false)){
			accountId = lolloLSharedPref.getPref("lolAccountID", "noId");
			accountPw = lolloLSharedPref.getPref("lolAccountPW", "noPw");
			
			lolid.setText(accountId);
			lolpw.setText(accountPw);
			loginInfoCheckBox.setChecked(true);
		}
		
		verifyBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String inputId = lolid.getText().toString();
				String inputPw = lolpw.getText().toString();
				boolean saveInfo = loginInfoCheckBox.isChecked();
				
				checkLoginInfoVaild(saveInfo, inputId, inputPw);
			}
		});
		
		return view;
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
	
	private void checkLoginInfoVaild(boolean saveInfo, String inputId, String inputPw) {
		
		if(inputId.isEmpty()){
			CharSequence empty_id = getResources().getText(R.string.empty_id);
			Toast.makeText(getActivity(), empty_id, Toast.LENGTH_SHORT).show();
			
		} else if(inputPw.isEmpty()){
			CharSequence empty_pw = getResources().getText(R.string.empty_pw);
			Toast.makeText(getActivity(), empty_pw, Toast.LENGTH_SHORT).show();
			
		} else if(!inputId.isEmpty() && !inputPw.isEmpty()){

			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			
			if(saveInfo){
                lolloLSharedPref.putPref("lolAccountID", inputId);
                lolloLSharedPref.putPref("lolAccountPW", inputPw);
                lolloLSharedPref.putPref("lolAccountSaved", true);

			} else {
                lolloLSharedPref.putPref("lolAccountSaved", false);
			}
			
			confirmAccount(inputId, inputPw);
			
		} else {
			CharSequence error = getResources().getText(R.string.error);
			Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
		}
	}

	private void confirmAccount(String inputId, String inputPw) {
		
		ConfirmSummoner confirm = new ConfirmSummoner(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {}
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {
				
				if(member.getSummoner() != null){
					
					int userlolid = member.getLolid();
					String userSummonerName = member.getSummoner();
					
					checklollolMember(userlolid, userSummonerName);
					
				} else {
					
					String login_failed = getResources().getString(R.string.login_failed);
					Toast.makeText(getActivity(), login_failed, Toast.LENGTH_SHORT).show();
				}
			}
			
		}, getActivity());
		
		confirm.execute(inputId, inputPw);
	}
	
	private void checklollolMember(final int userlolid, final String userSummonerName){
		String async_login = getResources().getString(R.string.async_login);
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(result != null){
					
					try {
						
						JSONObject json = new JSONObject(result);
                        lolloLSharedPref.putPref("summonerName", json.getString("summonerName"));
                        lolloLSharedPref.putPref("userNo", json.getInt("memberNum"));
                        lolloLSharedPref.putPref("userLolId", json.getInt("lolId"));

						startActivity(new Intent(getActivity(), LolloLActivity.class));

					} catch (JSONException e) {
						Log.i("Status", "Json Exception : " + e.getMessage());
						String error_membercheck = getResources().getString(R.string.error_membercheck);
						Toast.makeText(getActivity(), error_membercheck, Toast.LENGTH_SHORT).show();
					}
					
				} else {
					SignInFragment fragment = (SignInFragment)MainActivity.mainActivity.fragments[MainActivity.SIGNIN];
					fragment.getArguments().putInt("userlolid", userlolid);
					fragment.getArguments().putString("userSummonerName", userSummonerName);
					fragment.settingSummonerName();
					MainActivity.mainActivity.showFragment(MainActivity.SIGNIN, false);
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, getActivity(), async_login);
		
		qc.execute("checkmember", Integer.toString(userlolid));
	}
}
