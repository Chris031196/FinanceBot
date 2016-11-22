package addons;

public enum TYPE {
	Plane ("Flugzeug", "Flugeuge", true), 
	Upgrade ("Upgrade", "Upgrades", true), 
	Stock ("Aktien", "Aktien", true), 
	Certificate ("Urkunde", "Urkunden", false);
	
	private final String singular;
	private final String plural;
	private final boolean buyable;
	
	TYPE(String singular, String plural, boolean buyable){
		this.singular = singular;
		this.plural = plural;
		this.buyable = buyable;
	}
	
	public boolean hasMarket() {
		return buyable;
	}
	
	public String getSingular(){
		return singular;
	}
	
	public String getPlural(){
		return plural;
	}
}
