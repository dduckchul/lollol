package com.teampk.lollol.dto;

public class ChampionDTO {
	
	private int id;
	private int count;
	private String name;
	private String imgSrc;
	
	public ChampionDTO() {
		super();
	}
	
	public ChampionDTO(int id, int count) {
		super();
		this.id = id;
		this.count = count;
	}
	
	public ChampionDTO(int id, int count, String name){
		this.id = id;
		this.count = count;
		this.name = name;
	}
	
	public ChampionDTO(int id, String name, String imgSrc){
		super();
		this.id = id;
		this.name = name;
		this.imgSrc = imgSrc;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChampionDTO [id=");
		builder.append(id);
		builder.append(", count=");
		builder.append(count);
		builder.append(", name=");
		builder.append(name);
		builder.append(", imgSrc=");
		builder.append(imgSrc);
		builder.append("]");
		return builder.toString();
	}

}
