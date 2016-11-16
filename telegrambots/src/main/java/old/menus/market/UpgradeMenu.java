package old.menus.market;

import java.util.ArrayList;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.Account;
import persistence.market.items.Item;
import persistence.market.items.Upgrade;
import persistence.market.items.Item.TYPE;
import view.Menu;
import view.market.MarketMenu;

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
				Upgrade upgrade = (Upgrade) item;
				upgrades.add(upgrade);
				buttons.add(upgrade.getName() +":\n" +upgrade.getValue() +"$");
				buttons.add("" + index);
				items += upgrade.getName() + ":\nWert: "+c.round(upgrade.getValue()) +"$\nÜberlandchance: " +upgrade.getChance() +"%\n" +upgrade.getDescription() + "\n\n";
				index++;
			}
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage(items +"Welchen Gegenstand wollen Sie kaufen?", buttons.toArray(new String[]{}), userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);
		try {
			int index = Integer.parseInt(msg);
			Upgrade schedule =(Upgrade) upgrades.get(index);
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
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
