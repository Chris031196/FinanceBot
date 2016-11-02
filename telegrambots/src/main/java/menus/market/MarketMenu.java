package menus.market;

import main.FinanceController;
import main.IOController;
import menus.MainMenu;
import util.Account;
import util.Menu;

public class MarketMenu extends Menu {
	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Kategorien:", new String[]{"Aktienkatalog","stocks","Flugzeugkatalog","planes","Upgradekatalog","upgrades","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		Account acc = FinanceController.getInstance().getAccount(userID);
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
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
		}
	}
	
	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}
}
