package menus;

import main.FinanceController;
import main.IOController;
import menus.inventory.InventoryMenu;
import menus.market.MarketMenu;
import util.Menu;

public class MainMenu extends Menu{

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
			FinanceController.getInstance().getAccount(userID).setMenu(mMenu);
			mMenu.show(userID);
			break;
			
		case "inventory":
			InventoryMenu iMenu = new InventoryMenu();
			FinanceController.getInstance().getAccount(userID).setMenu(iMenu);
			iMenu.show(userID);
			break;
			
		case "logout":
			NoMenu nMenu = new NoMenu();
			FinanceController.getInstance().getAccount(userID).setMenu(nMenu);
			nMenu.show(userID);
			break;
		}
		
	}


}
