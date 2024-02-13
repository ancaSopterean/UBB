package service;

import domain.Friendship;
import domain.User;

import java.util.ArrayList;
import java.util.Optional;

public interface Service{
    void addUser(String firstName, String lastName);
     Optional<User> deleteUser(Long id);
     void addFriendship(Long id1, Long id2);
     Optional<Friendship> deleteFriendship(Long id1, Long id2);
     Integer getCommunitiesNo();
     ArrayList<ArrayList<User>> mostSociableCommunity();
}
