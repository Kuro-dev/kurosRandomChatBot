package org.kurodev.randomchat.sessionsearch;

import org.kurodev.randomchat.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionSearchTask extends CompletableFuture<Optional<Session>> implements Runnable {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10, task -> {
        Thread worker = new Thread(task);
        worker.setName("PartnerFinder-Thread");
        worker.setDaemon(true);
        return worker;
    });
    private static final int MAX_ATTEMPTS = 30; //should result in max. 15 seconds of search time
    /**
     * Delay between attempts in Milliseconds
     */
    private static final int RETRY_DELAY = 500;
    private static final Logger logger = LoggerFactory.getLogger(SessionSearchTask.class);
    private final UserId myID;
    @org.jetbrains.annotations.NotNull
    private final List<UserId> lfp;
    private final List<Session> sessions;

    private int currentAttempt = 0;

    /**
     * @param myID     The id of the client that's looking for a partner
     * @param lfp      the list of Ids that are looking for a partner
     * @param sessions the list of active sessions
     */
    public SessionSearchTask(UserId myID, List<UserId> lfp, List<Session> sessions) {
        this.myID = myID;
        this.lfp = lfp;
        this.sessions = sessions;
        logger.info("{} is currently looking for a partner", myID);

        if (!lfp.isEmpty()) {
            UserId partnerID = lfp.remove(0);
            logger.info("{} found partner {}", myID, partnerID);
            //no need to search, I found someone
            this.complete(Optional.of(new Session(myID, partnerID)));
        } else {
            //register yourself to the lsf
            lfp.add(myID);
            scheduler.schedule(this, 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void run() {
        //increment attempts so it doesn't take too long
        currentAttempt++;
        if (currentAttempt >= MAX_ATTEMPTS) {
            logger.info("Failed to find partner for user {}", myID);
            lfp.remove(myID);
            //found no one
            this.complete(Optional.empty());
        }
        Optional<Session> matchedSession = sessions.stream().filter(session -> session.containsId(myID)).findFirst();
        if (matchedSession.isPresent()) {
            logger.info("{} found partner {}", myID, matchedSession.get().getPartner(myID));
            //someone found me
            this.complete(matchedSession);
        }
        //could not find one this iteration, so we reschedule this task.
        if (!this.isDone()) {
            scheduler.schedule(this, RETRY_DELAY, TimeUnit.MILLISECONDS);
        }
    }
}
