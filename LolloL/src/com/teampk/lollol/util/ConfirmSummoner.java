package com.teampk.lollol.util;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.Listener.TaskListener;

public class ConfirmSummoner extends AsyncTask<String, Void, MemberDTO>{
	
	private static final String HOST = "chat.kr.lol.riotgames.com";
	private static final int PORT = 5223;
	private static final String SERVICE = "pvp.net";
	
	private String summonerName = null;
	
	private ProgressDialog progress;
	private Context context;
	private TaskListener listener = null;
	
	public ConfirmSummoner(TaskListener listener, Context context){
		this.context = context;
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = new ProgressDialog(context);
		progress.setCanceledOnTouchOutside(false);
		progress.setMessage("LoL 계정 인증중입니다..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		progress.show();
	}
	
	@Override
	protected MemberDTO doInBackground(String... params) {
		
		ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
		connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		connConfig.setSocketFactory(SSLSocketFactory.getDefault());
		connConfig.setCompressionEnabled(true);
		XMPPTCPConnection connection = new XMPPTCPConnection(connConfig);

		String userId = null;
		Long userIdtoLong = null;
		MemberDTO memberInfo = new MemberDTO();
		
		try{
			connection.connect();
			connection.login(params[0], "AIR_"+params[1]);
			
			if(connection.isAuthenticated()){
				
				userId = connection.getUser();
				
				int i = userId.indexOf("@");
				userId = userId.substring(3, i);
				userIdtoLong = Long.parseLong(userId);

				Map<String, String> summonerInfo = MyRiotApi.RIOTAPI.getSummonerName(userIdtoLong);
				summonerName = summonerInfo.get(Long.toString(userIdtoLong));
				memberInfo.setSummoner(summonerName);
				memberInfo.setLolid(userIdtoLong.intValue());
				
				connection.disconnect();
				connection = null;
			}
		}catch(Exception e){
			if(connection != null){
				try {
					connection.disconnect();
					Log.i("Status", "Log in Exception " + e.toString());
				} catch (NotConnectedException e1) {
					e1.printStackTrace();
				}
			} else {
				Log.i("Status", "Connection Exception " + e.toString());
			}
			connection = null;
		}
		
		return memberInfo;
	}
	
	@Override
	protected void onPostExecute(MemberDTO memberInfo) {
		super.onPostExecute(memberInfo);
		listener.onTaskComplete(memberInfo);
		progress.dismiss();
	}
}
