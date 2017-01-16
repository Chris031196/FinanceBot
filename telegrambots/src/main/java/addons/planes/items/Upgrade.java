package addons.planes.items;

import addons.TYPE;
import addons.planes.functions.UpgradeFunction;
import core.FinanceController;
import core.market.items.Item;

public class Upgrade extends Item{

	private int chance;

	public Upgrade(String name, double value, String description, String additionalData) {
		this.name = name;
		this.type = TYPE.Upgrade;
		this.value = value;
		this.description = description;
		this.chance = Integer.parseInt(additionalData);
		this.function = new UpgradeFunction(this);
	}

	public int getChance(){
		return chance;
	}
	
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
	}

	@Override
	public void setOptions(String[] options) {}

	@Override
	public String[] getOptionMessages() {
		return new String[]{};
	}

	@Override
	public Item copy() {
		return new Upgrade(name, value*(9.0/10.0), description, getAdditionalData());
	}
	
	@Override
	public String getShort() {
		return name +": " +chance +"%";
	}

	@Override
	public String getAdditionalData() {
		return ""+chance;
	}
}
