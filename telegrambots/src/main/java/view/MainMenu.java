package view;

import main.IOController;
import persistence.accounts.AccountManager;
import view.inventory.InventoryMenu;
import view.market.MarketMenu;

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
			AccountManager.getInstance().logout(userID);
			IOController.sendMessage("Erfolgreich ausgeloggt und gespeichert!", null, userID.toString(), false);
			break;
		}
		
	}


}
