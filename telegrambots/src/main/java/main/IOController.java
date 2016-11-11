package main;

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

import htmlTest.Test;
import menus.NoMenu;
import util.Account;
import util.Menu;

public class IOController {

	private static FinanceSystemBot bot;

	private IOController(){}

	public static void init(FinanceSystemBot bot) {
		IOController.bot = bot;
	}

	public static void messageReceived(Message message){
		String msg = message.getText();
		User user = message.getFrom();
		if(msg != null && user != null){
			if(msg != null && msg.equals("/logout")){
				logout(user.getId());
				return;
			}
			if(msg != null && msg.equals("/html")){
				sendMessage(Test.getHTML(), null, user.getId().toString(), false);
				return;
			}
			FinanceController controller = FinanceController.getInstance();
			controller.lookForPlus(msg, user.getId());
			Account account = controller.getAccount(user.getId()) != null ? controller.getAccount(user.getId()) : controller.addAccount(user);
			Menu menu = account.getCurMenu();
			menu.messageReceived(msg, user.getId());
		}
	}
	


	public static void callbackReceived(CallbackQuery callback){
		String query = callback.getData();
		User user = callback.getFrom();
		if(query != null && user != null){
			if(query.equals("logout")){
				logout(user.getId());
				bot.answerCallback(callback);
				return;
			}
			FinanceController controller = FinanceController.getInstance();
			Account account = controller.getAccount(user.getId()) != null ? controller.getAccount(user.getId()) : controller.addAccount(user);
			Menu menu = account.getCurMenu();
			menu.messageReceived(query, user.getId());
			bot.answerCallback(callback);
		}
	}

	public static void logout(Integer userID){
		FinanceController controller = FinanceController.getInstance();
		Account account = controller.getAccount(userID);
		account.save();
		if(account != null){
			NoMenu menu = new NoMenu();
			account.setMenu(menu);
			IOController.sendMessage("Erfolgreich ausgeloggt!", null, userID.toString(), false);
		}
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

	public static void sendMessage(String message, String[] keyboard, String chatID, boolean newMessage){
		if(!FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs.isEmpty() && !newMessage){
			EditMessageText edit = new EditMessageText();
			edit.setChatId(chatID);
			edit.enableMarkdown(true);
			edit.setMessageId(FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs.get(FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs.size()-1));
			if(message != null){
				edit.setText(message);
			}
			if(keyboard != null){
				edit.setReplyMarkup(assembleKeyboard(keyboard));
			}
			deleteLastMessages(chatID);
			try {
				FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs.add(bot.editMessageText(edit).getMessageId());
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		else {
			SendMessage msg = new SendMessage();
			msg.setChatId(chatID);
			msg.enableMarkdown(true);
			msg.enableHtml(true); // TODO funzt das bei jeder message?
			if(message != null){
				msg.setText(message);
			}
			if(keyboard != null){
				msg.setReplyMarkup(assembleKeyboard(keyboard));
			}
			try {
				FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs.add(bot.sendMessage(msg).getMessageId());
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteLastMessages(String chatID) {
		for(Integer m: FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs){
			EditMessageText edit = new EditMessageText();
			edit.setChatId(chatID);
			edit.setMessageId(m);
			edit.setText("-");
			edit.setReplyMarkup(null);

			try {
				bot.editMessageText(edit);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		FinanceController.getInstance().getAccount(Integer.parseInt(chatID)).lastSentMsgs = new ArrayList<Integer>();
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
