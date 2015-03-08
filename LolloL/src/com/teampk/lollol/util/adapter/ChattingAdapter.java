package com.teampk.lollol.util.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChattingMessageDTO;

public class ChattingAdapter extends ArrayAdapter<ChattingMessageDTO>{
	
	private LinearLayout wrapperLayout;
	private TextView texts;
	private TextView name;

	private ArrayList<ChattingMessageDTO> messages;
	private Context context;
	
	public ChattingAdapter(Context context, int textViewResource, ArrayList<ChattingMessageDTO> messages) {
		super(context, textViewResource);
		this.context = context;
		this.messages = messages;
	}
	
	@Override
	public void add(ChattingMessageDTO object) {
		messages.add(object);
		super.add(object);
	}
	
	@Override
	public ChattingMessageDTO getItem(int position) {
		return this.messages.get(position);
	}
	
	@Override
	public int getCount() {
		return this.messages.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int gravity;
		int image;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.chatting_lists, parent, false);
		}
		
		wrapperLayout = (LinearLayout)convertView.findViewById(R.id.wrapperLayout);
		texts = (TextView)convertView.findViewById(R.id.texts);
		name = (TextView)convertView.findViewById(R.id.name);
		
		ChattingMessageDTO message = getItem(position);

		if(message.isLeft()){
			gravity = Gravity.LEFT;
			image = R.drawable.bubble_blue;
		} else {
			gravity = Gravity.RIGHT;
			image = R.drawable.bubble_blue_darker;
		}
		
		texts.setText(message.getMessage());
		texts.setBackgroundResource(image);
		name.setText(message.getName());
		
		wrapperLayout.setGravity(gravity);
		
		return convertView;
	}

}
