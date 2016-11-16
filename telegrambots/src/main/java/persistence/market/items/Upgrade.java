package persistence.market.items;

import controller.FinanceController;

public class Upgrade extends Item{
	
	private int chance;

	public Upgrade(String name, double value, String description, int chance) {
		this.name = name;
		this.type = TYPE.Upgrade;
		this.value = value;
		this.description = description;
		this.chance = chance;
	}


	
	public int getChance(){
		return chance;
	}
	
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
	}

	@Override
	public String toSaveString() {
		String save = name +NEXT+ value +NEXT+ chance +NEXT+ description;
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
		return new Upgrade(name, value, description, chance);
	}

}
