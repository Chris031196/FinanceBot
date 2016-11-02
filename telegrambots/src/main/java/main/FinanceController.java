package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.telegram.telegrambots.api.objects.User;

import util.Account;
import util.Company;
import util.Item;

public class FinanceController {

	private HashMap<Integer, Account> accounts;
	private HashMap<String, Company> companies;
	private ArrayList<Item> market;

	private boolean running;
	private static final double MIN_PAY = 1000;
	
	public static final String logFile = "save/log.stck";
	public static final String companiesFile = "save/companies.mgs";
	public static final String accountsFile = "save/accounts.mgs";
	public static final String marketFile = "save/market.mgs";
	
	public static final String accountsFolder = "save/accounts/";

	private static FinanceController instance;

	public static FinanceController getInstance(){
		return instance == null ? instance = new FinanceController() : instance;
	}

	private FinanceController() {}

	public void init(){
		accounts = new HashMap<Integer, Account>();
		companies = new HashMap<String, Company>();
		market = new ArrayList<Item>();
		load();
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
		
		for(Company company: companies.values()){
			company.startCircleOfLife();
		}
	}

	public boolean transferMoney(Integer userIDOrigin, Integer userIDTarget, double money) {
		if(money <= 0){
			return false;
		}
		Account origin = getAccount(userIDOrigin);
		Account target = getAccount(userIDTarget);
		if(origin.getMoney() >= money){
			origin.addMoney(-money);
			target.addMoney(money);
			origin.save();
			target.save();
			return true;
		}
		else {
			return false;
		}	
	}
	
	public void lookForPlus(String msg, Integer userID){
		if(msg.contains("+")){
			String[] words = msg.split(" ");
			for(int i=0;i<words.length;i++){
				if(words[i].endsWith("+")){
					char[] letters = words[i].toCharArray();
					String name = "";
					int counter = 0;
					for(int j=0;j<letters.length;j++){
						if(letters[j] == '+'){
							counter++;
						}
						else {
							if(counter == 0){
								name += letters[j];
							}
							else{
								return;
							}
						}
					}
					for(Entry<Integer, Account> entry: accounts.entrySet()){
						if(name.equals(entry.getValue().getName()) && !name.equals(accounts.get(userID).getName())){
							entry.getValue().addPop(counter);
							IOController.sendMessage("Du hast " +counter +"+ bekommen!", null, entry.getValue().getID().toString(), true);
							entry.getValue().save();
						}
					}
				}
			}
		}
	}

	public boolean buyStocks(Integer userID, String company, int amount){
		if(!companies.containsKey(company) || amount <= 0){
			return false;
		}
		Account acc = getAccount(userID);
		double price = companies.get(company).getValue()*((double) amount);
		if(acc.getMoney() >= price){
			acc.addMoney(-price);
			if(acc.getStocks().containsKey(company)){
				int userAmount = acc.getStocks().get(company);
				acc.getStocks().remove(company);
				acc.getStocks().put(company, userAmount+amount);
			}
			else {
				acc.getStocks().put(company, amount);
			}
			acc.save();
			return true;
		}
		return false;
	}
	
	public boolean sellStocks(Integer userID, String company, int amount){
		if(amount < 0 || !companies.containsKey(company)){
			return false;
		}
		Account acc = getAccount(userID);
		int userAmount = acc.getStocks().get(company);
		if(amount == 0){
			amount = acc.getStocks().get(company);
			acc.getStocks().remove(company);
			acc.addMoney(amount*companies.get(company).getValue());
			acc.save();
			return true;
		}
		else if(amount < userAmount){
			acc.getStocks().remove(company);
			acc.getStocks().put(company, userAmount-amount);
			acc.addMoney(amount*companies.get(company).getValue());
			acc.save();
			return true;
		}
		else if(amount == userAmount){
			acc.getStocks().remove(company);
			acc.addMoney(amount*companies.get(company).getValue());
			acc.save();
			return true;
		}
			
		return false;
	}

	public Account addAccount(User user) {
		accounts.put(user.getId(), new Account(user.getId(), user.getFirstName(), 10000.0, 0, new HashMap<String, Integer>()));
		saveAll();
		IOController.sendMessage("Willkommen "+user.getFirstName() +"!\nEin neuer Account mit 10.000$ Startkapital wurde für Sie angelegt!", null, user.getId().toString(), true);
		return accounts.get(user.getId());
	}

	private void payUsers() {
		for(Account acc: accounts.values()){
			double pay = acc.getPop() + MIN_PAY;
			acc.addMoney(pay);
			IOController.sendMessage("Gehalt!", null, acc.getID().toString(), true);
		}
		saveAll();
	}

	private void saveAll() {
		Properties comps = new Properties();

		for(Company comp: companies.values()){
			comps.put(comp.getName(), comp.toString());
		}
		try {
			comps.store(new FileOutputStream(companiesFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Properties accs = new Properties();

		for(Account acc: accounts.values()){
			acc.save();
			accs.put(acc.getID().toString(), "");
		}
		try {
			accs.store(new FileOutputStream(accountsFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void load(){
		loadCompanies();
		loadAccounts();
		loadMarket();
	}
	
	private void loadCompanies(){
		Properties comps = new Properties();

		try {
			comps.load(new FileInputStream(companiesFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String key : comps.stringPropertyNames()) {
			String[] companyData = comps.getProperty(key).split("_");
			companies.put(key, new Company(key, Double.parseDouble(companyData[1]), Double.parseDouble(companyData[2])));
		}
	}
	private void loadAccounts(){
		Properties accs = new Properties();

		try {
			accs.load(new FileInputStream(accountsFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : accs.stringPropertyNames()) {
			Account acc = new Account(Integer.parseInt(key));
			accounts.put(acc.getID(), acc);
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
			Item item = Item.getNewItem(itemDesc);
			market.add(item);
		}
	}
	
	public void messageToAllUsers(String message){
		for(Integer iD: accounts.keySet()){
			IOController.sendMessage(message, null, iD.toString(), true);
		}
	}
	
	public double round(double number){
		int rounded = (int) (number*100);
		number = ((double) rounded) / 100;
		return number;
	}

	public Account getAccount(Integer userID){
		return accounts.get(userID);
	}

	public HashMap<Integer, Account> getAccounts() {
		return accounts;
	}

	public ArrayList<Item> getMarket() {
		return market;
	}

	public HashMap<String, Company> getCompanies() {
		return companies;
	}

	public boolean isRunning() {
		return running;
	}

	public void stockChanged(String company) {
		Company comp = companies.get(company);
		for(Account acc: accounts.values()){
			if(acc.getStocks().containsKey(company)){
				IOController.sendMessage("Der Wert von " +company +" hat sich um " +round(comp.getLastChange()) + "% geändert!", null, acc.getID().toString(), true);
			}
		}
		saveAll();
	}

	public void sellAllStocks(Integer userID) {
		Account acc = accounts.get(userID);
		for(String company: acc.getStocks().keySet()){
			sellStocks(userID, company, 0);
		}
		
	}
	
	
}
