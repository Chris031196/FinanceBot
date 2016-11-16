package view.market;

import java.util.ArrayList;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
import persistence.market.items.Item;
import view.Menu;

public class ItemListMenu extends Menu {

	ArrayList<Item> items;
	String message;

	public ItemListMenu(ArrayList<Item> items, String message){
		this.items = items;
		this.message = message;
	}

	@Override
	public void show(Integer userID) {
		ArrayList<String> buttons = new ArrayList<String>();
		for(Item item: items){
			buttons.add(item.getName() + ": " +item.getValue() +"$");
			buttons.add("" +items.indexOf(item));
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage(message, buttons.toArray(new String[]{}), userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		try {
			Item item = items.get(Integer.parseInt(msg));
			if(item != null){
				ItemDetailsMenu menu = new ItemDetailsMenu(item);
				AccountManager.getInstance().getAccount(userID).setMenu(menu);
				menu.show(userID);
			}
		}
		catch(NumberFormatException e){}
	}

	public void cancel(Integer userID){
		MarketMenu menu = new MarketMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}