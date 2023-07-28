package org.kurodev.telegram;

import kurodev.reader.IniInstance;
import org.kurodev.randomchat.RandomChatServer;
import org.kurodev.randomchat.sessionsearch.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramChatBot extends TelegramLongPollingBot implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TelegramChatBot.class);
    private final RandomChatServer server;

    public TelegramChatBot(IniInstance settings, RandomChatServer server) {
        super(settings.get("telegram.apiKey").orElse(null));
        this.server = server;
    }

    @Override
    public void run() {
        logger.info("Starting Telegram bot ({})", this.getBotUsername());
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error("Could not start Telegram bot", e);
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        UserId id = new TelegramuserId(update.getMessage(), this);
        server.handleMessage(update.getMessage().getText(), id);
    }

    @Override
    public String getBotUsername() {
        return "RandomChatBot";
    }
}
