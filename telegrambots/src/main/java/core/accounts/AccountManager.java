package core.accounts;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.telegram.telegrambots.api.objects.User;

import core.main.IOController;
import core.view.MainMenu;
import core.view.MessageListener;
import htmlTest.Test;

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

	public void init() {
		loadAccounts();
	}

	private void loadAccounts(){
		Properties accountList = new Properties();
		try {
			accountList.load(new FileInputStream(accountsFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(String key: accountList.stringPropertyNames()){
			loggedInAccounts.put(Integer.parseInt(key), Account.loadAccount(Integer.parseInt(key)));
		}
	}

	public Account addAccount(User user) {
		loggedInAccounts.put(user.getId(), new Account(user.getId(), user.getFirstName()));
		IOController.sendMessage("Willkommen "+user.getFirstName() +"!\nEin neuer Account mit 10.000$ Startkapital wurde für Sie angelegt!", null, user.getId().toString(), true);
		return loggedInAccounts.get(user.getId());
	}

	public Account getAccount(Integer userID){
		Account acc = loggedInAccounts.get(userID);
		return acc;
	}

	public void login(int userID) {
		MainMenu menu = new MainMenu();
		getAccount(userID).setListener(menu);
		menu.show(userID);
	}

	public void logout(Integer userID) {
		getAccount(userID).save();
		getAccount(userID).lastSentMsg = null;
	}

	public boolean transferMoney(Integer originID, Integer targetID, double money) {
		Inventory origin = getAccount(originID).getInventory();
		Inventory target = getAccount(targetID).getInventory();
		if(money <= 0) {
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

		for (Account acc: loggedInAccounts.values()) {
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
		//		if("/html".equals(msg)){
		//			System.out.println(Test.getHTML());
		//			IOController.sendMessage(Test.getHTML(), null, "205364667", true);
		//			return;
		//		}
		Account account = getAccount(user.getId()) != null ? getAccount(user.getId()) : addAccount(user);
		MessageListener listener = account.getListener();
		if(listener != null) {
			listener.messageReceived(msg, user.getId());
		}
	}
}
