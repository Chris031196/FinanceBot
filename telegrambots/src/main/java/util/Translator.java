package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import addons.planes.items.Certificate;
import addons.planes.items.Plane;
import addons.planes.items.Upgrade;
import addons.stocks.items.Stock;
import core.Stringable;
import core.accounts.Account;
import core.accounts.Inventory;
import core.market.items.Item;
import core.persistence.Database;

public class Translator implements Stringable{

	public static void main(String[] args) {
		Properties accs = new Properties();

		try {
			accs.load(new FileInputStream("save/accounts.mgs"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : accs.stringPropertyNames()) {
			Account acc = loadAccount(Integer.parseInt(key));
			saveAccount(acc);
		}

		Properties market = new Properties();

		try {
			market.load(new FileInputStream("save/market.mgs"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : market.stringPropertyNames()) {
			String itemDesc = market.getProperty(key);

			String[] stringArray = itemDesc.split(NEXT);
			String type = stringArray[1];
			Item item = null;
			String[] dat = null;
			switch(type){
			case "Plane":
				dat = itemDesc.split(NEXT);
				item = new Plane(dat[0], Double.parseDouble(dat[2]), dat[4], dat[3]);
				break;

			case "Upgrade":
				dat = itemDesc.split(NEXT);
				item = new Upgrade(dat[0], Double.parseDouble(dat[2]), dat[4], dat[3]);
				break;

			case "Stock":
				dat = itemDesc.split(NEXT);
				item = new Stock(dat[0], Double.parseDouble(dat[2]), dat[4], dat[2] +NEXT +dat[4]);
				break;

			case "Certificate":
				dat = itemDesc.split(NEXT);
				item = new Certificate(dat[0], 0, dat[2], "");
				break;
			}
			
			String sql = "INSERT INTO item (AccountID, Name, Type, Value, Description, AdditionalData) VALUES ";
			sql += "(0, '" + item.getName() +"', '" + item.getType() +"', "+item.getValue() +", '" + item.getDescription() +"', '" + item.getAdditionalData() +"');";
			System.out.println(sql);
			Database.executeUpdate(sql);
		}
	}

	public static void saveAccount(Account acc) {
		Inventory inv = acc.getInventory();

		String sql = "INSERT INTO inventory VALUES ("
				+"'" +acc.getID() +"', "
				+ inv.getPop() +", "
				+ inv.getMoney() +")";
		Database.executeUpdate(sql);
		
		sql = "INSERT INTO user VALUES ("
				+acc.getID() +", '" + acc.getName() +"')";
		Database.executeUpdate(sql);

		sql = "DELETE FROM item WHERE AccountID = " +acc.getID();
		Database.executeUpdate(sql);

		if(inv.getItems().size() > 0){
			sql = "INSERT INTO item (AccountID, Name, Type, Value, Description, AdditionalData) VALUES ";
			for(Item item: inv.getItems()){
				sql += "(" +acc.getID() +", '" + item.getName() +"', '" + item.getType() +"', "+item.getValue() +", '" + item.getDescription() +"', '" + item.getAdditionalData() +"'), ";
			}
			sql = sql.substring(0, sql.length()-2);
			sql += ";";
			System.out.println(sql);
			Database.executeUpdate(sql);
		}
	}

	private static Account loadAccount(Integer userID){

		Properties save = new Properties();
		try {
			save.load(new FileInputStream("save/accounts/" +userID +".mgs"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Account account = new Account(userID, save.getProperty("name"));
		Inventory inv = new Inventory();

		String[] parts = save.getProperty("inv").split(";newitem;");

		String[] data = parts[0].split(NEXT);
		inv.addPop(Integer.parseInt(data[0]));
		inv.addMoney(Double.parseDouble(data[1]));

		for(int i=1;i<parts.length;i++) {
			String[] stringArray = parts[i].split(NEXT);
			String type = stringArray[1];
			Item item = null;
			String[] dat = null;
			switch(type){
			case "Plane":
				dat = parts[i].split(NEXT);
				item = new Plane(dat[0], Double.parseDouble(dat[2]), dat[4], dat[3]);
				break;

			case "Upgrade":
				dat = parts[i].split(NEXT);
				item = new Upgrade(dat[0], Double.parseDouble(dat[2]), dat[4], dat[3]);
				break;

			case "Stock":
				dat = parts[i].split(NEXT);
				item = new Stock(dat[0], Double.parseDouble(dat[2]), dat[4], dat[2] +NEXT +dat[4]);
				break;

			case "Certificate":
				dat = parts[i].split(NEXT);
				item = new Certificate(dat[0], 0, dat[2], "");
				break;
			}
			inv.addItem(item);
		}
		account.setInventory(inv);

		return account;
	}
}
