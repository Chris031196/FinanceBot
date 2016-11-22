package core.view.market;

import core.accounts.AccountManager;
import core.main.IOController;
import core.market.MarketManager;
import core.market.items.Item;
import core.view.Menu;

public class BuyMenu extends Menu {

	String[] options;
	Item buyItem;
	int optionIndex = 0;

	public BuyMenu(Item buyItem){
		this.buyItem = buyItem;
		this.options = new String[buyItem.getOptionMessages().length];
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if("cancel".equals(msg)){
			cancel(userID);
			return;
		}
		options[optionIndex] = msg;
		
		optionIndex++;

		if(buyItem.getOptionMessages().length < optionIndex) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], BACK, userID.toString(), false);
		}
		else {
			MarketManager manager = MarketManager.getInstance();
			if(manager.buyItem(buyItem, options, userID)){
				IOController.sendMessage("Kauf erfolgreich!", BACK, userID.toString(), false);
			}
			else {
				IOController.sendMessage("Kauf fehlgeschlagen!", BACK, userID.toString(), false);
			}
		}
	}

	@Override
	public void show(Integer userID) {
		if(buyItem.getOptionMessages().length > 0) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], BACK, userID.toString(), false);
		}
		else {
			MarketManager manager = MarketManager.getInstance();
			if(manager.buyItem(buyItem, options, userID)){
				IOController.sendMessage("Kauf erfolgreich!", BACK, userID.toString(), false);
			}
			else {
				IOController.sendMessage("Kauf fehlgeschlagen!", BACK, userID.toString(), false);
			}
		}
	}
	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(buyItem.getType(), buyItem.getType().getSingular()+ "markt");
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}
}
