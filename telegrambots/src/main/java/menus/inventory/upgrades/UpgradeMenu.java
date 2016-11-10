package menus.inventory.upgrades;

import main.FinanceController;
import main.IOController;
import menus.generic.SellMenu;
import menus.inventory.InventoryMenu;
import util.Account;
import util.Item;
import util.Item.TYPE;
import util.Menu;

public class UpgradeMenu extends Menu{
	
	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		String inventory = "Upgrades:\n\n";
		Account acc = c.getAccount(userID);
		for(Item item: acc.getInventory().getItems()){
			if(item.getType() == TYPE.Upgrade)
				inventory += item.getName() + ":\nWert: "+c.round(item.getValue()) +"$\nÜberlandchance: " +item.getChance() +"%\n" +item.getDescription() + "\n\n";
		}

		IOController.sendMessage(inventory, new String[]{"Upgrade verkaufen","sell","Upgrade anbringen","use","🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		if(msg.equals("sell")){
//			SellMenu menu = new SellMenu();
//			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
//			menu.show(userID);
		}
		else if(msg.equals("use")){
			AttachMenu menu = new AttachMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}

	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
		menu.show(userID);
	}

}
