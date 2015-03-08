package com.teampk.lollol.dto;

public class MatchingDTO {
	
	public int num;
	public int userNo;
	public int partnerNo;
	public String request_time;
	public String response_time;
	public int match_status;
	
	public MatchingDTO(int num, int userNo, int partnerNo, String request_time,
			String response_time, int match_status) {
		super();
		this.num = num;
		this.userNo = userNo;
		this.partnerNo = partnerNo;
		this.request_time = request_time;
		this.response_time = response_time;
		this.match_status = match_status;
	}
	
	public MatchingDTO() {
		super();
	}

	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public int getuserNo() {
		return userNo;
	}
	
	public void setuserNo(int userNo) {
		this.userNo = userNo;
	}
	
	public int getpartnerNo() {
		return partnerNo;
	}
	
	public void setpartnerNo(int partnerNo) {
		this.partnerNo = partnerNo;
	}
	
	public String getRequest_time() {
		return request_time;
	}
	
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	
	public String getResponse_time() {
		return response_time;
	}
	
	public void setResponse_time(String response_time) {
		this.response_time = response_time;
	}
	
	public int getMatch_status() {
		return match_status;
	}
	
	public void setMatch_status(int match_status) {
		this.match_status = match_status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MatchingDTO [num=");
		builder.append(num);
		builder.append(", userNo=");
		builder.append(userNo);
		builder.append(", partnerNo=");
		builder.append(partnerNo);
		builder.append(", request_time=");
		builder.append(request_time);
		builder.append(", response_time=");
		builder.append(response_time);
		builder.append(", match_status=");
		builder.append(match_status);
		builder.append("]");
		return builder.toString();
	}
}
