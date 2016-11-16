package functions.stock;

import controller.FinanceController;
import persistence.Stringable;

public class Company implements Stringable{

	private String name;
	private double value;
	private double lastChange;

	public Company(String name, double value, double lastChange){
		this.name = name;
		this.value = value;
		this.lastChange = lastChange;
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
//					long waitTime = (long) (Math.random()*10000);
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

	public double getValue() {
		return value;
	}

	public double getLastChange() {
		return lastChange;
	}

	@Override
	public String toSaveString() {
		return name +NEXT +value +NEXT +lastChange;
	}

	@Override
	public void stringToObject(String string) {
		String[] parts = string.split(NEXT);
		this.name = parts[0];
		this.value = Double.parseDouble(parts[1]);
		this.lastChange = Double.parseDouble(parts[2]);
	}
}