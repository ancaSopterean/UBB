package com.example.lab7.service;

import com.example.lab7.domain.Friendship;
import com.example.lab7.domain.User;

import java.util.ArrayList;
import java.util.Optional;

public interface Service{
    Optional<User> addUser(String firstName, String lastName);
     Optional<User> deleteUser(Long id);
     void addFriendship(Long id1, Long id2);
     Optional<Friendship> deleteFriendship(Long id1, Long id2);
     Integer getCommunitiesNo();
     ArrayList<ArrayList<User>> mostSociableCommunity();
}
