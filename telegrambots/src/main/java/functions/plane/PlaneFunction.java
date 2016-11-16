package functions.plane;

import java.time.LocalDateTime;

import controller.FinanceController;
import functions.Function;
import main.IOController;
import old.menus.inventory.InventoryMenu;
import persistence.accounts.Account;
import persistence.market.items.Plane;
import view.Menu;

public class PlaneFunction implements Function {

	@Override
	public void use(String msg, Integer userID) {
		int hour = LocalDateTime.now().getHour();
		if(hour <= 12 && hour >= 8){
			FlyMenu menu = new FlyMenu(this);
			FinanceController.getInstance().getAccount(userID).setMenu(menu);
			menu.show(userID);
		}
		else {
			IOController.sendMessage("Sie sollten zwischen 09:00 Uhr und 13:00 Uhr starten. Sonst haben Sie keine Chance!", null, userID.toString(), true);
		}
	}
	
	private class FlyMenu extends Menu {

		Plane plane;

		public FlyMenu(Plane plane) {
			this.plane = plane;
		}

		@Override
		public void show(Integer userID) {
			IOController.sendMessage("Wie viele Kilometer möchten Sie ausschreiben?", new String[]{"100km FAI","100","300km FAI","300","500km FAI","500","700km FAI","700","1000km FAI","1000","🔙","cancel"}, userID.toString(), false);
		}

		@Override
		public void messageReceived(String msg, Integer userID) {
			if(msg.equals("cancel")){
				InventoryMenu menu = new InventoryMenu();
				FinanceController.getInstance().getAccount(userID).setMenu(menu);
				menu.show(userID);
			}
			try{
				Account acc = FinanceController.getInstance().getAccount(userID);
				plane.setValue(plane.getValue() * 7/8);
				Flight flight = new Flight(acc, plane, Integer.parseInt(msg));
				flight.start();
			}
			catch(NumberFormatException e){}
		}

	}

}
