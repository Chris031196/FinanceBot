package core.main;

import java.io.File;
import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import core.accounts.AccountManager;

public class IOController {

	private static FinanceSystemBot bot;
	private static boolean newMessage = true;

	private IOController(){}

	public static void init(FinanceSystemBot bot) {
		IOController.bot = bot;
	}

	public static void messageReceived(Message message){
		newMessage = true;
		String msg = message.getText();
		User user = message.getFrom();
		if(msg != null && user != null){
			AccountManager.getInstance().messageReceived(msg, user);
		}
	}

	public static void callbackReceived(CallbackQuery callback){
		String query = callback.getData();
		User user = callback.getFrom();
		if(query != null && user != null){
			AccountManager.getInstance().messageReceived(query, user);
			bot.answerCallback(callback);
		}
	}

	public static void logout(Integer userID){
		AccountManager.getInstance().logout(userID);
		IOController.sendMessage("Erfolgreich ausgeloggt!", null, userID.toString(), false);
	}

	public static void sendData(String path, String chatID){
		SendDocument doc = new SendDocument();
		doc.setNewDocument(new File(path));
		doc.setChatId(chatID);
		try {
			bot.sendDocument(doc);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessage(String message, String[] keyboard, String chatID, boolean note){
		MessageModel lastSend = AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg;
		if(!newMessage && lastSend != null){
			EditMessageText edit = new EditMessageText();
			edit.setChatId(chatID);
			edit.enableMarkdown(true);
			edit.setMessageId(lastSend.getMessageID());
			if(message != null) {
				edit.setText(message);
			}
			if(keyboard != null) {
				edit.setReplyMarkup(assembleKeyboard(keyboard));
			}
			try {
				if(!note) {
					MessageModel model = new MessageModel(bot.editMessageText(edit));
					model.setKeyboard(keyboard);
					AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg = model;
				} else {
					bot.editMessageText(edit).getMessageId();
					SendMessage msg = new SendMessage();
					msg.setChatId(lastSend.getChatID());
					msg.enableMarkdown(true);
					if(lastSend.getText() != null){
						msg.setText(lastSend.getText());
					}
					if(lastSend.getKeyboard() != null){
						msg.setReplyMarkup(assembleKeyboard(lastSend.getKeyboard()));
					}
					try {
						MessageModel model = new MessageModel(bot.sendMessage(msg));
						model.setKeyboard(keyboard);
						AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg = model;
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		else {
			newMessage = false;
			deleteLastMessage(chatID);
			SendMessage msg = new SendMessage();
			msg.setChatId(chatID);
			msg.enableMarkdown(true);
			if(message != null){
				msg.setText(message);
			}
			if(keyboard != null){
				msg.setReplyMarkup(assembleKeyboard(keyboard));
			}
			try {
				if(!note) {
					MessageModel model = new MessageModel(bot.sendMessage(msg));
					model.setKeyboard(keyboard);
					AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg = model;
				} else {
					bot.sendMessage(msg).getMessageId();
				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteLastMessage(String chatID) {
		if(AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg != null) {
			EditMessageText edit = new EditMessageText();
			edit.setChatId(chatID);
			edit.setMessageId(AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg.getMessageID());
			edit.setText("-");
			edit.setReplyMarkup(null);

			try {
				bot.editMessageText(edit);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			AccountManager.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsg = null;
		}
	}

	private static InlineKeyboardMarkup assembleKeyboard(String[] options){
		InlineKeyboardMarkup board = new InlineKeyboardMarkup();
		for(int i=0;i<options.length;i+=2){
			ArrayList<InlineKeyboardButton> buttons = new ArrayList<InlineKeyboardButton>();
			InlineKeyboardButton button = new InlineKeyboardButton();
			button.setText(options[i]);
			button.setCallbackData(options[i+1]);
			buttons.add(button);
			board.getKeyboard().add(buttons);
		}
		return board;
	}

}
