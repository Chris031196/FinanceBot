package functions.plane;

import main.IOController;
import persistence.accounts.Account;
import persistence.market.items.Certificate;
import persistence.market.items.Plane;
import view.Menu;
import view.MessageListener;

public class Flight {

	private Account account;
	private Plane plane;
	private int chance;
	private int leftParts;
	private int distance;
	private boolean crashedLast;

	public Flight(Account account, Plane plane, int distance){
		this.account = account;
		this.plane = plane;
		this.distance = distance;
		this.leftParts = distance / 100;
	}

	public void start() {
		double timeInHours = ((double)(distance/100.0))*0.75;
		int hours = (int) timeInHours;
		int minutes =(int) (60.0* (timeInHours-hours));

		IOController.sendMessage("Flug gestartet! Der Flug dauert mind. " +hours +"h und " +minutes +"min.", new String[]{"🔙","cancel"}, account.getID().toString(), false);
		account.getInventory().getItems().remove(plane);
		flyNextPart();
	}

	public void abort() {
		account.getInventory().addItem(plane);
	}

	public void flyNextPart(){
		if(leftParts <= 0){
			IOController.sendMessage("Sie sind rumgekommen! Sie bekommen " +distance/10 +"+ auf Ihr Punktekonto!", null, account.getID().toString(), false);
			account.getInventory().addPop(distance/10);
			account.getInventory().addItem(new Certificate(this));
			account.getInventory().addItem(plane);
			return;
		}
		leftParts--;

		Flight flight = this;
		Thread fly = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					//Thread.sleep(2700000);
					//TODO
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double chance = 100;
				chance -= (distance / 10);
				chance += plane.getChance();
				if(chance >= 100){
					chance = 95;
				}
				double random = Math.random() * 100;
				if(random > chance){
					if(!crashedLast){
						crashedLast = true;
						DecisionMenu menu = new DecisionMenu(flight, account.getCurMenu());
						account.setListener(menu);
						menu.show(account.getID());
					}
					else {
						plane.setValue(plane.getValue()/2);
						account.getInventory().addItem(plane);
						IOController.sendMessage("Ihre " +plane.getName() +" ist außengelandet! Sie verliert die Hälfte ihres Wertes!", null, account.getID().toString(), true);
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
		return account;
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

	private class DecisionMenu extends Menu {

		Flight flight;
		MessageListener last;

		public DecisionMenu(Flight flight, MessageListener last){
			this.flight = flight;
			this.last = last;
		}

		@Override
		public void show(Integer userID) {
			IOController.sendMessage("Ihre " +flight.getPlane().getName() +" hat eine kritische Höhe erreicht! Was möchten Sie tun?", new String[]{"Weiterfliegen!","fly","Umkehren","return"}, userID.toString(), false);
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
			case "return":
				flight.abort();
				flight.getAccount().setListener(last);
				if(last instanceof Menu){
					((Menu) last).show(userID);
				}
			}

		}
	}

}
