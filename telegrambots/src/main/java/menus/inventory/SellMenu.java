package menus.inventory;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Item;
import util.Menu;

public class SellMenu extends Menu {
	
	public SellMenu(ArrayList<Item> items, String message){
		
	}

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);

		String[] buttons = new String[acc.getInventory().getItems().size()*2+2];
		int index = 0;
		for(Item item: acc.getInventory().getItems()){
			buttons[index] = item.getName() + ": " +c.round(item.getValue()) +"$";
			index++;
			buttons[index] = "" + (index > 1 ? index/2 : 0);
			index++;
		}
		buttons[index] = "🔙";
		buttons[index+1] = "cancel";

		IOController.sendMessage("Welchen Gegenstand wollen Sie verkaufen?", buttons, userID.toString(), true);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);
		try {
			int index = Integer.parseInt(msg);
			acc.addMoney(acc.getInventory().getItems().get(index).getValue());
			acc.getInventory().getItems().remove(index);
			acc.save();
			IOController.sendMessage("Verkauf erfolgreich!", new String[]{"🔙","cancel"}, userID.toString(), true);
		}
		catch(NumberFormatException e){

		}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}

}
