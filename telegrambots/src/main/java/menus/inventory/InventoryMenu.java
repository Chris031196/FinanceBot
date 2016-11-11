	package menus.inventory;

import main.FinanceController;
import main.IOController;
import menus.MainMenu;
import menus.generic.UseMenu;
import menus.inventory.upgrades.UpgradeMenu;
import util.Account;
import util.Menu;

public class InventoryMenu extends Menu {

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Kategorien:", new String[]{"Konto", "account", "Aktien","stocks","Flugzeuge","planes","Upgrades","upgrades","Urkunden","certs","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		Account acc = FinanceController.getInstance().getAccount(userID);
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "account":
			next = new AccountMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "stocks":
			next = new StocksMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "planes":
			next = new UseMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "upgrades":
			next = new UpgradeMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "certs":
			next = new CertificateMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		}
	}
	
	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
