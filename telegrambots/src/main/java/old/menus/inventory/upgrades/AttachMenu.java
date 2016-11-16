package old.menus.inventory.upgrades;

import java.util.ArrayList;

import controller.FinanceController;
import main.IOController;
import old.menus.inventory.InventoryMenu;
import persistence.accounts.Account;
import persistence.market.items.Item;
import persistence.market.items.Upgrade;
import persistence.market.items.Item.TYPE;
import view.Menu;

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
