package com.teampk.lollol;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChattingMessageDTO;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.OpenFireConnection;
import com.teampk.lollol.util.Listener.TaskListener;
import com.teampk.lollol.util.LolloLSharedPref;
import com.teampk.lollol.util.adapter.ChattingAdapter;

public class ChattingActivity extends Activity{

	private OpenFireConnection oc;
	private XMPPTCPConnection connection;
	private ChattingAdapter adapter;
	
	private ListView listMessages;
	private EditText inputText;
	private Button sendTextBtn;
	
	private String partnerNo;
	private String partnerName;
	private String myUserNo;
	private String myName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        LolloLSharedPref lolloLSharedPref = new LolloLSharedPref(ChattingActivity.this);

		Intent intent = getIntent();
		partnerNo = intent.getStringExtra("partnerNo");
		partnerName = intent.getStringExtra("partnerName");
		myUserNo = Integer.toString(lolloLSharedPref.getPref("userNo", -1));
		myName = lolloLSharedPref.getPref("summonerName", null);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Chat With " + partnerName);

		oc = new OpenFireConnection(new TaskListener() {
			
			@Override
			public void onTaskComplete(String result) {}
			@Override
			public void onTaskComplete(boolean result) {
				
				if(result == true){
					connection = oc.connection;
					setPacketListener(connection);
					Toast.makeText(ChattingActivity.this, "채팅 서버에 연결!", Toast.LENGTH_LONG).show();
					
				}else if(result == false){
					connection = null;
					setPacketListener(null);
					Toast.makeText(ChattingActivity.this, "채팅 서버에 연결할 수 없습니다", Toast.LENGTH_LONG).show();
				}
				
			}
			
			@Override
			public void onTaskComplete(MemberDTO member) {}
			
		}, ChattingActivity.this, null);
		
		setContentView(R.layout.chatting);
		listMessages = (ListView)findViewById(R.id.listMessages);
		inputText = (EditText)findViewById(R.id.inputText);
		sendTextBtn = (Button)findViewById(R.id.sendTextBtn);

		adapter = new ChattingAdapter(ChattingActivity.this, R.layout.chatting_lists, new ArrayList<ChattingMessageDTO>());
		listMessages.setAdapter(adapter);
		
		sendTextBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = inputText.getText().toString();
				
				if(!text.isEmpty()){
					
					Message msg = new Message(partnerNo+"@teampk.co.kr", Message.Type.chat);
					msg.setBody(text);
					
					if(connection != null){
						try {
							connection.sendPacket(msg);
							ChattingMessageDTO chattingMessage = new ChattingMessageDTO(myName, text, false);
							adapter.add(chattingMessage);
							listMessages.setSelection(adapter.getCount()-1);
							inputText.setText(null);
							
						} catch (NotConnectedException e) {
							Toast.makeText(ChattingActivity.this, "채팅 서버에 연결할 수 없습니다", Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		oc.execute("settingConnections", myUserNo);
	}
	

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(connection != null){
			
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						connection.disconnect();
					} catch (NotConnectedException e) {
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
		}
	}
	
	private void setPacketListener(XMPPTCPConnection connection) {
		this.connection = connection;
		if(connection!= null){
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet packet) throws NotConnectedException {
					Message message = (Message) packet;
					if(message.getBody() != null){
						String messageBody = message.getBody();
						
						final ChattingMessageDTO chattingMessage = new ChattingMessageDTO(partnerName, messageBody, true);
						
						Runnable runnable =  new Runnable() {
							public void run() {
								adapter.add(chattingMessage);
								listMessages.setSelection(adapter.getCount()-1);
							}
						};
						runOnUiThread(runnable);
					}
				}
			}, filter);
		}
	}
}
