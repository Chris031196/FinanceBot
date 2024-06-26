package menus.market;

import main.FinanceController;
import main.IOController;
import util.Company;
import util.Menu;

public class StocksMenu extends Menu{

	@Override
	public void show(Integer userID) {
		FinanceController c = FinanceController.getInstance();
		String msg = "Unternehmen, Wert pro Aktie, letze Änderung\n";
		for(Company comp: c.getCompanies().values()){
			
			if(comp.getLastChange() > 0){
				msg += "*"+comp.getName() +", " +c.round(comp.getValue()) +"$, " +c.round(comp.getLastChange()) +"%*\n";
			}
			else if (comp.getLastChange() <= 0){
				msg += comp.getName() +", " +c.round(comp.getValue()) +"$, " +c.round(comp.getLastChange()) +"%\n";
			}
		}
		IOController.sendMessage(msg, new String[]{"Aktien kaufen","buy","Aktiendatei laden","load","🔙", "back"}, userID.toString(), false);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.equals("back")){
			MarketMenu sMenu = new MarketMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(sMenu);
			sMenu.show(userID);
		} else if(msg.equals("buy")){
			BuyStocksMenu sMenu = new BuyStocksMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(sMenu);
			sMenu.show(userID);
		} else if (msg.equals("load")){
			IOController.sendData(FinanceController.logFile, userID.toString());
		}
	}
}


