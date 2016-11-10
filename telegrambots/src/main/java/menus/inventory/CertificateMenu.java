package menus.inventory;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Menu;

public class CertificateMenu extends Menu {

	@Override
	public void show(Integer userID) {
		Account acc = FinanceController.getInstance().getAccount(userID);
		IOController.sendMessage("Sie besitzen folgende Urkunden: ", null, userID.toString(), true);
		for(String cert: acc.getInventory().getCerts()){
			IOController.sendMessage(cert, null, userID.toString(), true);
		}
		IOController.sendMessage("Herzlichen Glückwunsch!", new String[]{"🔙","cancel"}, userID.toString(), true);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			InventoryMenu menu = new InventoryMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}
	}
}
