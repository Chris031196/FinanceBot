package functions.stock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.Account;
import persistence.accounts.AccountManager;
import persistence.market.MarketManager;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;
import persistence.market.items.Stock;

public class StockmarketController {

//	private static final String companiesFile = "save/companies.mgs";

	ArrayList<Company> companies;

	private static StockmarketController instance;

	public static StockmarketController getInstance(){
		return instance == null ? new StockmarketController() : instance;
	}

	private StockmarketController(){
		companies = new ArrayList<Company>();
	}

	public void registerCompany(String name, double value, double lastChange){
		Company comp = new Company(name, value, lastChange);
		companies.add(comp);
		comp.startCircleOfLife();
	}

	//	private void loadCompanies() {
	//		companies = new ArrayList<Company>();
	//		Properties comps = new Properties();
	//
	//		try {
	//			comps.load(new FileInputStream(companiesFile));
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		for (String key : comps.stringPropertyNames()) {
	//			String companyData = comps.getProperty(key);
	//			Company comp = new Company(key, 0, 0);
	//			comp.stringToObject(companyData);
	//			comp.startCircleOfLife();
	//		}
	//	}
	//	
	//	private void saveCompanies() {
	//		Properties comps = new Properties();
	//
	//		for(Company comp: companies){
	//			comps.put(comp.getName(), comp.toSaveString());
	//		}
	//		try {
	//			comps.store(new FileOutputStream(companiesFile), null);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public Company getCompany(String name) {
		Company company = null;
		for(Company comp: companies){
			if(comp.getName().equals(name)){
				company = comp;
			}
		}
		return company;
	}

	public void stockChanged(Company company) {
		for(Item item: MarketManager.getInstance().getItemsOfType(TYPE.Stock)){
			if(item.getName().equals(company.getName())){
				Stock stock = (Stock) item;
				stock.setValue(company.getValue()*stock.getNumber());
				stock.setLastChange(company.getLastChange());
			}
		}
		MarketManager.getInstance().saveMarket();

		for(Account acc: AccountManager.getInstance().getAllAccounts()){
			for(Item item: acc.getInventory().getItemsOfType(TYPE.Stock)){
				if(item.getName().equals(company.getName())){
					IOController.sendMessage("Der Wert von " +company.getName() +" hat sich um " +FinanceController.round(company.getLastChange()) + "% geändert!", null, acc.getID().toString(), true);
					Stock stock = (Stock) item;
					stock.setValue(company.getValue()*stock.getNumber());
					stock.setLastChange(company.getLastChange());
				}
			}
		}
	}
}
