package com.teampk.lollol.util.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}
	
	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
		}
		
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
		TextView titleTxt = (TextView)convertView.findViewById(R.id.title);
		
		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		titleTxt.setText(navDrawerItems.get(position).getTitle());

		return convertView;
	}
	
}
