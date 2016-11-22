package functions.stock;

import java.util.ArrayList;

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
		return instance == null ? instance = new StockmarketController() : instance;
	}

	private StockmarketController(){
		companies = new ArrayList<Company>();
	}

	public void init() {
		LOOP: for(Item item: MarketManager.getInstance().getItemsOfType(TYPE.Stock)) {
			for(Company company: companies){
				if(company.getName().equals(item.getName())){
					continue LOOP;
				}
			}
			Company comp = new Company(item.getName(), item.getValue(), ((Stock) item).getLastChange());
			companies.add(comp);
			comp.startCircleOfLife();
		}
	}

	//	public void register(String name, double value, double lastChange){
	//		System.out.println(name);
	//		Company comp = new Company(name, value, lastChange);
	//		for(Account acc: AccountManager.getInstance().getAllAccounts()){
	//			for(Item item: acc.getInventory().getItemsOfType(TYPE.Stock)){
	//				if(item.getName().equals(comp.getName())){
	//					comp.addHolder(acc.getID());
	//				}
	//			}
	//		}
	//		
	//		for(Company temp: companies){
	//			if(temp.getName().equals(name)){
	//				return;
	//			}
	//		}
	//		companies.add(comp);
	//		System.out.println("Company \"" +name +"\" registered!");
	//		comp.startCircleOfLife();
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
					Stock stock = (Stock) item;
					stock.setValue(company.getValue());
					stock.setLastChange(company.getLastChange());
					IOController.sendMessage("Der Wert von " +company.getName() +" hat sich um " +FinanceController.round(company.getLastChange()) + "% geändert!", null, acc.getID().toString(), false);
				}
			}
		}
	}
}
