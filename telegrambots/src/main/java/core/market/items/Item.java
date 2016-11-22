package core.market.items;

import addons.Function;
import addons.TYPE;
import addons.planes.items.Plane;
import addons.planes.items.Upgrade;
import addons.stocks.functions.StockmarketController;
import addons.stocks.items.Stock;
import core.Stringable;

public abstract class Item implements Stringable {

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
