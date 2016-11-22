package view.market;

import java.util.ArrayList;

import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.market.items.Item.TYPE;
import view.MainMenu;
import view.Menu;

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
