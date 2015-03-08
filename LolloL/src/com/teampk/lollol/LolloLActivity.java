package com.teampk.lollol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChampionDTO;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.dto.NavDrawerItem;
import com.teampk.lollol.util.DataBaseHandler;
import com.teampk.lollol.util.DBConnect.PushServiceConnection;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.SlideMenuClickListener;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;
import com.teampk.lollol.util.adapter.LollolPagerAdapter;
import com.teampk.lollol.util.adapter.NavDrawerListAdapter;

public class LolloLActivity extends FragmentActivity implements ActionBar.TabListener{
	
	public static LolloLSharedPref lollolSharedPref;
	public static LolloLActivity lollolActivity;
    public static DataBaseHandler db;

	private LollolPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	
	private String[] navMenuStrings;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public DrawerLayout mDrawerLayout;
	public View navigationDrawer;
	private ListView mDrawerList;
	private NavDrawerListAdapter adapter;
	private ImageView tier;
	private TextView summonerName;
	
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";

    private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private final static String TAG = "GCM Test";
    
	public GoogleCloudMessaging gcm;
	private String regid;
	
	@Override
	protected void onCreate(Bundle saveInstances) {
		super.onCreate(saveInstances);
        setContentView(R.layout.lollol);

		if(MainActivity.mainActivity != null){
			MainActivity.mainActivity.finish();
		}

        lollolSharedPref = new LolloLSharedPref(LolloLActivity.this);
		lollolActivity = LolloLActivity.this;
        lollolSharedPref.settingSharedPref();
		
	    if (checkPlayServices()) {
		
			updateMyStats();
			
			// Google Play Messging Starts
			
			gcm = GoogleCloudMessaging.getInstance(lollolActivity);
			regid = getRegistrationId(lollolActivity);
			
            if (regid.isEmpty()) {
                registerInBackground();
            }
            
            // DataBase To Insert Champ Data

            db = new DataBaseHandler(lollolActivity);
            db.initializeDB();

			navMenuStrings = getResources().getStringArray(R.array.nav_drawer_items);
			navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
			
			mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
			mDrawerList = (ListView)findViewById(R.id.list_slidemenu);
			navigationDrawer = (View)findViewById(R.id.left_drawer_view);
			
			tier = (ImageView)findViewById(R.id.drawer_header_image);
			summonerName= (TextView)findViewById(R.id.drawer_header_summoner);
			
			ToggleButton toggleBtn = (ToggleButton)findViewById(R.id.toggleBtn);
			toggleBtn.setChecked(lollolSharedPref.getPref("push", true));
			toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if(isChecked){
						lollolSharedPref.putPref("push", true);
					} else {
                        lollolSharedPref.putPref("push", false);
					}
				}
			});
			
			navDrawerItems = new ArrayList<NavDrawerItem>();
			navDrawerItems.add(new NavDrawerItem(navMenuStrings[0], navMenuIcons.getResourceId(0, -1)));
			navDrawerItems.add(new NavDrawerItem(navMenuStrings[1], navMenuIcons.getResourceId(1, -1)));
			navDrawerItems.add(new NavDrawerItem(navMenuStrings[2], navMenuIcons.getResourceId(2, -1)));
			navDrawerItems.add(new NavDrawerItem(navMenuStrings[3], navMenuIcons.getResourceId(3, -1)));
			navDrawerItems.add(new NavDrawerItem(navMenuStrings[4], navMenuIcons.getResourceId(4, -1)));

			navMenuIcons.recycle();
			
			adapter = new NavDrawerListAdapter(this, navDrawerItems);
			mDrawerList.setAdapter(adapter);
			mDrawerList.setOnItemClickListener(new SlideMenuClickListener(lollolActivity));
			final int[] Icons = new int[]{R.drawable.ic_action_find, R.drawable.ic_action_request, R.drawable.ic_action_matched};
			
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			
			viewPager = (ViewPager)findViewById(R.id.pager);
			pagerAdapter = new LollolPagerAdapter(getSupportFragmentManager());
			viewPager.setAdapter(pagerAdapter);
			viewPager.setOffscreenPageLimit(3);
			viewPager.setOnPageChangeListener(new SimpleOnPageChangeListener(){
			
				@Override
				public void onPageSelected(int position) {
					super.onPageSelected(position);
					actionBar.setSelectedNavigationItem(position);
				}
			});
			
			for(int i = 0; i < 3; i++){
				actionBar.addTab(actionBar.newTab()
						.setIcon(lollolActivity.getResources().getDrawable(Icons[i]))
						.setTabListener(this));
				
				
			}
			
			findMyDuo();
			getRequestList();
			getMatchedList();

	    } else {
	    	
            Log.i(TAG, "No valid Google Play Services APK found.");
            Toast.makeText(LolloLActivity.this, "Google Play Service를 이용할수 없습니다", Toast.LENGTH_LONG).show();
	    	
	    }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
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
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void updateMyStats(){
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(result.equals("1")){
					Toast.makeText(getApplication(), "전적 갱신 성공", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplication(), "전적 갱신 실패 : 잠시 후 시도하세요", Toast.LENGTH_SHORT).show();
				}
				
				Resources rc = getResources();
				
				int totalRank = lollolSharedPref.getPref("totalRank", -1);
				summonerName.setText(lollolSharedPref.getPref("summonerName", "Error"));
				String myTier = lollolSharedPref.getPref("tier", "Error").toLowerCase(Locale.US).trim();
				
				isUnranked(totalRank);
				
				if(!myTier.isEmpty() || myTier.equals("error")){
					tier.setImageDrawable(rc.getDrawable(rc.getIdentifier("tier_"+myTier, "drawable", "com.teampk.lollol")));
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, lollolActivity, null);
		
		qc.execute("updateMyStats");
	}
	
	public void findMyDuo() {
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(result.trim().equals("[]")){
					pagerAdapter.setEmptyPage(0);
					pagerAdapter.notifyDataSetChanged();
				} else if(result.trim().equals("limit")) {
					pagerAdapter.setErrorPage(0);
					pagerAdapter.notifyDataSetChanged();
				} else {
					pagerAdapter.setJsonResult(result, 0);
					pagerAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, lollolActivity, "상대방을 찾고 있습니다..");
		
		qc.execute("findMyDuo", 
				Integer.toString(lollolSharedPref.getPref("downToMyRank", 0)),
				Integer.toString(lollolSharedPref.getPref("upToMyRank", 0)),
				Integer.toString(lollolSharedPref.getPref("findPosition", 0)));
		
	}
	
	public void getRequestList(){
		
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				
				if(result.trim().equals("[]")){
					pagerAdapter.setEmptyPage(1);
					pagerAdapter.notifyDataSetChanged();
				} else if(result.trim().equals("limit")){
					pagerAdapter.setErrorPage(0);
					pagerAdapter.notifyDataSetChanged();
				} else {
					pagerAdapter.setJsonResult(result, 1);
					pagerAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
		}, lollolActivity, null);
	
		qc.execute("getRequestList", "0");
	}
	
	public void getMatchedList(){
		QueryConnection qc = new QueryConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {
				if(result.trim().equals("[]")){
					pagerAdapter.setEmptyPage(2);
					pagerAdapter.notifyDataSetChanged();
				} else {
					pagerAdapter.setJsonResult(result, 2);
					pagerAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onTaskComplete(boolean result) {}
			@Override
			public void onTaskComplete(MemberDTO member) {}
		}, lollolActivity, null);
	
		qc.execute("getMatchedList", "0");

	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context) {
		
	    String registrationId = lollolSharedPref.getPref(PROPERTY_REG_ID, "");
	    
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    
	    int registeredVersion = lollolSharedPref.getPref(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    
	    return registrationId;
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	private void registerInBackground() {
		
		AsyncTask<Void, Void, Void> registerAsync = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				try{
					if(gcm == null){
						gcm = GoogleCloudMessaging.getInstance(lollolActivity);
					}
					
					regid = gcm.register(getResources().getString(R.string.gcm_key));
					sendRegistrationIdToBackend(regid);
					storeRegistrationId(lollolActivity, regid);
					
				} catch (IOException e){
					Toast.makeText(lollolActivity, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show(); 
				}
				return null;
			}
		};
		
		registerAsync.execute(null, null, null);
		
	}

	protected void sendRegistrationIdToBackend(String regid) {
		
		PushServiceConnection pc = new PushServiceConnection(LolloLActivity.this);
		pc.execute("registerID", regid);
	}
	
	protected void storeRegistrationId(Context context, String regId) {

		int appVersion = getAppVersion(context);

	    lollolSharedPref.putPref(PROPERTY_REG_ID, regId);
        lollolSharedPref.putPref(PROPERTY_APP_VERSION, appVersion);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){

			if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){

				mDrawerLayout.closeDrawer(navigationDrawer);
			
			} else {
			
				mDrawerLayout.openDrawer(navigationDrawer);
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){

			mDrawerLayout.closeDrawer(navigationDrawer);
			
		} else {
			
			super.onBackPressed();
		}
	}

	public void isUnranked(int totalRank){
		if(totalRank == 0 && lollolSharedPref.getPref("unRankNoti", true)){
			final Dialog unRankNoti = new Dialog(LolloLActivity.this);
			Window window = unRankNoti.getWindow();
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			unRankNoti.setContentView(R.layout.popup_dialog_unranked);
			unRankNoti.setTitle(R.string.unranked_warning_title);
			unRankNoti.getWindow().setBackgroundDrawableResource(R.color.background);
			unRankNoti.show();
			
			CheckBox warning_check = (CheckBox)unRankNoti.findViewById(R.id.warning_checkbox);
			warning_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						lollolSharedPref.putPref("unRankNoti", false);
						unRankNoti.dismiss();
					}
				}
			});
		}
	}
}
