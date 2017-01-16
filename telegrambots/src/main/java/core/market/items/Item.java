package core.market.items;

import addons.Function;
import addons.TYPE;
import addons.planes.items.Certificate;
import addons.planes.items.Plane;
import addons.planes.items.Upgrade;
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
	public static Item getItem(String name, String type, double value, String description, String additionalData){
		Item item = null;
		switch(type){
		case "Plane":
			item = new Plane(name, value, description, additionalData);
			break;

		case "Upgrade":
			item = new Upgrade(name, value, description, additionalData);
			break;

		case "Stock":
			item = new Stock(name, value, description, additionalData);
			break;
			
		case "Certificate":
			item = new Certificate(name, value, description, additionalData);
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
	
	public abstract String getAdditionalData();
	
	public abstract String print();
//	{
//		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\n" +getDescription() + "\n\n";
//	}

//	public String toString(){
//		String out = name +";" +type + ";" + value +";" +chance +";" +description;
//		return out;
//	}
}
