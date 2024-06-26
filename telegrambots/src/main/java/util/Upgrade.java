package util;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;

public class Upgrade extends Item{

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
			for(Item item: c.getAccount(userID).getItems()){
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
			items.addAll(c.getAccount(userID).getItems());
			for(Item item: items){
				if(msg.equals(item.getName()+item.getValue()) && !item.getDescription().contains(this.name)){
					((Plane) item).addItem(this);
					c.getAccount(userID).getItems().remove(this);
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

}
