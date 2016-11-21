package view.inventory;

import controller.FinanceController;
import main.IOController;
import persistence.accounts.AccountManager;
import view.Menu;

public class TransferMenu extends Menu {

	private double transferAmount = 0;
	private Integer transferTargetID = 0;

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Wem möchten Sie Geld überweisen? Bitte User ID senden:", new String[]{"🔙","cancel"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		if(msg.equals("cancel")){
			cancel(userID);
			return;
		}
		AccountManager manager = AccountManager.getInstance();

		if(transferTargetID == 0){
			try{
				transferTargetID = Integer.parseInt(msg);
			}
			catch(NumberFormatException e){
				IOController.sendMessage("Keine Zahl!", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			if(manager.isRegistered(transferTargetID)){
				IOController.sendMessage("Wieviel Geld möchten Sie überweisen?", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			else {
				transferTargetID = 0;
				IOController.sendMessage("Dieser User ist nicht registriert. Bitte User-ID senden:", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
		}
		else if(transferAmount == 0){
			try{
				transferAmount = Double.parseDouble(msg);
			}
			catch(NumberFormatException e){
				IOController.sendMessage("Keine Zahl! (Format: x.x, z.B 39.5)", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			if(manager.transferMoney(userID, transferTargetID, transferAmount)){
				IOController.sendMessage("Dir wurden " +transferAmount +"$ von " +manager.getAccount(userID).getName() +" überwiesen!", new String[]{"🔙","cancel"}, transferTargetID.toString(), true);
				IOController.sendMessage("Überweisung erfolgreich!", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
			else {
				transferAmount = 0;
				IOController.sendMessage("Überweisung nicht möglich! Bitte validen Wert senden:", new String[]{"🔙","cancel"}, userID.toString(), true);
			}
		}
	}

	public void cancel(Integer userID){
		AccountMenu menu = new AccountMenu();
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}

}