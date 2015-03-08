package com.teampk.lollol.dto;

public class MemberDTO {
	
	public int num;
	public String id;
	public String summoner;
	public int lolid;
	public String my_pos;
	public String introduce;
	public String login_time;
	public int login_count;
	public String tier;
	public String division;
	public int totalrank;
	public String rank_champs;
	public String rank_champs_short;
	public int friends_count;
	public String request_time;
	
	public MemberDTO(int num, String id, String summoner, int lolid, String my_pos, 
			String introduce, String login_time, int login_count, String tier, String division, 
			int totalrank, String rank_champs, String rank_champs_short ,int friends_count, String request_time) {
		super();
		this.num = num;
		this.id = id;
		this.summoner = summoner;
		this.lolid = lolid;
		this.my_pos = my_pos;
		this.introduce = introduce;
		this.login_time = login_time;
		this.login_count = login_count;
		this.tier = tier;
		this.division = division;
		this.totalrank = totalrank;
		this.rank_champs = rank_champs;
		this.rank_champs_short = rank_champs_short;
		this.friends_count = friends_count;
		this.request_time = request_time;
	}
	
	public MemberDTO(int num, String summoner, String tier, String division, int totalrank, String rank_champs){
		super();
		this.num = num;
		this.tier = tier;
		this.division = division;
		this.totalrank = totalrank;
		this.rank_champs = rank_champs;
		this.summoner = summoner;
	}
	
	public MemberDTO(int num, String summoner, String tier, String division, int totalrank, String rank_champs, String rank_champs_short){
		super();
		this.num = num;
		this.tier = tier;
		this.division = division;
		this.totalrank = totalrank;
		this.rank_champs = rank_champs;
		this.rank_champs_short = rank_champs_short;
		this.summoner = summoner;
	}
	
	
	public MemberDTO(int num, String my_pos, String tier, String division, String rank_champs_short){
		super();
		this.num = num;
		this.my_pos = my_pos;
		this.tier = tier;
		this.division = division;
		this.rank_champs_short = rank_champs_short;
	}
	
	public MemberDTO(int num, String my_pos, String tier, String division, String rank_champs_short, String request_time){
		super();
		this.num = num;
		this.my_pos = my_pos;
		this.tier = tier;
		this.division = division;
		this.rank_champs_short = rank_champs_short;
		this.request_time = request_time;
	}
	
	public MemberDTO(int num, String summoner, String my_pos, String tier, String division, int totalrank){
		this.num = num;
		this.summoner = summoner;
		this.my_pos = my_pos;
		this.tier = tier;
		this.division = division;
		this.totalrank = totalrank;
	}
	
	public MemberDTO() {
		super();
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSummoner() {
		return summoner;
	}
	
	public void setSummoner(String summoner) {
		this.summoner = summoner;
	}
	
	public int getLolid() {
		return lolid;
	}
	
	public void setLolid(int lolid) {
		this.lolid = lolid;
	}
	
	public String getMy_pos() {
		return my_pos;
	}
	
	public void setMy_pos(String my_pos) {
		this.my_pos = my_pos;
	}
	
	public String getIntroduce() {
		return introduce;
	}
	
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public String getLogin_time() {
		return login_time;
	}
	
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	
	public int getLogin_count() {
		return login_count;
	}

	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}

	public String getTier() {
		return tier;
	}
	
	public void setTier(String tier) {
		this.tier = tier;
	}
	
	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getTotalrank() {
		return totalrank;
	}

	public void setTotalrank(int totalrank) {
		this.totalrank = totalrank;
	}

	public String getRank_champs() {
		return rank_champs;
	}

	public void setRank_champs(String rank_champs) {
		this.rank_champs = rank_champs;
	}
	
	public String getRank_champs_short() {
		return rank_champs_short;
	}

	public void setRank_champs_short(String rank_champs_short) {
		this.rank_champs_short = rank_champs_short;
	}

	public int getFriends_count() {
		return friends_count;
	}
	
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberDTO [num=");
		builder.append(num);
		builder.append(", id=");
		builder.append(id);
		builder.append(", summoner=");
		builder.append(summoner);
		builder.append(", lolid=");
		builder.append(lolid);
		builder.append(", my_pos=");
		builder.append(my_pos);
		builder.append(", introduce=");
		builder.append(introduce);
		builder.append(", login_time=");
		builder.append(login_time);
		builder.append(", login_count=");
		builder.append(login_count);
		builder.append(", tier=");
		builder.append(tier);
		builder.append(", division=");
		builder.append(division);
		builder.append(", totalrank=");
		builder.append(totalrank);
		builder.append(", rank_champs=");
		builder.append(rank_champs);
		builder.append(", rank_champs_short=");
		builder.append(rank_champs_short);
		builder.append(", friends_count=");
		builder.append(friends_count);
		builder.append(", request_time=");
		builder.append(request_time);
		builder.append("]");
		return builder.toString();
	}
}
