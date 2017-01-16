package addons.stocks.functions;

import java.util.ArrayList;
import core.FinanceController;
import core.Stringable;

public class Company implements Stringable{
	
	private String name;
	private double value;
	private double lastChange;
	private ArrayList<Integer> shareholders;

	public Company(String name, double value, double lastChange){
		this.name = name;
		this.value = value;
		this.lastChange = lastChange;
		this.shareholders = new ArrayList<Integer>();
	}
	
	public void startCircleOfLife(){
		Company company = this;
		Thread life = new Thread(new Runnable() {

			@Override
			public void run() {

				while(FinanceController.getInstance().isRunning()){
					double change=0, min=0, max=20, random;

					if(lastChange < 0){
						min = 0;
						max = 20 + (lastChange / 2.0);
					}
					else if(lastChange > 0){
						min = 0 + (lastChange / 2.0);
						max = 20;
					}
					random = Math.random()*(max - min) + min;
					change = random-10;
					lastChange = change;
					value = value+value*(change/100);

					StockmarketController.getInstance().stockChanged(company);

					//					save();
					long waitTime = (long) (Math.random()*16200000+1800000);
//					long waitTime = (long) (Math.random()*60000);
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		life.start();
	}

	public String getName() {
		return name;
	}

	//	public void save(){
	//		Properties properties = new Properties();
	//
	//		try {
	//			properties.load(new FileInputStream(FinanceController.logFile));
	//			String values = properties.getProperty(name) != null ? properties.getProperty(name) +";" +value : "" +value;
	//			properties.put(name, values);
	//		} catch (IOException e) {}
	//		try {
	//			properties.store(new FileOutputStream(FinanceController.logFile), null);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public void addHolder(Integer userID) {
		if(!shareholders.contains(userID))
			shareholders.add(userID);
	}

	public ArrayList<Integer> getHolders() {
		return shareholders;
	}

	public double getValue() {
		return value;
	}

	public double getLastChange() {
		return lastChange;
	}
}