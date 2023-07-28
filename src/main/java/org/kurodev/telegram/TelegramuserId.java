package org.kurodev.telegram;

import org.kurodev.randomchat.sessionsearch.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramuserId extends UserId {
    private static final Logger logger = LoggerFactory.getLogger(TelegramuserId.class);

    private final long chatId;
    private final TelegramChatBot bot;

    public TelegramuserId(Message msg, TelegramChatBot bot) {
        super(msg.getFrom().getId());
        this.chatId = msg.getChatId();
        this.bot = bot;
    }

    @Override
    public void sendMessage(String msg) {
        SendMessage respose = new SendMessage();
        respose.setChatId(chatId);
        respose.setText(msg);
        try {
            bot.execute(respose);
        } catch (TelegramApiException e) {
            logger.error("Could not send message", e);
        }
    }
}
