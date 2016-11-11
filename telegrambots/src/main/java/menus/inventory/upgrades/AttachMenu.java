package menus.inventory.upgrades;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;
import menus.inventory.InventoryMenu;
import util.Account;
import util.Item;
import util.Menu;
import util.Upgrade;
import util.Item.TYPE;

public class AttachMenu extends Menu {

	private Item selected;
	private ArrayList<Item> upgrades = new ArrayList<Item>();

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);

		ArrayList<String> buttons = new ArrayList<String>();
		int index = 0;
		for(Item item: acc.getInventory().getItems()){
			if(item.getType().equals(TYPE.Upgrade)){
				buttons.add(item.getName());
				index++;
				buttons.add("" + (index > 1 ? index/2 : 0));
				index++;
				upgrades.add(item);
			}
		}
		buttons.add("🔙");
		buttons.add("cancel");

		IOController.sendMessage("Welchen Gegenstand wollen Sie anbringen?", buttons.toArray(new String[]{}), userID.toString(), true);
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
			selected = upgrades.get(index);
			selected.use(msg, userID);
			acc.save();
		}
		catch(NumberFormatException e){
			if(selected != null){
				((Upgrade)selected).use(msg, userID);
			}
		}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
