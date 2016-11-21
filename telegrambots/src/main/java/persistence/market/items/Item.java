package persistence.market.items;

import functions.Function;
import functions.stock.StockmarketController;
import persistence.Stringable;

public abstract class Item implements Stringable {

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
	public static Item stringToItem(String saveString){
		String[] stringArray = saveString.split(NEXT);
		String type = stringArray[1];
		Item item = null;
		switch(type){
		case "Plane":
			item = new Plane("", 0, "", 0);
			item.stringToObject(saveString);
			break;

		case "Upgrade":
			item = new Upgrade("", 0, "", 0);
			item.stringToObject(saveString);
			break;

		case "Stock":
			item = new Stock("", 0, 0, 0);
			item.stringToObject(saveString);
			((Stock) item).register();
			break;
			
		case "Certificate":
			item = new Certificate(null);
			item.stringToObject(saveString);
			break;
		}
		
		return item;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public abstract String getShort();

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
