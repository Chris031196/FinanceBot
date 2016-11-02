package util;

public class Item {

	public enum TYPE {
		Plane, Upgrade
	}

	protected String name;
	protected TYPE type;
	protected double value;
	protected int chance;
	protected String description;

	public Item(){}

	private Item(String name, TYPE type, double value, int chance, String description){
		this.name = name;
		this.type = type;
		this.value = value;
		this.chance = chance;
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
			return new Item(name, type, value, chance, description);
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
			return new Item(name, type, value, chance, description);
		}
	}

	public void use(String msg, Integer userID){};

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}


	public int getChance() {
		return chance;
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

	public String toString(){
		String out = name +";" +type + ";" + value +";" +chance +";" +description;
		return out;
	}
}
