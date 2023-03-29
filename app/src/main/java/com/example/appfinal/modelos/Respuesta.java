package com.example.appfinal.modelos;

public class Respuesta{
	private String accountId;
	private int profileIconId;
	private long revisionDate;
	private String name;
	private String puuid;
	private String id;
	private int summonerLevel;

	public void setAccountId(String accountId){
		this.accountId = accountId;
	}

	public String getAccountId(){
		return accountId;
	}

	public void setProfileIconId(int profileIconId){
		this.profileIconId = profileIconId;
	}

	public int getProfileIconId(){
		return profileIconId;
	}

	public void setRevisionDate(long revisionDate){
		this.revisionDate = revisionDate;
	}

	public long getRevisionDate(){
		return revisionDate;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPuuid(String puuid){
		this.puuid = puuid;
	}

	public String getPuuid(){
		return puuid;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSummonerLevel(int summonerLevel){
		this.summonerLevel = summonerLevel;
	}

	public int getSummonerLevel(){
		return summonerLevel;
	}

	@Override
 	public String toString(){
		return 
			"Respuesta{" + 
			"accountId = '" + accountId + '\'' + 
			",profileIconId = '" + profileIconId + '\'' + 
			",revisionDate = '" + revisionDate + '\'' + 
			",name = '" + name + '\'' + 
			",puuid = '" + puuid + '\'' + 
			",id = '" + id + '\'' + 
			",summonerLevel = '" + summonerLevel + '\'' + 
			"}";
		}
}
