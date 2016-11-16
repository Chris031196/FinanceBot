package view.market;

import java.util.ArrayList;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
import persistence.market.items.Item;
import persistence.market.items.Plane;
import persistence.market.items.Upgrade;
import persistence.market.items.Item.TYPE;
import view.MainMenu;
import view.Menu;

public class BuyMenu extends Menu {

	ArrayList<Item> items;
	String message;
	

	public BuyMenu(ArrayList<Item> items, String message){
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
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}
		
		MarketManager manager = MarketManager.getInstance();
		manager.buyItem(items.get(Integer.parseInt(msg)), userID);
	}
	
	public void cancel(Integer userID){
		MarketMenu menu = new MarketMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
