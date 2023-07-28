package org.kurodev.discord;

import net.dv8tion.jda.api.JDA;
import org.kurodev.randomchat.sessionsearch.UserId;

public class DiscordUserID extends UserId {
    private final JDA jda;

    public DiscordUserID(long id, JDA jda) {
        super(id);
        this.jda = jda;
    }

    @Override
    public void sendMessage(String msg) {
        jda.retrieveUserById(this.getId()).flatMap(user ->
                user.openPrivateChannel().flatMap(channel -> channel.sendMessage(msg))).queue();
    }
}
