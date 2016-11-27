package addons.planes.items;

import addons.TYPE;
import addons.planes.functions.PlaneFunction;
import core.FinanceController;
import core.market.items.Item;

public class Plane extends Item {

	private int chance;
	private boolean flying;

	public Plane(String name, double value, String description, int chance) {
		this.name = name;
		this.type = TYPE.Plane;
		this.value = value;
		this.description = description;
		this.chance = chance;
		this.flying = false;
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
		if(!flying){
			return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
		}
		else {
			return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\nAuf einem Überlandflug!";
		}
	}

	@Override
	public Item copy() {
		return new Plane(name, value*(9.0/10.0), description, chance);
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

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	@Override
	public String getShort() {
		if(!flying) {
		return name +": " +chance +"%";
		} else {
			return name +" (fliegt)";
		}
	}
}
