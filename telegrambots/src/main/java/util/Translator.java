package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import persistence.accounts.Account;

public class Translator {

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
	}
	
	public static void saveAccount(Account acc){
		Properties save = new Properties();
		save.put("name",""+acc.getName());
		save.put("inv", acc.getInventory().toSaveString());

		try {
			save.store(new FileOutputStream("save/accounts/" +acc.getID() +".mgs"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Account loadAccount(Integer userID){
		Properties acc = new Properties();

		try {
			acc.load(new FileInputStream("saveOLD/accounts/" +userID +".mgs"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Account account = new Account(userID, acc.getProperty("name"));
		account.getInventory().addMoney(Double.parseDouble(acc.getProperty("money")));
		account.getInventory().addPop(Integer.parseInt(acc.getProperty("pop")));

		return account;
	}

}
