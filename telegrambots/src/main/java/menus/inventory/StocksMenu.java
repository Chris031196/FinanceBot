package menus.inventory;

import java.util.HashMap;
import java.util.Map.Entry;

import main.FinanceController;
import main.IOController;
import util.Company;
import util.Menu;

public class StocksMenu extends Menu {

	
	private String stock;
	
	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		String msg;
		String[] buttons = new String[]{};
		HashMap<String, Integer> stocks = c.getAccount(userID).getInventory().getStocks();

		if(stocks.isEmpty()){
			msg = "Du besitzt keine Aktien!";
			buttons = new String[]{"🔙","back"};
		}
		else {
			msg = "Du besitzt folgende Aktien:\n\n";
			buttons = new String[(stocks.size()*2)+2];
			int i = 0;
			for(Entry<String, Integer> entry: stocks.entrySet()){
				Company comp = c.getCompanies().get(entry.getKey());
				msg += "Unternehmen: " +entry.getKey() +"\nMenge: " +entry.getValue() +"\nGesamtwert:" + (c.round(entry.getValue()*comp.getValue())) +"$\n\n";
				buttons[i] = entry.getKey() +" verkaufen.";
				buttons[i+1] = entry.getKey();
				i += 2;
			}
			buttons[i] = "🔙";
			buttons[i+1] = "back";
		}
		IOController.sendMessage(msg, buttons, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}
		FinanceController c = FinanceController.getInstance();
		switch(msg){
		case "back": 
			cancel(userID);
			break;
		default:
			HashMap<String, Integer> stocks = c.getAccount(userID).getInventory().getStocks();
			if(stocks.containsKey(msg)){
				stock = msg;
				IOController.sendMessage("Wieviele Ihrer Aktien möchten Sie verkaufen? (Bitte Anzahl senden)", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			else {
				try{
					int amount = Integer.parseInt(msg);
					if(c.sellStocks(userID, stock, amount)){
						IOController.sendMessage("Verkauf erfolgreich!", new String[]{"🔙","cancel"}, userID.toString(), true);
					}
					else {
						IOController.sendMessage("Verkauf fehlgeschgeschlagen! So viele Aktien besitzen Sie nicht!\n Wieviele Aktien möchten Sie verkaufen?", new String[]{"🔙","cancel"}, userID.toString(), true);
					}
				}
				catch(NumberFormatException e){
					IOController.sendMessage("Keine Zahl!", new String[]{"🔙","cancel"}, userID.toString(), true);
				}
			}
			break;
		}
	}

	public void cancel(Integer userID){
		InventoryMenu menu = new InventoryMenu();
		FinanceController.getInstance().getAccount(userID).setMenu(menu);
		menu.show(userID);
	}

}
