package view.inventory;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
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
		case "use":
			item.getFunction().use(userID);
			break;
		case "sell":
			if(MarketManager.getInstance().sellItem(item, userID)){
				IOController.sendMessage(item.getName() +" erfolgreich verkauft!", new String[]{"🔙","cancel"}, userID.toString(), false);
			}
		case "cancel":
			cancel(userID);
			break;
		}
	}

	@Override
	public void show(Integer userID) {
		if(item.getFunction() != null) {
			String[] buttons = new String[6];
			buttons[0] = "Jetzt benutzen!";
			buttons[1] = "use";
			buttons[2] = "Verkaufen!";
			buttons[3] = "sell";
			buttons[4] = "🔙";
			buttons[5] = "cancel";
			IOController.sendMessage(item.print(), buttons, userID.toString(), false);
		}
		else {
			String[] buttons = new String[4];
			buttons[0] = "Verkaufen!";
			buttons[1] = "sell";
			buttons[2] = "🔙";
			buttons[3] = "cancel";
			IOController.sendMessage(item.print(), buttons, userID.toString(), false);
		}
	}

	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(userID, item.getType(), item.getType().getPlural());
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
