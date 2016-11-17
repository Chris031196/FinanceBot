package persistence.accounts;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.telegram.telegrambots.api.objects.User;

import main.IOController;
import old.htmlTest.Test;
import view.MainMenu;
import view.Menu;

public class AccountManager {
	
	private static final String accountsFile = "save/accounts.mgs";

	private static AccountManager instance;
	private HashMap<Integer, Account> loggedInAccounts;

	public static AccountManager getInstance(){
		return instance == null ? instance = new AccountManager() : instance;
	}

	private AccountManager(){
		loggedInAccounts = new HashMap<Integer, Account>();
	}

	public Account addAccount(User user) {
		loggedInAccounts.put(user.getId(), new Account(user.getId(), user.getFirstName()));
		IOController.sendMessage("Willkommen "+user.getFirstName() +"!\nEin neuer Account mit 10.000$ Startkapital wurde für Sie angelegt!", null, user.getId().toString(), true);
		return loggedInAccounts.get(user.getId());
	}

	public Account getAccount(Integer userID){
		return loggedInAccounts.get(userID);
	}

	public void login(int userID) {
		Account acc = Account.loadAccount(userID);
		loggedInAccounts.put(acc.getID(), acc);
		
		MainMenu menu = new MainMenu();
		acc.setMenu(menu);
		menu.show(userID);
	}

	public void logout(Integer userID) {
		IOController.deleteLastMessages(userID.toString());
		getAccount(userID).save();
		loggedInAccounts.remove(getAccount(userID));
	}
	
	public boolean transferMoney(Integer originID, Integer targetID, double money) {
		Inventory origin = getAccount(originID).getInventory();
		Inventory target = getAccount(targetID).getInventory();
		if(money <= 0){
			return false;
		}
		if(origin.getMoney() >= money){
			origin.addMoney(-money);
			target.addMoney(money);
			return true;
		}
		else {
			return false;
		}	
	}
	
	public boolean isRegistered(Integer iD) {
		for(Account acc: getAllAccounts()) {
			if(acc.getID().equals(iD)){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Account> getAllAccounts(){
		ArrayList<Account> accounts = new  ArrayList<Account>();
		Properties accs = new Properties();

		try {
			accs.load(new FileInputStream(accountsFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : accs.stringPropertyNames()) {
			Account acc = Account.loadAccount(Integer.parseInt(key));
			accounts.add(acc);
		}
		
		return accounts;
	}

	public void messageReceived(String msg, User user){
		if("/logout".equals(msg)){
			logout(user.getId());
			return;
		}
		if("/login".equals(msg)){
			login(user.getId());
			return;
		}
		//TODO html test
		if("/html".equals(msg)){
			IOController.sendMessage(Test.getHTML(), null, user.getId().toString(), false);
			return;
		}
		Account account = getAccount(user.getId()) != null ? getAccount(user.getId()) : addAccount(user);
		Menu menu = account.getCurMenu();
		menu.messageReceived(msg, user.getId());
	}
}
