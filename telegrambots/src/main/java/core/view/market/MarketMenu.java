package core.view.market;

import java.util.ArrayList;

import addons.TYPE;
import core.accounts.Account;
import core.accounts.AccountManager;
import core.main.IOController;
import core.view.MainMenu;
import core.view.Menu;

public class MarketMenu extends Menu {

	@Override
	public void show(Integer userID) {
		TYPE[] types = TYPE.values();
		ArrayList<String> buttons = new ArrayList<String>();
		for(int i=0;i<types.length;i++){
			if(types[i].hasMarket()){
				buttons.add(types[i].getSingular() +"katalog");
				buttons.add(types[i].name());
			}
		}
		buttons.add("🔙");
		buttons.add("cancel");
		IOController.sendMessage("Kategorien:", buttons.toArray(new String[]{}), userID.toString(), true);
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
		acc.setListener(next);
		next.show(userID);
	}

	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}
}
