package menus.inventory;

import main.FinanceController;
import main.IOController;
import util.Account;
import util.Item;
import util.Menu;
import util.Plane;
import util.Upgrade;
import util.Item.TYPE;

public class UseMenu extends Menu {

	Item selected;

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		Account acc = c.getAccount(userID);

		String[] buttons = new String[acc.getItems().size()*2+2];
		int index = 0;
		for(Item item: acc.getItems()){
			buttons[index] = item.getName();
			index++;
			buttons[index] = "" + (index > 1 ? index/2 : 0);
			index++;
		}
		buttons[index] = "🔙";
		buttons[index+1] = "cancel";

		IOController.sendMessage("Welchen Gegenstand wollen Sie nutzen?", buttons, userID.toString(), true);
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
			selected = acc.getItems().get(index);
			selected.use(msg, userID);
			acc.save();
		}
		catch(NumberFormatException e){
			if(selected != null){
				if(selected.getType() == TYPE.Plane){
					((Plane)selected).use(msg, userID);
				}
				else if(selected.getType() == TYPE.Upgrade){
					((Upgrade)selected).use(msg, userID);
				}
			}
		}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}
}
