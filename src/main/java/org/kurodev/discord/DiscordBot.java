package org.kurodev.discord;

import kurodev.reader.IniInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kurodev.randomchat.RandomChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DiscordBot extends ListenerAdapter implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);
    private static JDA bot;
    private final IniInstance settings;
    private final RandomChatServer server;

    public DiscordBot(IniInstance settings, RandomChatServer server) {

        this.settings = settings;
        this.server = server;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("Discord bot successfully started");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        server.handleMessage(event.getMessage().getContentRaw(), new DiscordUserID(event.getAuthor().getIdLong(), bot));
    }

    @Override
    public void run() {
        Optional<String> key = settings.get("discord.apiKey");
        if (key.isPresent()) {
            logger.info("starting bot");
            bot = JDABuilder.createDefault(key.get()).build();
            bot.addEventListener(this);
        }
    }
}
