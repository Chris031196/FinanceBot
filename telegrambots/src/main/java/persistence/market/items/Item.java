package persistence.market.items;

import java.io.Serializable;

import functions.Function;
import persistence.Stringable;

public abstract class Item implements Stringable, Serializable{

	private static final long serialVersionUID = 1L;

	public enum TYPE {
		Plane ("Flugzeug", "Flugeuge", true), 
		Upgrade ("Upgrade", "Upgrades", true), 
		Stock ("Aktien", "Aktien", true), 
		Certificate ("Urkunde", "Urkunden", false);
		
		private final String singular;
		private final String plural;
		private final boolean buyable;
		
		TYPE(String singular, String plural, boolean buyable){
			this.singular = singular;
			this.plural = plural;
			this.buyable = buyable;
		}
		
		public boolean hasMarket() {
			return buyable;
		}
		
		public String getSingular(){
			return singular;
		}
		
		public String getPlural(){
			return plural;
		}
	}

	protected String name;
	protected TYPE type;
	protected double value;
	protected String description;
	protected Function function;

//	public static Item getNewItem(String name, TYPE type, double value, int chance, String description){
//		if(type == TYPE.Plane){
//			return new Plane(name, value, chance, description);
//		}
//		else if(type == TYPE.Upgrade){
//			return new Upgrade(name, value, chance, description);
//		}
//		else {
//			return new Item(name, type, value, description);
//		}
//	}
//
//	public static Item getNewItem(String itemString){
//		String[] stringArray = itemString.split(";");
//		String name = stringArray[0];
//		TYPE type = TYPE.valueOf(stringArray[1]);
//		double value = Double.parseDouble(stringArray[2]);
//		int chance = Integer.parseInt(stringArray[3]);
//		String description = "";
//		if(stringArray.length >= 5){
//			description = stringArray[4];
//		}
//		if(type == TYPE.Plane){
//			return new Plane(name, value, chance, description);
//		}
//		else if(type == TYPE.Upgrade){
//			return new Upgrade(name, value, chance, description);
//		}
//		else {
//			return new Item(name, type, value, description);
//		}
//	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TYPE getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

	public Function getFunction() {
		return function;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public abstract void setOptions(String[] options);
	
	public abstract String[] getOptionMessages();
	
	public abstract Item copy();
	
	public abstract String print();
//	{
//		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\n" +getDescription() + "\n\n";
//	}

//	public String toString(){
//		String out = name +";" +type + ";" + value +";" +chance +";" +description;
//		return out;
//	}
}
