package org.kurodev;

import kurodev.reader.IniInstance;
import org.kurodev.discord.DiscordBot;
import org.kurodev.randomchat.RandomChatServer;
import org.kurodev.telegram.TelegramChatBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final IniInstance settings;
    private static final RandomChatServer SERVER = new RandomChatServer();
    private static DiscordBot discordBot;
    private static TelegramChatBot telegramBot;

    static {
        try {
            settings = IniInstance.createNew(Files.newInputStream(Path.of("./settings.ini")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        discordBot = new DiscordBot(settings, SERVER);
        telegramBot = new TelegramChatBot(settings, SERVER);
        new Thread(discordBot, "Discord Bot").start();
        new Thread(telegramBot, "Telegram Bot").start();
    }
}
