package persistence.market.items;


import java.time.LocalDateTime;

import controller.FinanceController;
import functions.plane.Flight;
import functions.plane.PlaneFunction;
import main.IOController;
import persistence.accounts.Account;
import view.Menu;
import view.inventory.InventoryMenu;

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
			description += "\n" +upgrade.getName();
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
		return new Plane(name, chance, description, chance);
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
		return name +NEXT+ type +NEXT+ value +NEXT+ description +NEXT+ chance;
	}

	@Override
	public void stringToObject(String string) {
		String[] data = string.split(NEXT);
		this.name = data[0];
		this.value = Double.parseDouble(data[2]);
		this.description = data[4];
		this.chance = Integer.parseInt(data[3]);
	}
}
