package core.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.telegram.telegrambots.api.objects.User;

import core.main.IOController;
import core.market.items.Item;
import core.persistence.Database;
import core.view.MainMenu;
import core.view.MessageListener;

public class AccountManager {

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

	public void saveAll(){
		for(Account acc: loggedInAccounts.values()){
			logout(acc.getID());
		}
	}
	
	public void saveAccount(int userID) {
		Inventory inv = loggedInAccounts.get(userID).getInventory();
		
		String sql = "UPDATE inventory "
					+"SET Money = " +inv.getMoney()
					+", Pop = " +inv.getPop()
					+"WHERE AccountID = " +userID;
		Database.executeUpdate(sql);
		
		sql = "DELETE FROM item WHERE AccountID = " +userID;
		Database.executeUpdate(sql);
		
		sql = "INSERT INTO item VALUES ";
		for(Item item: inv.getItems()){
			sql += "('" + item.getName() +"', " +item.getValue() +", '" +item.getDescription() +"', '" + item.getAdditionalData() +"'), ";
		}
		sql = sql.substring(0, sql.length()-1);
		sql += ";";
		Database.executeUpdate(sql);
		
	}

	private void loadAccounts(){
		String sql;
		try {
			sql = "SELECT AccountID, Name FROM user";
			ResultSet rsAccounts = Database.executeQuery(sql);

			while(rsAccounts.next()){
				Account account = new Account(rsAccounts.getInt(1), rsAccounts.getString(2));

				sql = "SELECT Pop, Money FROM inventory WHERE inventory.AccountID = " +account.getID();
				ResultSet rsInventory = Database.executeQuery(sql);

				rsInventory.next();
				Inventory inv = new Inventory();
				inv.addPop(rsInventory.getInt(1));
				inv.addMoney(rsInventory.getDouble(1));
				
				sql = "SELECT * FROM item WHERE AccountID = " +account.getID();
				ResultSet rsItems = Database.executeQuery(sql);
				
				while(rsItems.next()){
					Item item = Item.getItem(rsItems.getString(2), rsItems.getString(3), rsItems.getDouble(4), rsItems.getString(5), rsItems.getString(6));
					inv.addItem(item);
				}

				account.setInventory(inv);
				loggedInAccounts.put(account.getID(), account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		saveAccount(userID);
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
