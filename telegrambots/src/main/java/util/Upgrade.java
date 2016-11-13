package util;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;

public class Upgrade extends Item{
	
	private int chance;

	public Upgrade(String name, double value, int chance, String description) {
		this.name = name;
		this.type = TYPE.Upgrade;
		this.value = value;
		this.chance = chance;
		this.description = description;
	}

	@Override
	public void use(String msg, Integer userID) {
		try {
			Integer.parseInt(msg);
			FinanceController c = FinanceController.getInstance();
			//			ArrayList<Item> planes = new ArrayList<Item>();
			ArrayList<String> buttons = new ArrayList<String>();
			for(Item item: c.getAccount(userID).getInventory().getItems()){
				if(item.getType() == TYPE.Plane){
					//					planes.add(item);
					buttons.add(item.getName());
					buttons.add(item.getName()+item.getValue());
				}
			}
			buttons.add("🔙");
			buttons.add("cancel");
			IOController.sendMessage("Auf welches Flugzeug möchten Sie dieses Upgrade anwenden?", buttons.toArray(new String[]{}), userID.toString(), false);
		}
		catch(NumberFormatException e){
			FinanceController c = FinanceController.getInstance();
			ArrayList<Item> items = new ArrayList<Item>();
			items.addAll(c.getAccount(userID).getInventory().getItems());
			for(Item item: items){
				if(msg.equals(item.getName()+item.getValue()) && !item.getDescription().contains(this.name)){
					((Plane) item).addUpgrade(this);
					c.getAccount(userID).getInventory().getItems().remove(this);
					if(item.getDescription().contains(this.name)){
						IOController.sendMessage("Erfolgreich angewendet!", new String[]{"🔙","cancel"}, userID.toString(), false);
					}
				}
				else if(item.getDescription().contains(this.name)){
					IOController.sendMessage("Das Flugzeug besitzt dieses Upgrade bereits!", new String[]{"🔙","cancel"}, userID.toString(), false);
				}
			}
		}
	}
	
	public int getChance(){
		return chance;
	}
	
	public String print() {
		return getName() + ":\nWert: "+FinanceController.round(getValue()) +"$\nÜberlandchance: " +getChance() +"%\n" +getDescription() + "\n\n";
	}

}
