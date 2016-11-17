package view.inventory;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.accounts.TransferMenu;
import view.Menu;

public class AccountMenu extends Menu {

	@Override
	public void show(Integer userID) {
		Account account = AccountManager.getInstance().getAccount(userID);
		String message = "";
		message += "Name: ";
		message += account.getName() +"\n";
		message += "User ID: ";
		message += account.getID() +"\n";
		message += "Kontostand: ";
		message += FinanceController.round(account.getInventory().getMoney()) +"$\n";
		message += "Popularität: ";
		message += account.getInventory().getPop() +"+";
		IOController.sendMessage(message, new String[]{"Geld überweisen","transfer","🔙", "back"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("back")){
			InventoryMenu menu = new InventoryMenu();
			AccountManager.getInstance().getAccount(userID).setMenu(menu);
			menu.show(userID);
		}
		if(msg.equals("transfer")){
			TransferMenu menu = new TransferMenu();
			AccountManager.getInstance().getAccount(userID).setMenu(menu);
			menu.show(userID);
		}

	}

}
