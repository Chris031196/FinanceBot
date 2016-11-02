package main;



import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
public class Main {

	public static void main(String[] args) {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new FinanceSystemBot());
		} catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

}
