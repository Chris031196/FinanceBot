package view.market;

import main.IOController;
import old.menus.market.PlanesMenu;
import old.menus.market.UpgradeMenu;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.accounts.Inventory;
import persistence.market.MarketManager;
import persistence.market.items.Item.TYPE;
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
		MarketManager man = MarketManager.getInstance();
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "stocks":
			next = new ItemListMenu(man.getItemsOfType(TYPE.Stock), "Aktienmarkt");
			acc.setMenu(next);
			next.show(userID);
			break;
		case "planes":
			next = new ItemListMenu(man.getItemsOfType(TYPE.Plane), "Flugzeugmarkt");
			acc.setMenu(next);
			next.show(userID);
			break;
		case "upgrades":
			next = new ItemListMenu(man.getItemsOfType(TYPE.Upgrade), "Upgrademarkt");
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
