package menus.inventory;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Item;
import util.Menu;

public class SellMenu extends Menu {

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);

		String[] buttons = new String[acc.getItems().size()*2+2];
		int index = 0;
		for(Item item: acc.getItems()){
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
			acc.addMoney(acc.getItems().get(index).getValue());
			acc.getItems().remove(index);
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
