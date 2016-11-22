package core.view.inventory;

import java.util.ArrayList;

import addons.TYPE;
import core.accounts.AccountManager;
import core.main.IOController;
import core.market.items.Item;
import core.view.Menu;

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
			buttons.add(item.getShort());
			buttons.add("" +items.indexOf(item));
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage(message, buttons.toArray(new String[]{}), userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if("cancel".equals(msg)){
			cancel(userID);
		}
		
		try {
			Item item = items.get(Integer.parseInt(msg));
			if(item != null){
				ItemDetailsMenu menu = new ItemDetailsMenu(item);
				AccountManager.getInstance().getAccount(userID).setListener(menu);
				menu.show(userID);
			}
		}
		catch(NumberFormatException e){}
	}
	
	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}
}