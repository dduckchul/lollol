package com.teampk.lollol.util.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChampionDTO;

public class RankStatsListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<ChampionDTO> championDTO;
	private int totalCount;
	
	public RankStatsListAdapter(Context context, ArrayList<ChampionDTO> championDTO, int totalCount){
		this.context = context;
		this.championDTO = championDTO;
		this.totalCount = totalCount;
	}
	
	@Override
	public int getCount() {
		return championDTO.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		return championDTO.get(0);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Resources rc = context.getResources();
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rank_champ_array_all, parent, false);
		}
		
		ImageView champImg = (ImageView)convertView.findViewById(R.id.champ_img);
		ImageView champCountGraph = (ImageView)convertView.findViewById(R.id.champ_count_graph);
		TextView champCountPer = (TextView)convertView.findViewById(R.id.champ_count_percent);
		TextView champCountInt = (TextView)convertView.findViewById(R.id.champ_count_integer);
		
		String champ_id = Integer.toString(championDTO.get(position).getId());
		int champ_count = championDTO.get(position).getCount();
		int top_champ_count = championDTO.get(0).getCount();
		double champ_select_per = (champ_count * 1000) / (double)totalCount ;
		double champ_select_per_round = Math.round(champ_select_per) / (double)10;
		
		float widthDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, rc.getDisplayMetrics());
		int width = (Math.round(widthDp) * champ_count) / top_champ_count;

		if(rc.getIdentifier("champ_"+champ_id, "drawable", "com.teampk.lollol") != 0){
            champImg.setImageDrawable(rc.getDrawable(rc.getIdentifier("champ_"+champ_id, "drawable", "com.teampk.lollol")));
        } else {
            champImg.setImageDrawable(rc.getDrawable(rc.getIdentifier("champ_empty", "drawable", "com.teampk.lollol")));
        }

        GradientDrawable gd = makeRoundRectDrawable(width, 50);
		
		champCountGraph.setImageDrawable(gd);
		champCountPer.setText(Double.toString(champ_select_per_round)+"%");
		champCountInt.setText(Long.toString(champ_count)+"회");
		
		return convertView;
	}

	private GradientDrawable makeRoundRectDrawable(int width, int height) {
		
		GradientDrawable gd = (GradientDrawable)context.getResources().getDrawable(R.drawable.shape_graph);
		gd.setSize(width, height);
		
		return gd;
	}

}
