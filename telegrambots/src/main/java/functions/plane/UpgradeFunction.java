package functions.plane;

import java.util.ArrayList;

import functions.Function;
import main.IOController;
import persistence.accounts.AccountManager;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;
import persistence.market.items.Plane;
import persistence.market.items.Upgrade;
import view.MessageListener;
import view.inventory.ItemDetailsMenu;
import view.inventory.ItemListMenu;

public class UpgradeFunction implements Function, MessageListener {

	Upgrade upgrade;
	Plane plane;

	public UpgradeFunction(Upgrade upgrade){
		this.upgrade = upgrade;
	}

	@Override
	public void use(Integer userID) {
		AccountManager m = AccountManager.getInstance();
		m.getAccount(userID).setListener(this);
		ArrayList<String> buttons = new ArrayList<String>();
		int index = 0;
		for(Item item: m.getAccount(userID).getInventory().getItemsOfType(TYPE.Plane)){
			buttons.add(item.getName() +": " +item.getValue() +"$");
			buttons.add("" +index);
			index++;
		}
		buttons.add("🔙");
		buttons.add("cancel");
		IOController.sendMessage("An welches Flugzeug möchten Sie dieses Upgrade anbringen?", buttons.toArray(new String[]{}), userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		AccountManager m = AccountManager.getInstance();
		if("cancel".equals(msg)){
			ItemListMenu menu = new ItemListMenu(userID, TYPE.Upgrade, "Ihre Upgrades:");
			m.getAccount(userID).setListener(menu);
			menu.show(userID);
			return;
		}
		Item item = m.getAccount(userID).getInventory().getItemsOfType(TYPE.Plane).get(Integer.parseInt(msg));
		if(!item.getDescription().contains(upgrade.getName())){
			((Plane) item).addUpgrade(upgrade);
			m.getAccount(userID).getInventory().getItems().remove(upgrade);
			if(item.getDescription().contains(upgrade.getName())){
				IOController.sendMessage("Erfolgreich angewendet!", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
		}
		else {
			IOController.sendMessage("Das Flugzeug besitzt dieses Upgrade bereits!", new String[]{"🔙","cancel"}, userID.toString(), true);
		}
	}

}
