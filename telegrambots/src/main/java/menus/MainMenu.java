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
		IOController.sendMessage("Wilkommen! Was möchten Sie tun?", new String[]{"Inventar","inventory","Markt","market","Ausloggen","logout"}, userID.toString(), true);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		
		switch(msg){
		case "market":
			MarketMenu mMenu = new MarketMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(mMenu);
			mMenu.show(userID);
			break;
			
		case "inventory":
			InventoryMenu iMenu = new InventoryMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(iMenu);
			iMenu.show(userID);
			break;
			
		case "logout":
			NoMenu nMenu = new NoMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(nMenu);
			nMenu.show(userID);
			break;
		}
		
	}


}
