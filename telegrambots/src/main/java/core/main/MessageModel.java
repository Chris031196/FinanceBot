package core.main;

import org.telegram.telegrambots.api.objects.Message;

public class MessageModel{
	
	private String[] keyboard = null;
	private String chatID;
	private String text;
	private Integer messageID;
	
	public MessageModel(Message m) {
		this.chatID = m.getChatId().toString();
		this.text = m.getText();
		this.messageID = m.getMessageId();
	}

	public String[] getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String[] keyboard) {
		this.keyboard = keyboard;
	}

	public String getChatID() {
		return chatID;
	}

	public String getText() {
		return text;
	}

	public Integer getMessageID() {
		return messageID;
	}
	
}
