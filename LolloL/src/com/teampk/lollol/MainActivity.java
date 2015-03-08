package com.teampk.lollol;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.teampk.lollol.util.LolloLSharedPref;

public class MainActivity extends Activity {
	
	public static final int LOGIN = 0;
	public static final int SIGNIN = 1;
	public static final int FRAGMENT_COUNT = SIGNIN+1;
	
	public static MainActivity mainActivity;
	public Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mainActivity = MainActivity.this;
		setContentView(R.layout.main);
		
		FragmentManager fm = getFragmentManager();
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[SIGNIN] = fm.findFragmentById(R.id.signInFragment);
		FragmentTransaction transaction = fm.beginTransaction();
		
		for(int i = 0; i < fragments.length; i++){
			transaction.hide(fragments[i]);
		}

		transaction.commit();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showFragment(LOGIN, false);
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public void showFragment(int fragmentIndex, boolean addToBackStack){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		
		for(int i = 0; i < fragments.length; i++){
			if(i == fragmentIndex){
				transaction.show(fragments[i]);
			}else{
				transaction.hide(fragments[i]);
			}
		}
		
		if(addToBackStack){
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
}
