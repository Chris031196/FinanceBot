package menus.market;

import java.util.HashMap;

import main.FinanceController;
import main.IOController;
import util.Company;
import util.Menu;

public class BuyStocksMenu extends Menu {
	
	String stock;

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		String[] buttons = new String[c.getCompanies().size()*2+2];
		int i = 0;
		for(String comp: c.getCompanies().keySet()){
			buttons[i] = comp +" (" + c.round(c.getCompanies().get(comp).getValue()) + "$ pro Aktie)";
			buttons[i+1] = comp;
			i += 2;
		}
		buttons[i] = "🔙";
		buttons[i+1] = "cancel";
		IOController.sendMessage("Von welchem Unternehmen möchten Sie Aktien kaufen?\n", buttons, userID.toString(), true);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}

		FinanceController c = FinanceController.getInstance();
		HashMap<String, Company> availableStocks = c.getCompanies();
		if(availableStocks.containsKey(msg)){
			stock = msg;
			IOController.sendMessage("Wieviele Aktien möchten Sie kaufen? (Bitte Anzahl senden)", new String[]{"🔙","cancel"}, userID.toString(), true);
		}
		else {
			try{
				int amount = Integer.parseInt(msg);
				if(c.buyStocks(userID, stock, amount)){
					IOController.sendMessage("Kauf erfolgreich!", new String[]{"🔙","cancel"}, userID.toString(), true);
				}
				else {
					IOController.sendMessage("Kauf fehlgeschlagen! (insufficient funds)", new String[]{"🔙","cancel"}, userID.toString(), true);
				}
			}
			catch(NumberFormatException e){
				IOController.sendMessage("Keine Zahl!", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			
		}
	}

	public void cancel(Integer userID){
		StocksMenu menu = new StocksMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}
}
