package com.teampk.lollol.util.DBConnect;

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

import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.Listener.TaskListener;

public class SignInConnection extends AsyncTask<MemberDTO, Void, Boolean>{
	
	private static final String signInPage = "http://54.64.63.36/signIn.php";
	
	private TaskListener listener;
	private ProgressDialog progress;
	private Context context;
	
	private boolean result = false;
	
	public SignInConnection(TaskListener listener, Context context){
		this.listener = listener;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = new ProgressDialog(context);
		progress.setCanceledOnTouchOutside(false);
		progress.setMessage("가입 처리 중입니다...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		progress.show();
	}
	
	@Override
	protected Boolean doInBackground(MemberDTO... args) {

		try{
			MemberDTO memberInfo = args[0];

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(signInPage);
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userLoLId", Integer.toString(memberInfo.getLolid())));
			params.add(new BasicNameValuePair("summonerName", memberInfo.getSummoner()));
			params.add(new BasicNameValuePair("my_pos", memberInfo.getMy_pos()));
			params.add(new BasicNameValuePair("introduce", memberInfo.getIntroduce()));
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			
			if(resEntity != null){
				result = true;
			} else {
				result = false;
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}

		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		listener.onTaskComplete(result.booleanValue());
		progress.dismiss();
	}
}
