package view.market;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.items.Item;
import view.Menu;

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
		IOController.sendMessage(item.print(), buttons, userID.toString(), false);
	}
	
	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(item.getType(), item.getType().getSingular()+ "markt");
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}

}
