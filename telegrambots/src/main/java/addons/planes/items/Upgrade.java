package addons.planes.items;

import addons.TYPE;
import addons.planes.functions.UpgradeFunction;
import core.FinanceController;
import core.market.items.Item;

public class Upgrade extends Item{

	private int chance;

	public Upgrade(String name, double value, String description, int chance) {
		this.name = name;
		this.type = TYPE.Upgrade;
		this.value = value;
		this.description = description;
		this.chance = chance;
		this.function = new UpgradeFunction(this);
	}

	public int getChance(){
		return chance;
	}
	
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
	}

	@Override
	public String toSaveString() {
		String save = name +NEXT+ type +NEXT+ value +NEXT+ chance +NEXT+ description;
		return save;
	}

	@Override
	public void stringToObject(String string) {
		String[] data = string.split(NEXT);
		this.name = data[0];
		this.value = Double.parseDouble(data[2]);
		this.chance = Integer.parseInt(data[3]);
		this.description = data[4];
	}

	@Override
	public void setOptions(String[] options) {}

	@Override
	public String[] getOptionMessages() {
		return new String[]{};
	}

	@Override
	public Item copy() {
		return new Upgrade(name, value*(9.0/10.0), description, chance);
	}
	
	@Override
	public String getShort() {
		return name +": " +chance +"%";
	}
}
