package functions.upgrade;

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

public class UpgradeFunction implements Function, MessageListener {

	Upgrade upgrade;
	Plane plane;

	public UpgradeFunction(Upgrade upgrade){
		this.upgrade = upgrade;
	}

	@Override
	public void use(Integer userID) {
		AccountManager m = AccountManager.getInstance();
		m.getAccount(userID).addListener(this);
		ArrayList<String> buttons = new ArrayList<String>();
		for(Item item: m.getAccount(userID).getInventory().getItemsOfType(TYPE.Plane)){
			buttons.add(item.getName() +": " +item.getValue() +"$");
			buttons.add(item.print());
		}
		buttons.add("🔙");
		buttons.add("cancel");
		IOController.sendMessage("An welches Flugzeug möchten Sie dieses Upgrade anbringen?", buttons.toArray(new String[]{}), userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		AccountManager m = AccountManager.getInstance();
		if("cancel".equals(msg)){
			ItemDetailsMenu menu = new ItemDetailsMenu(upgrade);
			m.getAccount(userID).setMenu(menu);
			menu.show(userID);
		}
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(m.getAccount(userID).getInventory().getItems());
		for(Item item: items){
			if(msg.equals(item.print()) && !item.getDescription().contains(upgrade.getName())){
				((Plane) item).addUpgrade(upgrade);
				m.getAccount(userID).getInventory().getItems().remove(upgrade);
				if(item.getDescription().contains(upgrade.getName())){
					IOController.sendMessage("Erfolgreich angewendet!", new String[]{"🔙","cancel"}, userID.toString(), false);
				}
			}
			else if(item.getDescription().contains(upgrade.getName())){
				IOController.sendMessage("Das Flugzeug besitzt dieses Upgrade bereits!", new String[]{"🔙","cancel"}, userID.toString(), false);
			}
		}
	}

}
