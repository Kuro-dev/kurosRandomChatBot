package sessionfinding;

import org.kurodev.randomchat.sessionsearch.UserId;

public class TestUserId extends UserId {
    public TestUserId(long id) {
        super(id);
    }

    @Override
    public void sendMessage(String msg) {
        System.out.println("sending " + msg);
    }
}
