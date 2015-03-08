package com.teampk.lollol.util.DBConnect;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.teampk.lollol.MainActivity;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;

public class OpenFireConnection extends AsyncTask<String, Void, String>{

	private static final String HOST = "54.64.63.36";
	private static final int PORT = 5222;
	private static final String adConn = "http://"+HOST+"/openFire.php";
	
	private TaskListener listener;
	private Context context;
	private ProgressDialog progress;
	private String dialogMessage;
	
	private Gson gson = new Gson();
	private String result = null;

	public XMPPTCPConnection connection;

	public OpenFireConnection(){};
	public OpenFireConnection(TaskListener listener, Context context, String dialogMessage){
		this.listener = listener;
		this.context = context;
		this.dialogMessage = dialogMessage;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(dialogMessage == null){
			
		} else {
			progress = new ProgressDialog(context);
			progress.setCanceledOnTouchOutside(false);
			progress.setMessage(dialogMessage);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(result.contains("setting")){
			
			String[] setting = result.split(",");
			
			if(setting[1].equals("success")){

				listener.onTaskComplete(true);

			} else if(setting[1].equals("fail")){
				
				listener.onTaskComplete(false);
				
			}
			
		} else {
			
			listener.onTaskComplete(result);
			
		}
		
		if(dialogMessage == null){
			
		} else {
			progress.dismiss();
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(adConn);
		
		if(params[0].equals("signIn")){
			
			result = signInOpenFire(params, client, post);
			
		} else if(params[0].equals("settingConnections")){
			
			result = settingConnections(params);
			
		} else {
			
			
		}

		return result;
	}
	
	private String signInOpenFire(String[] param, HttpClient client, HttpPost post) {
		String xmppResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(LolloLSharedPref.getInstance(context).getPref("summonerName", null));
		
		postParams.add(new BasicNameValuePair("whatToDo", param[0]));
		postParams.add(new BasicNameValuePair("jsonParams", gson.toJson(sendParameter)));
		
		xmppResult = requestQuery(postParams, client, post);

		return xmppResult;	
	}
	
	private String settingConnections(String[] param) {
		
		ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT);
		connConfig.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPTCPConnection(connConfig);
		
		try {
			connection.connect();
			connection.login(param[1], "lollol_"+param[1]);
			
			Presence presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);

		} catch (Exception e) {
			e.printStackTrace();
			connection = null;
			return "setting,fail";
		}
		
		return "setting,success";
	}
	
	private String requestQuery(ArrayList<NameValuePair> postParams, HttpClient client, HttpPost post){
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			
			InputStream is = resEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line).append("\n");
			}
			is.close();
			
			String queryResult = sb.toString();
			
			if(queryResult == null || queryResult == ""){
				
				Log.i("Status", "queryResult null : " + queryResult);
				return null;

			}else{

				Log.i("Status", "queryResult notNull : " + queryResult);
				return queryResult;
			}

		} catch (Exception e) {
			Log.i("status", "Query error :" + e.getMessage());
			return null;
		}
	}
}
