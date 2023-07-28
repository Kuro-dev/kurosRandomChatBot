package org.kurodev.randomchat.sessionsearch;

import org.kurodev.randomchat.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SessionFinder {
    /**
     * lfp = looking for partner
     */
    private final List<UserId> lfp = Collections.synchronizedList(new ArrayList<>());
    private final List<Session> sessions;

    public SessionFinder(List<Session> sessions) {
        this.sessions = sessions;
    }


    public CompletableFuture<Optional<Session>> findPartner(UserId myID) {
        CompletableFuture<Optional<Session>> search = new SessionSearchTask(myID, lfp, sessions);
        search.whenComplete((session, throwable) -> session.ifPresent(sessions::add));
        return search;
    }
}
