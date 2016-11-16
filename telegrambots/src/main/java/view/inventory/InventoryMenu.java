package view.inventory;

import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.accounts.Inventory;
import persistence.market.items.Item.TYPE;
import view.MainMenu;
import view.Menu;

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
		IOController.sendMessage("Kategorien:", buttons, userID.toString(), false);
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
		default:
			TYPE type = TYPE.valueOf(msg);
			next = new ItemListMenu(inv.getItemsOfType(type), "Ihre " +type.getPlural() +":");
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
