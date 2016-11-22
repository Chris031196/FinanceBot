package core.main;

import org.telegram.telegrambots.api.objects.Message;

public class MessageModel extends Message{
	
	private static final long serialVersionUID = 1L;
	private String[] keyboard;
	
	public MessageModel() {
	}

	public String[] getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String[] keyboard) {
		this.keyboard = keyboard;
	}
	
	

}
