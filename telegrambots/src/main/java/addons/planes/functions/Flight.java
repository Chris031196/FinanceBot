package addons.planes.functions;

import addons.planes.items.Certificate;
import addons.planes.items.Plane;
import core.accounts.Account;
import core.accounts.AccountManager;
import core.main.IOController;
import core.view.Menu;
import core.view.MessageListener;

public class Flight {

	private Integer userID;
	private Plane plane;
	private int chance;
	private int leftParts;
	private int distance;
	private boolean crashedLast;

	public Flight(Integer userID, Plane plane, int distance){
		this.userID = userID;
		this.plane = plane;
		this.distance = distance;
		this.leftParts = distance / 100;
	}

	public void start() {
		double timeInHours = ((double)(distance/100.0))*0.75;
		int hours = (int) timeInHours;
		int minutes = (int) (60.0* (timeInHours-hours));

		IOController.sendMessage("Flug gestartet! Der Flug dauert mind. " +hours +"h und " +minutes +"min.", new String[]{"🔙","cancel"}, userID.toString(), false);
		plane.setFlying(true);
		
		flyNextPart();
	}

	public void abort() {
		plane.setFlying(false);
	}

	public void flyNextPart(){
		
		if(leftParts <= 0){
			Account account = AccountManager.getInstance().getAccount(userID);
			account.getInventory().addPop(distance/10);
			plane.setFlying(false);
			SaveMenu menu = new SaveMenu(this, account.getListener());
			account.setListener(menu);
			menu.show(userID);
			return;
		}
		leftParts--;

		Flight flight = this;
		Thread fly = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(2700000);
//					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Account account = AccountManager.getInstance().getAccount(userID);
				double chance = plane.getChance();
				if(chance >= 100){
					chance = 95;
				}
				double random = Math.random() * 100;
				System.out.println(random+ " "+chance);
				if(random > chance){
					if(!crashedLast){
						crashedLast = true;
						DecisionMenu menu = new DecisionMenu(flight, account.getCurMenu());
						account.setListener(menu);
						menu.show(account.getID());
					}
					else {
						plane.setValue(plane.getValue()*(9.0/10.0));
						plane.setFlying(false);
						IOController.sendMessage("Ihre " +plane.getName() +" ist außengelandet! Sie verliert dadurch an Wert!", null, account.getID().toString(), true);
					}
				}
				else {
					flyNextPart();
					crashedLast = false;
				}
			}
		});
		fly.start();
	}

	public boolean isCrashedLast() {
		return crashedLast;
	}

	public void setCrashedLast(boolean crashedLast) {
		this.crashedLast = crashedLast;
	}

	public Account getAccount() {
		return AccountManager.getInstance().getAccount(userID);
	}

	public Plane getPlane() {
		return plane;
	}

	public int getChance() {
		return chance;
	}

	public int getLeftParts() {
		return leftParts;
	}

	public int getDistance() {
		return distance;
	}
	
	private class SaveMenu extends Menu{
		
		Flight flight;
		MessageListener last;
		
		public SaveMenu(Flight flight, MessageListener last){
			this.flight = flight;
			this.last = last;
		}

		@Override
		public void messageReceived(String msg, Integer userID) {
			Account account = AccountManager.getInstance().getAccount(userID);
			switch(msg){
			case "save":
				account.getInventory().addItem(new Certificate(flight));
				account.save();
				account.setListener(last);
				if(last instanceof Menu){
					((Menu) last).show(userID);
				}
				break;
			case "not":
				account.save();
				account.setListener(last);
				if(last instanceof Menu){
					((Menu) last).show(userID);
				}
				break;
			}
		}

		@Override
		public void show(Integer userID) {
			IOController.sendMessage("Sie sind rumgekommen! Sie bekommen " +distance/10 +"+ auf Ihr Punktekonto! Urkunde des Fluges speichern?", new String[]{"Urkunde speichern!","save","Nicht speichern!","not"}, userID.toString(), false);
		}
		
	}

	private class DecisionMenu extends Menu {

		Flight flight;
		MessageListener last;

		public DecisionMenu(Flight flight, MessageListener last){
			this.flight = flight;
			this.last = last;
		}

		@Override
		public void show(Integer userID) {
			IOController.sendMessage("Ihre " +flight.getPlane().getName() +" ("+flight.getPlane().getValue() +"$) hat eine kritische Höhe erreicht! Was möchten Sie tun?", new String[]{"Weiterfliegen!","fly","Umkehren!","return"}, userID.toString(), false);
		}

		@Override
		public void messageReceived(String msg, Integer userID) {
			switch(msg) {
			case "fly":
				flight.flyNextPart();
				flight.getAccount().setListener(last);
				if(last instanceof Menu){
					((Menu) last).show(userID);
				}
				break;
			case "return":
				flight.abort();
				flight.getAccount().setListener(last);
				if(last instanceof Menu){
					((Menu) last).show(userID);
				}
				break;
			}

		}
	}

}
