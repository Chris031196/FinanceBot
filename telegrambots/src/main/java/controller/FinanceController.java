package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.accounts.Inventory;

public class FinanceController {

	private boolean running;
	private static final double MIN_PAY = 1000;

	private static FinanceController instance;

	public static FinanceController getInstance(){
		return instance == null ? instance = new FinanceController() : instance;
	}

	private FinanceController() {}

	public void init(){
		startCircleOfLife();
	}
	
	private void startCircleOfLife(){
		running = true;
		Thread payer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(running){
					int hour = LocalDateTime.now().getHour();
					long waitTime = 24 - hour + 7;
					waitTime = waitTime * 60 * 60 * 1000;
					
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					payUsers();
				}
			}
		});
		payer.start();
	}
	
	private void payUsers() {
		ArrayList<Account> accounts = AccountManager.getInstance().getAllAccounts();
		for(Account acc: accounts){
			Inventory inv = acc.getInventory();
			double pay = inv.getPop() + MIN_PAY;
			inv.addMoney(pay);
			IOController.sendMessage("Gehalt! Sie bekommen " +pay +"$!", null, acc.getID().toString(), true);
		}
	}

	public boolean transferMoney(Inventory origin, Inventory target, double money) {
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
	
//	public void lookForPlus(String msg, Integer userID){
//		if(msg.contains("+")){
//			String[] words = msg.split(" ");
//			for(int i=0;i<words.length;i++){
//				if(words[i].endsWith("+")){
//					char[] letters = words[i].toCharArray();
//					String name = "";
//					int counter = 0;
//					for(int j=0;j<letters.length;j++){
//						if(letters[j] == '+'){
//							counter++;
//						}
//						else {
//							if(counter == 0){
//								name += letters[j];
//							}
//							else{
//								return;
//							}
//						}
//					}
//					for(Account acc: AccountManager.getInstance().getAllAccounts()){
//						if(name.equals(acc.getName()) && !name.equals(AccountManager.getInstance().getAccount(userID).getName())){
//							acc..addPop(counter);
//							IOController.sendMessage("Du hast " +counter +"+ bekommen!", null, entry.getValue().getID().toString(), true);
//							entry.getValue().save();
//						}
//					}
//				}
//			}
//		}
//	}

//	public boolean buyStocks(Integer userID, String company, int amount){
//		if(!companies.containsKey(company) || amount <= 0){
//			return false;
//		}
//		Account acc = getAccount(userID);
//		double price = companies.get(company).getValue()*((double) amount);
//		if(acc.getMoney() >= price){
//			acc.addMoney(-price);
//			if(acc.getInventory().getStocks().containsKey(company)){
//				int userAmount = acc.getInventory().getStocks().get(company);
//				acc.getInventory().getStocks().remove(company);
//				acc.getInventory().getStocks().put(company, userAmount+amount);
//			}
//			else {
//				acc.getInventory().getStocks().put(company, amount);
//			}
//			acc.save();
//			return true;
//		}
//		return false;
//	}
	
//	public boolean sellStocks(Integer userID, String company, int amount){
//		if(amount < 0 || !companies.containsKey(company)){
//			return false;
//		}
//		Account acc = getAccount(userID);
//		int userAmount = acc.getInventory().getStocks().get(company);
//		if(amount == 0){
//			amount = acc.getInventory().getStocks().get(company);
//			acc.getInventory().getStocks().remove(company);
//			acc.addMoney(amount*companies.get(company).getValue());
//			acc.save();
//			return true;
//		}
//		else if(amount < userAmount){
//			acc.getInventory().getStocks().remove(company);
//			acc.getInventory().getStocks().put(company, userAmount-amount);
//			acc.addMoney(amount*companies.get(company).getValue());
//			acc.save();
//			return true;
//		}
//		else if(amount == userAmount){
//			acc.getInventory().getStocks().remove(company);
//			acc.addMoney(amount*companies.get(company).getValue());
//			acc.save();
//			return true;
//		}
//			
//		return false;
//	}

	



//	private void saveAll() {
//		Properties comps = new Properties();
//
//		for(Company comp: companies.values()){
//			comps.put(comp.getName(), comp.toString());
//		}
//		try {
//			comps.store(new FileOutputStream(companiesFile), null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		Properties accs = new Properties();
//
//		for(Account acc: accounts.values()){
//			acc.save();
//			accs.put(acc.getID().toString(), "");
//		}
//		try {
//			accs.store(new FileOutputStream(accountsFile), null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	private void load(){
//		loadCompanies();
//		loadAccounts();
//		loadMarket();
//	}
//	

//	private void loadMarket(){
//		Properties marketSave = new Properties();
//
//		try {
//			marketSave.load(new FileInputStream(marketFile));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		for (String key : marketSave.stringPropertyNames()) {
//			String itemDesc = marketSave.getProperty(key);
//			Item item = Item.getNewItem(itemDesc);
//			market.add(item);
//		}
//	}
	
	public void messageToAllUsers(String message){
		for(Account acc: AccountManager.getInstance().getAllAccounts()){
			IOController.sendMessage(message, null, acc.getID().toString(), true);
		}
	}
	
	public static double round(double number){
		int rounded = (int) (number*100);
		number = ((double) rounded) / 100;
		return number;
	}

	public boolean isRunning() {
		return running;
	}



//	public void sellAllStocks(Integer userID) {
//		Account acc = accounts.get(userID);
//		for(String company: acc.getInventory().getStocks().keySet()){
//			sellStocks(userID, company, 0);
//		}
//	}
}
