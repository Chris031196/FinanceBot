package view;

public abstract class Menu implements MessageListener{
	
	protected static final String[] BACK = {"🔙", "cancel"};

	public abstract void show(Integer userID);

}
