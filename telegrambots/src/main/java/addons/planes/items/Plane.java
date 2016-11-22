package addons.planes.items;


import addons.TYPE;
import addons.planes.functions.PlaneFunction;
import core.FinanceController;
import core.market.items.Item;

public class Plane extends Item {
	
	private int chance;

	public Plane(String name, double value, String description, int chance) {
		this.name = name;
		this.type = TYPE.Plane;
		this.value = value;
		this.description = description;
		this.chance = chance;
		this.function = new PlaneFunction(this);
	}

	public void addUpgrade(Upgrade upgrade){
		if(description == ""){
			description += upgrade.getName();
		}
		else {
			description += "\n-" +upgrade.getName();
		}
		chance += upgrade.getChance();
		value += upgrade.getValue();
	}

	public int getChance() {
		return chance;
	}
	
	@Override
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
	}

	@Override
	public Item copy() {
		return new Plane(name, value, description, chance);
	}

	@Override
	public void setOptions(String[] options) {}

	@Override
	public String[] getOptionMessages() {
		String[] options = new String[0];
		return options;
	}

	@Override
	public String toSaveString() {
		return name +NEXT+ type +NEXT+ value +NEXT+ chance +NEXT+ description;
	}

	@Override
	public void stringToObject(String string) {
		String[] data = string.split(NEXT);
		this.name = data[0];
		this.value = Double.parseDouble(data[2]);
		this.description = data[4];
		this.chance = Integer.parseInt(data[3]);
	}

	@Override
	public String getShort() {
		return name +": " +chance +"%";
	}
}
