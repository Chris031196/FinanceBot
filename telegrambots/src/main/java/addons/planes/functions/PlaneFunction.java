package addons.planes.functions;

import java.time.LocalDateTime;

import addons.Function;
import addons.TYPE;
import addons.planes.items.Plane;
import core.accounts.Account;
import core.accounts.AccountManager;
import core.main.IOController;
import core.view.Menu;
import core.view.inventory.ItemListMenu;

public class PlaneFunction implements Function {

	Plane plane;

	public PlaneFunction(Plane plane) {
		this.plane = plane;
	}

	@Override
	public void use(Integer userID) {
		FlyMenu menu = new FlyMenu(plane);
		AccountManager.getInstance().getAccount(userID).setListener(menu);
		menu.show(userID);
	}

	private class FlyMenu extends Menu {

		Plane plane;

		public FlyMenu(Plane plane) {
			this.plane = plane;
		}

		@Override
		public void show(Integer userID) {
			int hour = LocalDateTime.now().getHour();
			//TODO falsche zeit
			if(hour <= 19 && hour >= 8){
				IOController.sendMessage("Wie viele Kilometer möchten Sie ausschreiben?", new String[]{"100km FAI","100","300km FAI","300","500km FAI","500","700km FAI","700","1000km FAI","1000","🔙","cancel"}, userID.toString(), true);
			}
			else {
				IOController.sendMessage("Sie sollten zwischen 09:00 Uhr und 13:00 Uhr starten. Sonst haben Sie keine Chance!", BACK, userID.toString(), true);
			}
		}

		@Override
		public void messageReceived(String msg, Integer userID) {
			if(msg.equals("cancel")){
				ItemListMenu menu = new ItemListMenu(userID, TYPE.Plane, "Ihre Flugzeuge:");
				AccountManager.getInstance().getAccount(userID).setListener(menu);
				menu.show(userID);
			}
			try {
				Account acc = AccountManager.getInstance().getAccount(userID);
				plane.setValue(plane.getValue() * 9.0/10.0);
				Flight flight = new Flight(userID, plane, Integer.parseInt(msg));
				flight.start();
			}
			catch(NumberFormatException e){}
		}
	}
}
