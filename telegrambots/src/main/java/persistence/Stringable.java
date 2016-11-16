package persistence;

public interface Stringable {
	
	public static final String NEXT = "X|d:)|X";
	
	public String toSaveString();
	
	public void stringToObject(String string);

}
