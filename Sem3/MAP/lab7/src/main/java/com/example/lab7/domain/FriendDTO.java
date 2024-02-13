package com.example.lab7.domain;

import java.time.LocalDateTime;

public class FriendDTO {
    private String firstName;
    private String lastName;
    private LocalDateTime friendFrom;

    public FriendDTO(String firstName, String lastName, LocalDateTime friendFrom){
        this.firstName=firstName;
        this.lastName=lastName;
        this.friendFrom=friendFrom;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getFriendFrom() {
        return friendFrom;
    }

    public void setFriendFrom(LocalDateTime friendFrom) {
        this.friendFrom = friendFrom;
    }

    @Override
    public String toString(){return ""+firstName+" | "+lastName+" | "+friendFrom;}
}
