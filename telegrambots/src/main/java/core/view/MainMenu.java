package core.view;

import core.accounts.AccountManager;
import core.main.IOController;
import core.view.inventory.InventoryMenu;
import core.view.market.MarketMenu;

public class MainMenu extends Menu {

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Wilkommen! Was möchten Sie tun?", new String[]{"Inventar","inventory","Markt","market","Ausloggen (Speichern)","logout"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		
		switch(msg){
		case "market":
			MarketMenu mMenu = new MarketMenu();
			AccountManager.getInstance().getAccount(userID).setListener(mMenu);
			mMenu.show(userID);
			break;
			
		case "inventory":
			InventoryMenu iMenu = new InventoryMenu();
			AccountManager.getInstance().getAccount(userID).setListener(iMenu);
			iMenu.show(userID);
			break;
			
		case "logout":
			IOController.sendMessage("Erfolgreich ausgeloggt und gespeichert!", null, userID.toString(), false);
			AccountManager.getInstance().logout(userID);
			break;
		}
		
	}


}
