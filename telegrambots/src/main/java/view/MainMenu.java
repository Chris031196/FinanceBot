package view;

import main.IOController;
import old.menus.inventory.InventoryMenu;
import persistence.accounts.AccountManager;
import view.market.MarketMenu;

public class MainMenu extends Menu {

	@Override
	public void show(Integer userID) {
		IOController.deleteLastMessages(userID.toString());
		IOController.sendMessage("Wilkommen! Was möchten Sie tun?", new String[]{"Inventar","inventory","Markt","market","Ausloggen (Speichern)","logout"}, userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		
		switch(msg){
		case "market":
			MarketMenu mMenu = new MarketMenu();
			AccountManager.getInstance().getAccount(userID).setMenu(mMenu);
			mMenu.show(userID);
			break;
			
		case "inventory":
			InventoryMenu iMenu = new InventoryMenu();
			AccountManager.getInstance().getAccount(userID).setMenu(iMenu);
			iMenu.show(userID);
			break;
			
		case "logout":
			AccountManager.getInstance().logout(userID);
			break;
		}
		
	}


}
