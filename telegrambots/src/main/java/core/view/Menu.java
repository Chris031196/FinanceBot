package core.view;

public abstract class Menu implements MessageListener{
	
	public static final String[] BACK = {"🔙", "cancel"};

	public abstract void show(Integer userID);

}
