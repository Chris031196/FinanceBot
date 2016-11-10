package org.telegram.telegrambots;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.bots.BotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.BotSession;
import org.telegram.telegrambots.updatesreceivers.Webhook;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Bots manager
 * @date 14 of January of 2016
 */
public class TelegramBotsApi {
    private static final int SOCKET_TIMEOUT = 75 * 1000;
    private static final String webhookUrlFormat = "{0}callback/";
    private boolean useWebhook; ///< True to enable webhook usage
    private Webhook webhook; ///< Webhook instance
    private String extrenalUrl; ///< External url of the bots
    private String pathToCertificate; ///< Path to public key certificate

    /**
     *
     */
    public TelegramBotsApi() {
    }

    /**
     *
     * @param keyStore KeyStore for the server
     * @param keyStorePassword Key store password for the server
     * @param externalUrl External base url for the webhook
     * @param internalUrl Internal base url for the webhook
     */
    public TelegramBotsApi(String keyStore, String keyStorePassword, String externalUrl, String internalUrl) throws TelegramApiRequestException {
        if (externalUrl == null || externalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
        }
        if (internalUrl == null || internalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
        }

        this.useWebhook = true;
        this.extrenalUrl = fixExternalUrl(externalUrl);
        webhook = new Webhook(keyStore, keyStorePassword, internalUrl);
        webhook.startServer();
    }

    /**
     *
     * @param keyStore KeyStore for the server
     * @param keyStorePassword Key store password for the server
     * @param externalUrl External base url for the webhook
     * @param internalUrl Internal base url for the webhook
     * @param pathToCertificate Full path until .pem public certificate keys
     */
    public TelegramBotsApi(String keyStore, String keyStorePassword, String externalUrl, String internalUrl, String pathToCertificate) throws TelegramApiRequestException {
        if (externalUrl == null || externalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
        }
        if (internalUrl == null || internalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
        }
        this.useWebhook = true;
        this.extrenalUrl = fixExternalUrl(externalUrl);
        this.pathToCertificate = pathToCertificate;
        webhook = new Webhook(keyStore, keyStorePassword, internalUrl);
        webhook.startServer();
    }

    /**
     * Register a bot. The Bot Session is started immediately, and may be disconnected by calling close.
     * @param bot the bot to register
     */
    public BotSession registerBot(TelegramLongPollingBot bot) throws TelegramApiRequestException {
        setWebhook(bot.getBotToken(), null, bot.getOptions());
        return new BotSession(bot.getBotToken(), bot, bot.getOptions());
    }

    /**
     * Register a bot in the api that will receive updates using webhook method
     * @param bot Bot to register
     */
    public void registerBot(TelegramWebhookBot bot) throws TelegramApiRequestException {
        if (useWebhook) {
            webhook.registerWebhook(bot);
            setWebhook(bot.getBotToken(), bot.getBotPath(), bot.getOptions());
        }
    }

    private static String fixExternalUrl(String externalUrl) {
        if (externalUrl != null && !externalUrl.endsWith("/")) {
            externalUrl = externalUrl + "/";
        }
        return MessageFormat.format(webhookUrlFormat, externalUrl);
    }

    /**
     * Set webhook or remove it if necessary
     * @param webHookURL Webhook url or empty is removing it
     * @param botToken Bot token
     * @param publicCertificatePath Path to certificate public key
     * @param options Bot options
     * @throws TelegramApiRequestException If any error occurs setting the webhook
     */
    private static void setWebhook(String webHookURL, String botToken,
                                   String publicCertificatePath, BotOptions options) throws TelegramApiRequestException {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
            String url = Constants.BASEURL + botToken + "/" + SetWebhook.PATH;

            RequestConfig.Builder configBuilder = RequestConfig.copy(RequestConfig.custom().build())
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setConnectTimeout(SOCKET_TIMEOUT)
                    .setConnectionRequestTimeout(SOCKET_TIMEOUT);

            if (options.hasProxy()) {
                configBuilder.setProxy(new HttpHost(options.getProxyHost(), options.getProxyPort()));
            }

            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(configBuilder.build());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody(SetWebhook.URL_FIELD, webHookURL);
            if (publicCertificatePath != null) {
                File certificate = new File(publicCertificatePath);
                if (certificate.exists()) {
                    builder.addBinaryBody(SetWebhook.CERTIFICATE_FIELD, certificate, ContentType.TEXT_PLAIN, certificate.getName());
                }
            }
            HttpEntity multipart = builder.build();
            httppost.setEntity(multipart);
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                HttpEntity ht = response.getEntity();
                BufferedHttpEntity buf = new BufferedHttpEntity(ht);
                String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(responseContent);
                if (!jsonObject.getBoolean(Constants.RESPONSEFIELDOK)) {
                    throw new TelegramApiRequestException(webHookURL == null ? "Error removing old webhook" : "Error setting webhook", jsonObject);
                }
            }
        } catch (JSONException e) {
            throw new TelegramApiRequestException("Error deserializing setWebhook method response", e);
        } catch (IOException e) {
            throw new TelegramApiRequestException("Error executing setWebook method", e);
        }
    }

    /**
     * Set the webhook or remove it if necessary
     * @param botToken Bot token
     * @param urlPath Url for the webhook or null to remove it
     * @param botOptions Bot Options
     */
    private void setWebhook(String botToken, String urlPath, BotOptions botOptions) throws TelegramApiRequestException {
        if (botToken == null) {
            throw new TelegramApiRequestException("Parameter botToken can not be null");
        }
        String completeExternalUrl = urlPath == null ? "" : extrenalUrl + urlPath;
        setWebhook(completeExternalUrl, botToken, pathToCertificate, botOptions);
    }
}
