package core.market;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import addons.TYPE;
import core.accounts.AccountManager;
import core.accounts.Inventory;
import core.market.items.Item;
import core.persistence.Database;

public class MarketManager {

	ArrayList<Item> marketItems;

	private static MarketManager instance;

	public static MarketManager getInstance(){
		return instance == null ? instance = new MarketManager() : instance;
	}

	public MarketManager(){
		marketItems = new ArrayList<Item>();
	}

	public void init() {
		loadMarket();
	}

	public boolean buyItem(Item item, String[] options, Integer userID) {
		Inventory inv = AccountManager.getInstance().getAccount(userID).getInventory();
		Item userItem = item.copy();
		userItem.setOptions(options);
		if(inv.getMoney() >= item.getValue()){
			inv.addMoney(-userItem.getValue());
			inv.addItem(userItem);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean sellItem(Item item, Integer userID){
		Inventory inv = AccountManager.getInstance().getAccount(userID).getInventory();
		inv.addMoney(item.getValue());
		inv.getItems().remove(item);
		return true;
	}
	
	private void loadMarket(){
		String sql = "SELECT * FROM item WHERE AccountID = 0";
		ResultSet rs = Database.executeQuery(sql);
		
		try {
			while(rs.next()){
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getTypesAsString(){
		String[] types = new String[TYPE.values().length];
		for(int i=0;i<types.length;i++){
			types[i] = TYPE.values()[i].name();
		}
		return types;
	}

	public void saveMarket() {
		Properties marketSave = new Properties();

		synchronized (marketItems){
			for(Item item: marketItems){
				marketSave.put(item.getName(), item.toSaveString());
			}
		}
		try {
			marketSave.store(new FileOutputStream(marketFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ArrayList<Item> getItemsOfType(TYPE type){
		ArrayList<Item> itemsOfType = new ArrayList<Item>();
		synchronized (marketItems) {
			for(Item item: marketItems){
				if(item.getType().equals(type)){
					itemsOfType.add(item);
				}
			}
		}
		return itemsOfType;
	}
}