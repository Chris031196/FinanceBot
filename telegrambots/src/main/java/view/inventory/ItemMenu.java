package view.inventory;

import java.util.ArrayList;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.items.Item;
import view.Menu;
import view.market.MarketMenu;

public class ItemMenu extends Menu{

	
	ArrayList<Item> items;
	String message;
	
	public ItemMenu(ArrayList<Item> items, String message){
		this.items = items;
		this.message = message;
	}
	
	@Override
	public void show(Integer userID) {
		ArrayList<String> buttons = new ArrayList<String>();
		for(Item item: items){
			buttons.add(item.print());
			buttons.add("" +items.indexOf(item));
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage(message, buttons.toArray(new String[]{}), userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if("cancel".equals(msg)){
			cancel(userID);
		}
		
	}
	
	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
