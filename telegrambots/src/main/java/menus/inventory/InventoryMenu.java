package menus.inventory;

import main.FinanceController;
import main.IOController;
import menus.MainMenu;
import util.Account;
import util.Item;
import util.Menu;
import util.Item.TYPE;

public class InventoryMenu extends Menu {

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Kategorien:", new String[]{"Konto", "account", "Aktien","stocks","Flugzeuge","planes","Upgrades","upgrades","Urkunden","certs","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		Account acc = FinanceController.getInstance().getAccount(userID);
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "account":
			next = new AccountMenu();
			acc.setCurMenu(next);
			next.show(userID);
			break;
		case "stocks":
			next = new StocksMenu();
			acc.setCurMenu(next);
			next.show(userID);
			break;
		case "planes":
			next = new PlanesMenu();
			acc.setCurMenu(next);
			next.show(userID);
			break;
		case "upgrades":
			next = new UpgradeMenu();
			acc.setCurMenu(next);
			next.show(userID);
			break;
		case "certs":
			next = new CertificateMenu();
			acc.setCurMenu(next);
			next.show(userID);
			break;
		}
	}
	
	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}
}
