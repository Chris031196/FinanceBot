package menus.inventory.planes;

import java.util.ArrayList;

import main.FinanceController;
import main.IOController;
import menus.inventory.InventoryMenu;
import util.Account;
import util.Item;
import util.Item.TYPE;
import util.Menu;
import util.Plane;

public class FlyMenu extends Menu {


	private Item selected;
	private ArrayList<Item> planes = new ArrayList<Item>();

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);

		String[] buttons = new String[acc.getInventory().getItems().size()*2+2];
		int index = 0;
		for(Item item: acc.getInventory().getItems()){
			if(item.getType() == TYPE.Plane) {
				buttons[index] = item.getName();
				index++;
				buttons[index] = "" + (index > 1 ? index/2 : 0);
				index++;
				planes.add(item);
			}
		}
		buttons[index] = "🔙";
		buttons[index+1] = "cancel";

		IOController.sendMessage("Welches Flugzeug wollen Sie nutzen?", buttons, userID.toString(), true);
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
			selected = planes.get(index);
			selected.use(msg, userID);
			acc.save();
		}
		catch(NumberFormatException e){
			if(selected != null)
				((Plane)selected).use(msg, userID);
		}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}

}
