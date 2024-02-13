package com.example.lab8.service;

import com.example.lab8.domain.Friendship;
import com.example.lab8.domain.User;

import java.util.ArrayList;
import java.util.Optional;

public interface Service{
    Optional<User> addUser(String username, String password, String firstName, String lastName);
     Optional<User> deleteUser(Long id);
     Optional<Friendship> addFriendship(Long id1, Long id2);
     Optional<Friendship> deleteFriendship(Long id1, Long id2);
     Integer getCommunitiesNo();
     ArrayList<ArrayList<User>> mostSociableCommunity();
}
