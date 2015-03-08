package com.teampk.lollol.util;

import java.util.Comparator;

import com.teampk.lollol.dto.ChampionDTO;

public class ListCompare implements Comparator<ChampionDTO>{
	
	@Override
	public int compare(ChampionDTO lhs, ChampionDTO rhs) {
		return lhs.getCount() > rhs.getCount() ? -1 : lhs.getCount() < rhs.getCount() ? 1 : 0;
	}

}
