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

import com.google.gson.Gson;
import com.teampk.lollol.LolloLActivity;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PushServiceConnection extends AsyncTask<String, Void, String>{

	private Context context;
	private TaskListener tasklistener;
	private Gson gson = new Gson();
	
	private static final String pushService = "http://54.64.63.36/pushService.php";
	
	public PushServiceConnection() {}
	
	public PushServiceConnection(Context context){
		this.context = context;
	}
	
	public PushServiceConnection(TaskListener tasklistener, Context context){
		this.tasklistener = tasklistener;
		this.context = context;
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {
		
		String result = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(pushService);
		
		if(params[0].equals("registerID")){
			
			result = registerID(client, post, params);
			
		} else if(params[0].equals("sendGcmToPartner")){
			
			result = sendGcmToPartner(client, post, params);
			
		} else {
			
		}

		return result;
	}
	
	private String registerID(HttpClient client, HttpPost post, String[] param) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("whatToDo", param[0]));
		postParams.add(new BasicNameValuePair("jsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String sendGcmToPartner(HttpClient client, HttpPost post, String[] param) {

		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(LolloLSharedPref.getInstance(context).getPref("summonerName", "Error"));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("whatToDo", param[0]));
		postParams.add(new BasicNameValuePair("jsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
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
