package menus.market;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Item;
import util.Item.TYPE;
import util.Menu;
import util.Plane;

public class PlanesMenu extends Menu{
	
	private ArrayList<Item> planes = new ArrayList<Item>();

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();

		ArrayList<String> buttons = new ArrayList<String>();
		String items = "";
		int index = 0;
		for(Item item: c.getMarket()){
			if(item.getType() == TYPE.Plane){
				Plane plane = (Plane) item;
				planes.add(plane);
				buttons.add(plane.getName() +":\n" +plane.getValue() +"$");
				buttons.add("" + index);
				items += plane.getName() + ":\nWert: "+c.round(plane.getValue()) +"$\nÜberlandchance: " +plane.getChance() +"%\n" +plane.getDescription() + "\n\n";
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
			Plane schedule = (Plane) planes.get(index);
			if(acc.getMoney() >= schedule.getValue()){
				Item item = Item.getNewItem(schedule.getName(), schedule.getType(), schedule.getValue(), schedule.getChance(), schedule.getDescription());
				acc.addMoney(-item.getValue());
				item.setDescription("");
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
