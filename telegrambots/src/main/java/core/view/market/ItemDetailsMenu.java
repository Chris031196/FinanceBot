package core.view.market;

import core.accounts.AccountManager;
import core.main.IOController;
import core.market.items.Item;
import core.view.Menu;

public class ItemDetailsMenu extends Menu {
	
	Item item;
	
	public ItemDetailsMenu(Item item){
		this.item = item;
	}
	
	@Override
	public void messageReceived(String msg, Integer userID) {
		switch(msg){
		case "buy":
			BuyMenu menu = new BuyMenu(item);
			AccountManager.getInstance().getAccount(userID).setListener(menu);
			menu.show(userID);
			break;
		case "cancel":
			cancel(userID);
			break;
		}

	}

	@Override
	public void show(Integer userID) {
		String[] buttons = new String[4];
		buttons[0] = "Jetzt kaufen!";
		buttons[1] = "buy";
		buttons[2] = "🔙";
		buttons[3] = "cancel";
		IOController.sendMessage(item.print(), buttons, userID.toString(), true);
	}
	
	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(item.getType(), item.getType().getSingular()+ "markt");
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}

}
