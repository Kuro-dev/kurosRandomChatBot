package org.kurodev.randomchat;

import org.kurodev.randomchat.sessionsearch.UserId;

import java.util.Objects;

public class Session {
    private final UserId personA;
    private final UserId personB;

    public Session(UserId personA, UserId personB) {
        this.personA = personA;
        this.personB = personB;
    }


    @Override
    public boolean equals(Object o) {
        System.out.println("Equals is called!");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return this.containsId(session.getPersonA()) && this.containsId(session.personB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personA, personB);
    }

    public UserId getPersonA() {
        return personA;
    }

    public UserId getPersonB() {
        return personB;
    }

    public boolean containsId(UserId id) {
        return Objects.equals(id, personA) || Objects.equals(id, personB);
    }

    public UserId getPartner(UserId id) {
        if (Objects.equals(id, personA)) return personB;
        if (Objects.equals(id, personB)) return personA;
        return null;
    }
}
