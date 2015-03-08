package com.teampk.lollol.util.adapter;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teampk.lollol.R;
import com.teampk.lollol.InfoActivity;
import com.teampk.lollol.LolloLActivity;
import com.teampk.lollol.dto.MemberDTO;
import com.teampk.lollol.util.DBConnect.QueryConnection;
import com.teampk.lollol.util.Listener.TaskListener;

public class MatchedListAdapter extends ArrayAdapter<MemberDTO>{

	private Context context;
	private List<MemberDTO> objects;
	
	static class ViewHolder{
		public ImageView image;
		public TextView num;
		public TextView tier;
		public TextView division;
		public TextView pos;
		public TextView summoner;
		public ImageButton infoBtn;
		public ImageButton breakDuoBtn;
	}
	
	public MatchedListAdapter(Context context, int resource, List<MemberDTO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.matched_row, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.image = (ImageView)rowView.findViewById(R.id.image);
			viewHolder.num = (TextView)rowView.findViewById(R.id.num);
			viewHolder.tier = (TextView)rowView.findViewById(R.id.tier);
			viewHolder.division = (TextView)rowView.findViewById(R.id.division);
			viewHolder.pos = (TextView)rowView.findViewById(R.id.pos);
			viewHolder.summoner = (TextView)rowView.findViewById(R.id.summoner);
			viewHolder.infoBtn = (ImageButton)rowView.findViewById(R.id.infoBtn);
			viewHolder.breakDuoBtn = (ImageButton)rowView.findViewById(R.id.breakDuoBtn);
			rowView.setTag(viewHolder);
		}
		
		final ViewHolder holder = (ViewHolder)rowView.getTag();
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("듀오 삭제");
		alert.setMessage("삭제 하시겠습니까?");
		alert.setNegativeButton("Cancel", null);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				QueryConnection qc = new QueryConnection(new TaskListener() {
					
					@Override
					public void onTaskComplete(String result) {
						if(result != null){
							if(result.trim().equals("1")){
								Toast.makeText(context, "듀오 해제!", Toast.LENGTH_SHORT).show();
								objects.remove(position);
								notifyDataSetChanged();
								
								LolloLActivity.lollolActivity.findMyDuo();
								LolloLActivity.lollolActivity.getRequestList();
								LolloLActivity.lollolActivity.getMatchedList();
								
							} else {
								Toast.makeText(context, "삭제 실패!", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "삭제 실패!", Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onTaskComplete(boolean result) {}
					@Override
					public void onTaskComplete(MemberDTO member) {}
				}, context, null);

				qc.execute("deleteMyPartner", holder.num.getText().toString());
			}
		});
		
		String tierStr = objects.get(position).getTier().toLowerCase(Locale.US);
		int imgResource = context.getResources().getIdentifier("tier_"+tierStr, "drawable", "com.teampk.lollol");

		holder.image.setImageResource(imgResource);
		holder.num.setText(Integer.toString(objects.get(position).getNum()));
		holder.tier.setText(objects.get(position).getTier());
		holder.division.setText(objects.get(position).getDivision());
		holder.pos.setText(objects.get(position).getMy_pos());
		holder.summoner.setText(objects.get(position).getSummoner());
		holder.infoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent info = new Intent(context, InfoActivity.class);
				info.putExtra("userNo", Integer.toString(objects.get(position).getNum()));
				info.putExtra("userName", (objects.get(position).getSummoner()));
				context.startActivity(info);
			}
		});
		
		holder.breakDuoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alert.show();
			}
		});
		
		holder.infoBtn.setFocusable(false);
		holder.breakDuoBtn.setFocusable(false);
		
		return rowView;
	}
}
