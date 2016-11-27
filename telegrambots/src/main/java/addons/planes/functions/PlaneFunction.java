package addons.planes.functions;

import java.time.LocalDateTime;

import addons.Function;
import addons.TYPE;
import addons.planes.items.Plane;
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
			if(plane.isFlying()){
				IOController.sendMessage("Dieses Flugzeug ist bereits auf einem Überlandflug!", BACK, userID.toString(), false);
				return;
			}
			int hour = LocalDateTime.now().getHour();
			//TODO
			if(hour <= 20 && hour >= 8){
				IOController.sendMessage("Wie viele Kilometer möchten Sie ausschreiben?", new String[]{"100km FAI","100","300km FAI","300","500km FAI","500","700km FAI","700","1000km FAI","1000","🔙","cancel"}, userID.toString(), false);
			}
			else {
				IOController.sendMessage("Sie sollten zwischen 09:00 Uhr und 13:00 Uhr starten. Sonst haben Sie keine Chance!", BACK, userID.toString(), false);
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
				Flight flight = new Flight(userID, plane, Integer.parseInt(msg));
				flight.start();
			}
			catch(NumberFormatException e){}
		}
	}
}
