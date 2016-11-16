package persistence.market;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import main.IOController;
import persistence.Stringable;
import persistence.accounts.AccountManager;
import persistence.accounts.Inventory;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;
import persistence.market.items.Plane;
import persistence.market.items.Stock;
import persistence.market.items.Upgrade;

public class MarketManager {

	private static final String marketFile = "save/market.mgs";		
	ArrayList<Item> marketItems;

	private static MarketManager instance;

	public static MarketManager getInstance(){
		return instance == null ? new MarketManager() : instance;
	}

	public MarketManager(){
		marketItems = new ArrayList<Item>();
	}
	
	public void init() {
		loadMarket();
	}

	public void buyItem(Item item, String[] options, Integer userID) {
		Inventory inv = AccountManager.getInstance().getAccount(userID).getInventory();
		if(inv.getMoney() >= item.getValue()){
			Item userItem = item.copy();
			inv.addMoney(-userItem.getValue());
			userItem.setValue(userItem.getValue()*(3.0/4.0));
			userItem.setOptions(options);
			inv.addItem(userItem);
			IOController.sendMessage("Kauf erfolgreich!", null, userID.toString(), true);
		}
		else {
			IOController.sendMessage("Kauf fehlgeschlagen! (insufficient funds)", null, userID.toString(), true);
		}
	}
	private void loadMarket(){
		Properties marketSave = new Properties();

		try {
			marketSave.load(new FileInputStream(marketFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : marketSave.stringPropertyNames()) {
			String itemDesc = marketSave.getProperty(key);
			Item item = null;
			switch(itemDesc.split(Stringable.NEXT)[1]){
			case "Plane":
				item = new Plane("", 0, "", 0);
				item.stringToObject(itemDesc);
				break;

			case "Upgrade":
				item = new Upgrade("", 0, "", 0);
				item.stringToObject(itemDesc);
				break;

			case "Stock":
				item = new Stock("", 0, 0, 0);
				item.stringToObject(itemDesc);
			}

			marketItems.add(item);
		}
	}

	public void saveMarket() {
		Properties marketSave = new Properties();

		for(Item item: marketItems){
			marketSave.put(item.getName(), item.toSaveString());
		}
		try {
			marketSave.store(new FileOutputStream(marketFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ArrayList<Item> getItemsOfType(TYPE type){
		ArrayList<Item> itemsOfType = new ArrayList<Item>();
		for(Item item: marketItems){
			if(item.getType().equals(type)){
				itemsOfType.add(item);
			}
		}
		return itemsOfType;
	}
}