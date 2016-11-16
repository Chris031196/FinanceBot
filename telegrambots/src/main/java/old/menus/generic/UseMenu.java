package old.menus.generic;

import java.util.ArrayList;

import controller.FinanceController;
import main.IOController;
import old.menus.generic.SelectionMenu;
import old.menus.inventory.planes.FlyMenu;
import persistence.accounts.Account;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;
import view.Menu;
import view.inventory.InventoryMenu;

public class UseMenu extends Menu {
	
	ArrayList<Item> items;
	String message;
	
	public UseMenu(ArrayList<Item> items, String message){
		this.items = items;
		this.message = message;
	}

	@Override
	public void show(Integer userID) {
		String inventory = "Flugzeuge:\n\n";
		for(Item item: items){
				inventory += item.getName() + ":\nWert: "+FinanceController.round(item.getValue()) +"$\nÜberlandchance: " +item.getChance() +"%\n" +item.getDescription() + "\n\n";
		}

		IOController.sendMessage(inventory, new String[]{"Flugzeug verkaufen","sell","Überland gehen","use","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		if(msg.equals("sell")){
			Account acc = FinanceController.getInstance().getAccount(userID);
			SellMenu menu = new SellMenu(acc.getInventory().getItemsOfType(TYPE.Plane), "Welches Flugzeug möchten Sie verkaufen?");
			acc.setMenu(menu);
			menu.show(userID);
		}
		else if(msg.equals("use")){
			FlyMenu menu = new FlyMenu();
			FinanceController.getInstance().getAccount(userID).setMenu(menu);
			menu.show(userID);
		}

	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
