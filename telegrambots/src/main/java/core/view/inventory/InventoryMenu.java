package core.view.inventory;

import addons.TYPE;
import core.accounts.Account;
import core.accounts.AccountManager;
import core.main.IOController;
import core.view.MainMenu;
import core.view.Menu;

public class InventoryMenu extends Menu {

	@Override
	public void show(Integer userID) {
		TYPE[] types = TYPE.values();
		String[] buttons = new String[types.length*2+4];
		buttons[0] = "Kontoinformationen";
		buttons[1] = "account";
		for(int i=0;i<types.length;i++){
			buttons[i*2+2] = types[i].getPlural();
			buttons[i*2+3] = types[i].name();
		}
		buttons[buttons.length-2] = "🔙";
		buttons[buttons.length-1] = "cancel";
		IOController.sendMessage("Kategorien:", buttons, userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		Account acc = AccountManager.getInstance().getAccount(userID);
		Menu next;
		switch(msg){
		case "cancel": cancel(userID); break;
		case "account":
			next = new AccountMenu();
			acc.setListener(next);
			next.show(userID);
			break;
		default:
			TYPE type = TYPE.valueOf(msg);
			next = new ItemListMenu(userID, type, "Ihre " +type.getPlural() +":");
			acc.setListener(next);
			next.show(userID);
			break;
		}
	}

	public void cancel(Integer userID){
		MainMenu menu = new MainMenu();
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}
}