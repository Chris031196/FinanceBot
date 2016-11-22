package core.main;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import addons.stocks.functions.StockmarketController;
import core.FinanceController;
import core.accounts.AccountManager;
import core.market.MarketManager;

public class FinanceSystemBot extends TelegramLongPollingBot{

	public FinanceSystemBot() {
		initialize();
	}

	@Override
	public void onUpdateReceived(Update update) {
		try{
			if(update.hasCallbackQuery() && update.getCallbackQuery() != null){
				IOController.callbackReceived(update.getCallbackQuery());
			}
			else if(update.hasMessage() && update.getMessage() != null){
				if(update.getMessage().getText() != null && update.getMessage().getText().startsWith("/all") && update.getMessage().getFrom().getId().equals(205364667)){
					FinanceController.getInstance().messageToAllUsers(update.getMessage().getText().replace("/all", ""));
				}
				IOController.messageReceived(update.getMessage());
			}
		} catch (Exception e){
			e.printStackTrace();
//			IOController.sendMessage(e.getMessage(), null, (new Integer(205364667)).toString(), true);
		}
	}

	@Override
	public String getBotUsername() {
		return "Uwe";
	}

	public void answerCallback(CallbackQuery callback){
		AnswerCallbackQuery answer = new AnswerCallbackQuery();
		answer.setCallbackQueryId(callback.getId());
		try {
			answerCallbackQuery(answer);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotToken() {
		return "274593850:AAG3Wc3vKK6X_dtC9nI1PRhwkJabPgeypnw"; // DEBUG
//		return "264511117:AAE1MlMYUe0UliHd_O0R_D8UbRtpm_5VMw8"; // REAL
	}

	public void initialize() {
		IOController.init(this);
		FinanceController.getInstance().init();
		MarketManager.getInstance().init();
		AccountManager.getInstance().init();
		StockmarketController.getInstance().init();
	}
}