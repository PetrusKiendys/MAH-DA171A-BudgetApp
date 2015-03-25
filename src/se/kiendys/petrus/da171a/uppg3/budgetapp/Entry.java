package se.kiendys.petrus.da171a.uppg3.budgetapp;

public class Entry {
	private String date;
	private String type;
	private String amount;
	
	public Entry(String date, String type, String amount){
		this.date = date;
		this.type = type;
		this.amount = amount;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getAmount(){
		return this.amount;
	}
}
