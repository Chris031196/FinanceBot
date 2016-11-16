package old.menus.generic;

import java.util.ArrayList;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.Account;
import persistence.market.items.Item;
import view.Menu;
import view.inventory.InventoryMenu;

public class SellMenu extends Menu {
	
	ArrayList<Item> items;
	String message;
	
	public SellMenu(ArrayList<Item> items, String message){
		this.items = items;
		this.message = message;
	}

	@Override
	public void show(Integer userID) {

		String[] buttons = new String[items.size()*2+2];
		int index = 0;
		for(Item item: items){
			buttons[index] = item.getName() + ": " +FinanceController.round(item.getValue()) +"$";
			index++;
			buttons[index] = "" + (index > 1 ? index/2 : 0);
			index++;
		}
		buttons[index] = "🔙";
		buttons[index+1] = "cancel";

		IOController.sendMessage(message, buttons, userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);
		try {
			int index = Integer.parseInt(msg);
			Item toSell = items.get(index);
			acc.addMoney(toSell.getValue());
			acc.getInventory().getItems().remove(toSell);
			IOController.sendMessage(toSell.getName() +" erfolgreich verkauft!", new String[]{"🔙","cancel"}, userID.toString(), true);
		}
		catch(NumberFormatException e){}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
