package sessionfinding;

import org.junit.Test;
import org.kurodev.randomchat.RandomChatServer;
import org.kurodev.randomchat.Session;
import org.kurodev.randomchat.sessionsearch.UserId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFindingPeople {
    private final RandomChatServer server = new RandomChatServer();

    @Test
    public void testPeopleFindEachOther() throws ExecutionException, InterruptedException {
        long idA = 1;
        long idB = 2;
        CompletableFuture<Optional<Session>> searchA = server.getChatFinder().findPartner(new TestUserId(idA));
        CompletableFuture<Optional<Session>> searchB = server.getChatFinder().findPartner(new TestUserId(idB));

        Optional<Session> resultA = searchA.get();
        Optional<Session> resultB = searchB.get();

        assertTrue(resultA.isPresent());
        assertTrue(resultB.isPresent());
        assertEquals(resultA.get(), resultB.get());
    }

}
