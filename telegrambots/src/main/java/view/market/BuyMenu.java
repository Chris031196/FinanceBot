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
		options[optionIndex] = msg;
		
		optionIndex++;

		if(buyItem.getOptionMessages().length < optionIndex) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], BACK, userID.toString(), true);
		}
		else {
			MarketManager manager = MarketManager.getInstance();
			if(manager.buyItem(buyItem, options, userID)){
				IOController.sendMessage("Kauf erfolgreich!", BACK, userID.toString(), true);
			}
			else {
				IOController.sendMessage("Kauf fehlgeschlagen!", BACK, userID.toString(), true);
			}
		}
	}

	@Override
	public void show(Integer userID) {
		if(buyItem.getOptionMessages().length > 0) {
			IOController.sendMessage(buyItem.getOptionMessages()[optionIndex], BACK, userID.toString(), true);
		}
		else {
			MarketManager manager = MarketManager.getInstance();
			if(manager.buyItem(buyItem, options, userID)){
				IOController.sendMessage("Kauf erfolgreich!", BACK, userID.toString(), true);
			}
			else {
				IOController.sendMessage("Kauf fehlgeschlagen!", BACK, userID.toString(), true);
			}
		}
	}
	public void cancel(Integer userID){
		ItemListMenu menu = new ItemListMenu(buyItem.getType(), buyItem.getType().getSingular()+ "markt");
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}
}
