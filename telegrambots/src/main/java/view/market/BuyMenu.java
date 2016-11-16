package view.market;

import java.util.ArrayList;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
import persistence.market.items.Item;
import view.Menu;

public class BuyMenu extends Menu {

	ArrayList<Item> items;
	String message;
	Item buyItem;
	String[] options;
	int optionIndex = -1;
	
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
		
		if(optionIndex == -1){
			buyItem = items.get(Integer.parseInt(msg));
			options = new String[buyItem.getOptionMessages().length];
		}
		optionIndex++;
		
		if(buyItem != null && buyItem.getOptionMessages().length < optionIndex) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], null, userID.toString(), false);
			if(optionIndex-1 >= 0){
				options[optionIndex-1] = msg;
			}
		}
		else if(buyItem != null){
			MarketManager manager = MarketManager.getInstance();
			manager.buyItem(buyItem, options, userID);
		}
	}
	
	public void cancel(Integer userID){
		MarketMenu menu = new MarketMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
