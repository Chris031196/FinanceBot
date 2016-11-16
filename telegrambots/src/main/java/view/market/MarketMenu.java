package view.market;

import main.IOController;
import old.menus.market.PlanesMenu;
import old.menus.market.StocksMenu;
import old.menus.market.UpgradeMenu;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import view.MainMenu;
import view.Menu;

public class MarketMenu extends Menu {
	
	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Kategorien:", new String[]{"Aktienkatalog","stocks","Flugzeugkatalog","planes","Upgradekatalog","upgrades","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		Account acc = AccountManager.getInstance().getAccount(userID);
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "stocks":
			next = new StocksMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "planes":
			next = new PlanesMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "upgrades":
			next = new UpgradeMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		}
	}
	
	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
