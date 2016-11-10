package org.telegram.telegrambots.api.methods;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.json.JSONObject;
import org.telegram.telegrambots.Constants;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;

import java.io.IOException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Use this method to send answers to callback queries sent from inline keyboards. The answer
 * will be displayed to the user as a notification at the top of the chat screen or as an alert. On
 * success, True is returned.
 *
 * @note Alternatively, the user can be redirected to the specified URL. For this option to work,
 * you must enable /setcustomurls for your bot via BotFather and accept the terms.
 *
 * @date 10 of April of 2016
 */
public class AnswerCallbackQuery extends BotApiMethod<Boolean> {
    public static final String PATH = "answercallbackquery";

    private static final String CALLBACKQUERYID_FIELD = "callback_query_id";
    private static final String TEXT_FIELD = "text";
    private static final String SHOWALERT_FIELD = "show_alert";
    private static final String URL_FIELD = "url";

    @JsonProperty(CALLBACKQUERYID_FIELD)
    private String callbackQueryId; ///< Unique identifier for the query to be answered
    @JsonProperty(TEXT_FIELD)
    private String text; ///< Optional	Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
    @JsonProperty(SHOWALERT_FIELD)
    private Boolean showAlert; ///< Optional. If true, an alert will be shown by the client instead of a notificaiton at the top of the chat screen. Defaults to false.
    @JsonProperty(URL_FIELD)
    /**
     * Optional. URL that will be opened by the user's client.
     * If you have created a Game and accepted the conditions via @Botfather,
     * specify the URL that opens your game. Otherwise you may use links
     * InlineQueryResultGamelike telegram.me/your_bot?start=XXXX that open your bot with a parameter.
     */
    private String url;

    public AnswerCallbackQuery() {
        super();
    }

    public String getCallbackQueryId() {
        return this.callbackQueryId;
    }

    public void setCallbackQueryId(String callbackQueryId) {
        this.callbackQueryId = callbackQueryId;
    }

    public String getText() {
        return this.text;
    }

    public AnswerCallbackQuery setText(String text) {
        this.text = text;
        return this;
    }

    public Boolean getShowAlert() {
        return this.showAlert;
    }

    public AnswerCallbackQuery setShowAlert(Boolean showAlert) {
        this.showAlert = showAlert;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AnswerCallbackQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CALLBACKQUERYID_FIELD, callbackQueryId);
        if (text != null) {
            jsonObject.put(TEXT_FIELD, text);
        }
        if (showAlert != null) {
            jsonObject.put(SHOWALERT_FIELD, showAlert);
        }
        if (url != null) {
            jsonObject.put(URL_FIELD, url);
        }
        return jsonObject;
    }

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    public Boolean deserializeResponse(JSONObject answer) {
        if (answer.getBoolean(Constants.RESPONSEFIELDOK)) {
            return answer.getBoolean(Constants.RESPONSEFIELDRESULT);
        }
        return null;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (callbackQueryId == null) {
            throw new TelegramApiValidationException("CallbackQueryId can't be null", this);
        }
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(METHOD_FIELD, PATH);
        gen.writeStringField(CALLBACKQUERYID_FIELD, callbackQueryId);
        if (text != null) {
            gen.writeStringField(TEXT_FIELD, text);
        }
        if (showAlert != null) {
            gen.writeBooleanField(SHOWALERT_FIELD, showAlert);
        }
        if (url != null) {
            gen.writeStringField(URL_FIELD, url);
        }
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(gen, serializers);
    }

    @Override
    public String toString() {
        return "AnswerCallbackQuery{" +
                "callbackQueryId='" + callbackQueryId + '\'' +
                ", text='" + text + '\'' +
                ", showAlert=" + showAlert +
                ", url='" + url + '\'' +
                '}';
    }
}
