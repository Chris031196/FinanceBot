package util;

import java.io.Serializable;

import main.FinanceController;

public class Item implements Serializable{

	private static final long serialVersionUID = 9147104058516715643L;

	public enum TYPE {
		Plane, Upgrade, Stock, Certificate
	}

	protected String name;
	protected TYPE type;
	protected double value;
	protected String description;

	public Item(){}

	private Item(String name, TYPE type, double value, String description){
		this.name = name;
		this.type = type;
		this.value = value;
		this.description = description;
	}

	public static Item getNewItem(String name, TYPE type, double value, int chance, String description){
		if(type == TYPE.Plane){
			return new Plane(name, value, chance, description);
		}
		else if(type == TYPE.Upgrade){
			return new Upgrade(name, value, chance, description);
		}
		else {
			return new Item(name, type, value, description);
		}
	}

	public static Item getNewItem(String itemString){
		String[] stringArray = itemString.split(";");
		String name = stringArray[0];
		TYPE type = TYPE.valueOf(stringArray[1]);
		double value = Double.parseDouble(stringArray[2]);
		int chance = Integer.parseInt(stringArray[3]);
		String description = "";
		if(stringArray.length >= 5){
			description = stringArray[4];
		}
		if(type == TYPE.Plane){
			return new Plane(name, value, chance, description);
		}
		else if(type == TYPE.Upgrade){
			return new Upgrade(name, value, chance, description);
		}
		else {
			return new Item(name, type, value, description);
		}
	}

	public void use(String msg, Integer userID){};

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


	public void setValue(double value) {
		this.value = value;
	}
	
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\n" +getDescription() + "\n\n";
	}

//	public String toString(){
//		String out = name +";" +type + ";" + value +";" +chance +";" +description;
//		return out;
//	}
}
