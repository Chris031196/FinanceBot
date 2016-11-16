package view.market;

import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
import persistence.market.items.Item;
import view.Menu;

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
		optionIndex++;

		if(buyItem.getOptionMessages().length < optionIndex) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], BACK, userID.toString(), false);
			if(optionIndex-1 >= 0){
				options[optionIndex-1] = msg;
			}
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
		ItemDetailsMenu menu = new ItemDetailsMenu(buyItem);
		AccountManager.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
