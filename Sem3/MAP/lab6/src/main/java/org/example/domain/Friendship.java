package org.example.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Friendship extends Entity<Tuple<Long,Long>> {
    private User user1;
    private User user2;
    LocalDateTime friendsFrom;

    public Friendship(User user1, User user2, LocalDateTime friendsFrom) {
        this.user1 = user1;
        this.user2 = user2;
        Long user1Id = user1.getId();
        Long user2Id = user2.getId();
        Tuple<Long, Long> id = new Tuple<>(user1Id, user2Id);
        setId(id);
        this.friendsFrom = friendsFrom;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
}