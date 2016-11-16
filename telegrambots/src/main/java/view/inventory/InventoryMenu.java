	package view.inventory;

import controller.FinanceController;
import main.IOController;
import old.menus.inventory.CertificateMenu;
import old.menus.inventory.StocksMenu;
import old.menus.inventory.upgrades.UpgradeMenu;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.accounts.Inventory;
import persistence.market.items.Item.TYPE;
import view.MainMenu;
import view.Menu;

public class InventoryMenu extends Menu {

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Kategorien:", new String[]{"Konto", "account", "Aktien","stocks","Flugzeuge","planes","Upgrades","upgrades","Urkunden","certs","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		Account acc = AccountManager.getInstance().getAccount(userID);
		Inventory inv = acc.getInventory();
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "account":
			next = new AccountMenu();
			acc.setMenu(next);
			next.show(userID);
			break;
		case "stocks":
			next = new ItemListMenu(inv.getItemsOfType(TYPE.Stock), "Ihre Aktien:");
			acc.setMenu(next);
			next.show(userID);
			break;
		case "planes":
			next = new ItemListMenu(inv.getItemsOfType(TYPE.Plane), "Ihre Flugzeuge:");
			acc.setMenu(next);
			next.show(userID);
			break;
		case "upgrades":
			next = new ItemListMenu(inv.getItemsOfType(TYPE.Upgrade), "Ihre Upgrades:");
			acc.setMenu(next);
			next.show(userID);
			break;
		case "certs":
			next = new ItemListMenu(inv.getItemsOfType(TYPE.Certificate), "Ihre Urkunden:");
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
