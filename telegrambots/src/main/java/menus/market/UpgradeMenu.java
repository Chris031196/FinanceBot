package menus.market;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;
import menus.MainMenu;
import util.Account;
import util.Item;
import util.Menu;
import util.Item.TYPE;

public class UpgradeMenu extends Menu{
	
	private ArrayList<Item> upgrades = new ArrayList<Item>();

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();

		ArrayList<String> buttons = new ArrayList<String>();
		String items = "";
		int index = 0;
		for(Item item: c.getMarket()){
			if(item.getType() == TYPE.Upgrade){
				upgrades.add(item);
				buttons.add(item.getName() +":\n" +item.getValue() +"$");
				buttons.add("" + index);
				items += item.getName() + ":\nWert: "+c.round(item.getValue()) +"$\nÜberlandchance: " +item.getChance() +"%\n" +item.getDescription() + "\n\n";
				index++;
			}
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage(items +"Welchen Gegenstand wollen Sie kaufen?", buttons.toArray(new String[]{}), userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);
		try {
			int index = Integer.parseInt(msg);
			Item schedule = upgrades.get(index);
			if(acc.getMoney() >= schedule.getValue()){
				Item item = Item.getNewItem(schedule.getName(), schedule.getType(), schedule.getValue(), schedule.getChance(), schedule.getDescription());
				acc.addMoney(-item.getValue());
				item.setValue(item.getValue()*(3.0/4.0));
				acc.getInventory().addItem(item);
				acc.save();
				IOController.sendMessage("Kauf erfolgreich!", null, userID.toString(), true);
			}
			else {
				IOController.sendMessage("Kauf fehlgeschlagen! (insufficient funds)", null, userID.toString(), true);
			}
		}
		catch(NumberFormatException e){

		}
	}

	public void cancel(Integer userID){
		MarketMenu menu = new MarketMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}

}
