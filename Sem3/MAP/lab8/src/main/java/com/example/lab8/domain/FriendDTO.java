package com.example.lab8.domain;

import java.time.LocalDateTime;

public class FriendDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private FriendRequest status;
    private LocalDateTime friendFrom;

    public FriendDTO(Long id, String username,String firstName, String lastName, LocalDateTime friendFrom, FriendRequest status){
        this.id = id;
        this.username = username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.friendFrom=friendFrom;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FriendRequest getStatus() {
        return status;
    }

    public void setStatus(FriendRequest status) {
        this.status = status;
    }

    @Override
    public String toString(){return ""+firstName+" | "+lastName+" | "+friendFrom;}
}
