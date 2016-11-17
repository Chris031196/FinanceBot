package view.inventory;

import java.util.ArrayList;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;
import view.Menu;

public class ItemListMenu extends Menu{

	ArrayList<Item> items;
	String message;
	
	public ItemListMenu(Integer userID, TYPE type, String message){
		this.items = AccountManager.getInstance().getAccount(userID).getInventory().getItemsOfType(type);
		this.message = message;
	}
	
	@Override
	public void show(Integer userID) {
		ArrayList<String> buttons = new ArrayList<String>();
		for(Item item: items){
			buttons.add(item.getName() +": " +item.getValue() +"$");
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
