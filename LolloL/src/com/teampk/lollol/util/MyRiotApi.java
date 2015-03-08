package com.teampk.lollol.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

import com.google.gson.Gson;
import com.teampk.lollol.LolloLActivity;
import com.teampk.lollol.MainActivity;
import com.teampk.lollol.dto.ChampionDTO;

import constant.Region;
import constant.Season;
import dto.League.League;
import dto.Stats.ChampionStats;
import dto.Stats.RankedStats;

public class MyRiotApi {
	
	public static final String apiKey = "f77a9807-f8e7-42dd-bdcd-60ca3b4dc4d0";
	public static RiotApi RIOTAPI = new RiotApi(apiKey, Region.KR);

    private Context context;
	public Gson gson = new Gson();

    public MyRiotApi(Context context){
        this.context = context;
    }
	
	public String getSummonerName(int lolid){

		String result = null;
		RIOTAPI.setSeason(Season.Season4);
		
		try{
			
			Map<String, String> name = RIOTAPI.getSummonerName(lolid);
			String resultKey = name.keySet().iterator().next();
			result = name.get(resultKey);
			
		} catch (RiotApiException e) {
			return "Error";
		}
		
		return result;
	}
	
	public String[] getMyLevelAndDivision(int lolid){
		
		String[] result;
		RIOTAPI.setSeason(Season.Season4);
		
		try {
			List<League> league = RIOTAPI.getLeagueEntryBySummoners(lolid).get(Integer.toString(lolid));
			String tier = league.get(0).getTier();
			String division = league.get(0).getEntries().get(0).getDivision();
			result = new String[]{tier, division};
			
		} catch (RiotApiException e) {
			
			result = new String[]{"unRanked", "unRanked"};
		}
		
		return result;
	}
	
	public ArrayList<String> getMyRankedStats(int lolid){
		
		RIOTAPI.setSeason(Season.Season4);
		
		ArrayList<ChampionDTO> arrayList = new ArrayList<ChampionDTO>();
		ArrayList<ChampionDTO> arrayListShort = new ArrayList<ChampionDTO>();
		
		ArrayList<String> result = new ArrayList<String>();
		
		String rank_champs;
		String rank_champs_short;
		
		try {
			
			RankedStats myStats = RIOTAPI.getRankedStats(lolid);
			
			for(ChampionStats stats : myStats.getChampions()){
				if(stats.getId() != 0){
					int champId = stats.getId();
					int champPlayed = stats.getStats().getTotalSessionsPlayed();
					
					arrayList.add(new ChampionDTO(champId, champPlayed));
				}
			}
			
			Collections.sort(arrayList, new ListCompare());
			
			int arrayListSize = arrayList.size();
			
			if(arrayListSize < 5){
				
				for(int i = 0; i < arrayListSize; i++){
					arrayListShort.add(i, arrayList.get(i));
				}
				
			} else {
				
				for(int i = 0; i < 5; i++){
					arrayListShort.add(i, arrayList.get(i));
				}
			}
			
			rank_champs = gson.toJson(arrayList);
			rank_champs_short = gson.toJson(arrayListShort);
			
			result.add(rank_champs);
			result.add(rank_champs_short);

		} catch (RiotApiException e) {
			
			result = null;
//			Log.i("status", "RiotApiException : " + e.getMessage());

		}
		
		return result;
	}
	
	public int getMyTotalRank(String[] league){
		int totalRank = 0, tier = 0, division = 0;
		
		if(league.length != 0){

            LolloLSharedPref.getInstance(context).putPref("tier", league[0]);

			switch(league[0]){
			
			case "BRONZE" : tier=0; break;
			case "SILVER" : tier=1; break;
			case "GOLD" : tier=2; break;
			case "PLATINUM" : tier=3; break;
			case "DIAMOND" : tier=4; break;
			case "MASTER" : tier=5; break;
			case "CHALLENGER" : tier=6; break;
			default : tier=0; break;
			
			}
			
			switch(league[1]){
			
			case "V" : division=1; break;
			case "IV" : division=2; break;
			case "III" : division=3; break;
			case "II" : division=4; break;
			case "I" : division=5; break;
			default : division=0; break;
			
			}
			
			totalRank = (tier * 5) + division;
			
		}
		
		return totalRank;
	}
	
}
