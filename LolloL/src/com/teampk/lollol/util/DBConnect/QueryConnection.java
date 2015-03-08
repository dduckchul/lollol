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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.teampk.lollol.LolloLActivity;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.LolloLSharedPref;
import com.teampk.lollol.util.MyRiotApi;
import com.teampk.lollol.util.Listener.TaskListener;

public class QueryConnection extends AsyncTask<String, Void, String>{

	private TaskListener listener;
	private Context context;
	private ProgressDialog progress;
	private String dialogMessage;

	private Gson gson = new Gson();
	private String result = null;
	
	private static final String query = "http://54.64.63.36/query.php";
	
	public QueryConnection(TaskListener listener, Context context, String dialogMessage) {
		super();
		this.listener = listener;
		this.context = context;
		this.dialogMessage = dialogMessage;
	}
	
	@Override
	protected void onPreExecute() {
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
	protected String doInBackground(String... args) {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(query);
		
		if(args[0].equals("email")){
			
			result = checklollolMember(args, client, post);
			
		}else if(args[0].equals("findDuplicateId")){
			
			result = findDuplicateId(args, client, post);
			
		}else if(args[0].equals("checkmember")){
			
			result = checkmember(args, client, post);
		
		}else if(args[0].equals("updateMyStats")){
			
			result = updateMyStats(args, client, post);

		}else if(args[0].equals("findMyDuo")){
			
			result = findMyDuo(args, client, post);
			
		} else if(args[0].equals("selectMyPartner")){
			
			result = selectMyPartner(args, client, post);
			
		} else if(args[0].equals("getRequestList")){
			
			result = getRequestList(args, client, post);

		} else if(args[0].equals("responseToList")){
			
			result = responseToList(args, client, post);
			
		} else if(args[0].equals("getDetailInfo")){
			
			result = getDetailInfo(args, client, post);
			
		} else if(args[0].equals("getMatchedList")){
			
			result = getMatchedList(args, client, post);
			
		} else if(args[0].equals("deleteMyPartner")){
			
			result = deleteMyPartner(args, client, post);
			
		} else if(args[0].equals("updateMyAccount")){
			
			result = updateMyAccount(args, client, post);
			
		} else {
			
		}
		
		return result;
	}
	

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		listener.onTaskComplete(result);
		
		if(dialogMessage == null){
			
		} else {
			progress.dismiss();
		}
	}
	
	private String checklollolMember(String[] param, HttpClient client, HttpPost post){

		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(param[1])));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String findDuplicateId(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(param[1])));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String checkmember(String[] param, HttpClient client, HttpPost post) {

		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(param[1])));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}

	private String updateMyStats(String [] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		String rank_champs = "";
		String rank_champs_short = "";
		
		MyRiotApi myRiotApi = new MyRiotApi(context);
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
				
		int lolid = LolloLSharedPref.getInstance(context).getPref("userLolId", 0);
		int userNo = LolloLSharedPref.getInstance(context).getPref("userNo", 0);
		
		String summonerName = myRiotApi.getSummonerName(lolid);
		String[] leagueResult = myRiotApi.getMyLevelAndDivision(lolid);
		ArrayList<String> stats = myRiotApi.getMyRankedStats(lolid);
		int totalRank = myRiotApi.getMyTotalRank(leagueResult);
		
		if(summonerName.equals("Error")){
			return "Error";
		}

        LolloLSharedPref.getInstance(context).putPref("totalRank", totalRank);
        LolloLSharedPref.getInstance(context).putPref("summonerName", summonerName);
		
		String tier = leagueResult[0];
		String division = leagueResult[1];
		
		if(stats != null){
			rank_champs = stats.get(0);
			rank_champs_short = stats.get(1);
		}
		
		MemberDTO member = new MemberDTO(userNo, summonerName, tier, division, totalRank, rank_champs, rank_champs_short);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(member)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult.trim();
	}
	
	private String findMyDuo(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		String position = decodePosition(Integer.parseInt(param[3]));
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		sendParameter.add(param[2]);
		sendParameter.add(position);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String selectMyPartner(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String getRequestList(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String responseToList(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		sendParameter.add(param[2]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String getDetailInfo(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String getMatchedList(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;
	}
	
	private String deleteMyPartner(String[] param, HttpClient client, HttpPost post) {
		
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
		queryResult = requestQuery(postParams, client, post);
		
		return queryResult;	
	}
	
	private String updateMyAccount(String[] param, HttpClient client, HttpPost post) {
		String queryResult = null;
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		ArrayList<String> sendParameter = new ArrayList<String>();
		
		sendParameter.add(Integer.toString(LolloLSharedPref.getInstance(context).getPref("userNo", 0)));
		sendParameter.add(param[1]);
		sendParameter.add(param[2]);
		
		postParams.add(new BasicNameValuePair("query_for", param[0]));
		postParams.add(new BasicNameValuePair("queryJsonParams", gson.toJson(sendParameter)));
		
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
	
	private String decodePosition(int position){
		String strPosition = null;
		
		switch(position){
		case 0 : strPosition = "Top"; break;
		
		case 1 : strPosition = "Jungle"; break;
		
		case 2 : strPosition = "Mid"; break;
		
		case 3 : strPosition = "AD"; break;
		
		case 4 : strPosition = "Support"; break;
		
		case 5 : strPosition = "All"; break;
		
		default : strPosition = null; break;
		}
		
		return strPosition;
	}
}
