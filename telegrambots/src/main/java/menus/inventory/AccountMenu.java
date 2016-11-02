package menus.inventory;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Menu;

public class AccountMenu extends Menu {

	@Override
	public void show(Integer userID) {
		Account account = FinanceController.getInstance().getAccounts().get(userID);
		String message = "";
		message += "Name: ";
		message += account.getName() +"\n";
		message += "User ID: ";
		message += account.getID() +"\n";
		message += "Kontostand: ";
		message += FinanceController.getInstance().round(account.getMoney()) +"$\n";
		message += "Popularität: ";
		message += account.getPop() +"+";
		IOController.sendMessage(message, new String[]{"Geld überweisen","transfer","🔙", "back"}, userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("back")){
			InventoryMenu menu = new InventoryMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}
		if(msg.equals("transfer")){
			TransferMenu menu = new TransferMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}

	}

}
