package org.kurodev.randomchat;

import org.kurodev.randomchat.sessionsearch.SessionFinder;
import org.kurodev.randomchat.sessionsearch.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RandomChatServer {
    private static final String DIVIDER_LINE = ". . . ".repeat(15);
    private static final Logger logger = LoggerFactory.getLogger(RandomChatServer.class);
    private final List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    private final SessionFinder finder = new SessionFinder(sessions);

    public SessionFinder getChatFinder() {
        return finder;
    }

    public void handleMessage(String msg, UserId userId) {

        Optional<Session> userSession = sessions.stream().filter(session -> session.containsId(userId)).findFirst();
        if ("!find".equals(msg)) {
            userId.sendMessage("Looking for others...");
            finder.findPartner(userId).whenComplete((session, throwable) -> {
                if (session.isPresent()) {
                    String connectionMsg = "Found a partner. use `!find` or `!close` to disconnect. Enjoy!\n" +
                            DIVIDER_LINE;
                    userId.sendMessage(connectionMsg);
                } else {
                    userId.sendMessage("Could not find user");
                }
            });
            userSession.ifPresent(session -> closeSession(session, userId));
            return;
        }

        if ("!close".equals(msg)) {
            userSession.ifPresent((session) -> {
                closeSession(session, userId);
            });
            return;
        }
        //user has a running session
        userSession.ifPresent(session -> session.getPartner(userId).sendMessage(msg));
    }

    private void closeSession(Session userSession, UserId userId) {
        if (sessions.remove(userSession)) {
            logger.info("Removing session --- SUCCESS");
        } else {
            logger.info("Removing session --- FAILURE");
        }
        userId.sendMessage(DIVIDER_LINE + "\nSuccessfully Disconnected");
        userSession.getPartner(userId).sendMessage(DIVIDER_LINE + "\nPartner disconnected");
    }
}
