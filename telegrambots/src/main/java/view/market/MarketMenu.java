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
		TYPE[] types = TYPE.values();
		String[] buttons = new String[types.length*2+2];
		for(int i=0;i<types.length;i++){
			if(types[i].hasMarket()){
				buttons[i*2] = types[i].getSingular() +"katalog";
				buttons[i*2+1] = types[i].name();
			}
		}
		buttons[buttons.length-2] = "🔙";
		buttons[buttons.length-1] = "cancel";
		IOController.sendMessage("Kategorien:", buttons, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if("cancel".equals(msg)){
			cancel(userID);
			return;
		}

		Account acc = AccountManager.getInstance().getAccount(userID);
		TYPE type = TYPE.valueOf(msg);
		Menu next = new ItemListMenu(type, type.getSingular()+ "markt");
		acc.setMenu(next);
		next.show(userID);
	}

	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
