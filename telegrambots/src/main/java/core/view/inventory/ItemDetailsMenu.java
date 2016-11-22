package core.view.inventory;

import core.accounts.AccountManager;
import core.main.IOController;
import core.market.MarketManager;
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
		case "use":
			item.getFunction().use(userID);
			break;
		case "sell":
			if(MarketManager.getInstance().sellItem(item, userID)){
				IOController.sendMessage(item.getName() +" erfolgreich verkauft!", new String[]{"🔙","cancel"}, userID.toString(), true);
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
			IOController.sendMessage(item.print(), buttons, userID.toString(), true);
		}
		else {
			String[] buttons = new String[4];
			buttons[0] = "Verkaufen!";
			buttons[1] = "sell";
			buttons[2] = "🔙";
			buttons[3] = "cancel";
			IOController.sendMessage(item.print(), buttons, userID.toString(), true);
		}
	}

	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(userID, item.getType(), item.getType().getPlural());
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}

}
