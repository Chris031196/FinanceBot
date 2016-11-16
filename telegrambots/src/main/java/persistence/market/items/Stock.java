package persistence.market.items;

import controller.FinanceController;
import functions.stock.Company;
import functions.stock.StockmarketController;

public class Stock extends Item {
	
	private static StockmarketController controller = StockmarketController.getInstance();
	
	int number;
	double lastChange;
	
	public Stock(String name, int number, double value, double lastChange) {
		this.name = name;
		this.number = number;
		this.value = value*number;
		this.lastChange = lastChange;
		this.type = TYPE.Stock;
	}
	
	public void add(int number) {
		this.number += number;
	}
	
	public void registerAsMain(){
		controller.registerCompany(name, number, lastChange);
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String print() {
		String msg = "";
		Company comp = StockmarketController.getInstance().getCompany(name);
		if(comp.getLastChange() > 0){
			msg += "*"+comp.getName() +", " +FinanceController.round(comp.getValue()) +"$, " +FinanceController.round(comp.getLastChange()) +"%*\n";
		}
		else if (comp.getLastChange() <= 0){
			msg += comp.getName() +", " +FinanceController.round(comp.getValue()) +"$, " +FinanceController.round(comp.getLastChange()) +"%\n";
		}
		return msg;
	}
	
	public void setLastChange(double change) {
		this.lastChange = change;
	}

	@Override
	public Item copy() {
		return new Stock(name, number, value, lastChange);
	}

	@Override
	public void setOptions(String[] options) {
		try {
			String num = options[0].replace("$", "");
			this.number = Integer.parseInt(options[0]);
		}
		catch(NumberFormatException e){}
	}

	@Override
	public String[] getOptionMessages() {
		String[] options = new String[1];
		options[0] = "Für wieviel Geld möchten Sie Aktien der Firma " +name +" kaufen? (Bitte Betrag in $ angeben!)";
		return options;
	}

	@Override
	public String toSaveString() {
		return name +NEXT+ number +NEXT+ value +NEXT+ lastChange;
	}

	@Override
	public void stringToObject(String string) {
		String[] data = string.split(NEXT);
		this.name = data[0];
		this.number = Integer.parseInt(data[1]);
		this.value = Double.parseDouble(data[2]);
		this.lastChange = Double.parseDouble(data[3]);
	}
	
}
