package com.example.lab8.service;

import com.example.lab8.domain.*;
import com.example.lab8.repo.PagingFriendshipRepo;
import com.example.lab8.repo.Repo;
import com.example.lab8.repo.RepositoryException;
import com.example.lab8.repo.paging.Page;
import com.example.lab8.repo.paging.Pageable;
import com.example.lab8.repo.paging.PagingRepo;
import com.example.lab8.utils.events.ChangeEventType;
import com.example.lab8.utils.events.FriendshipChangeEvent;
import com.example.lab8.utils.events.UserChangeEvent;
import com.example.lab8.utils.events.myEvent;
import com.example.lab8.utils.observer.Observable;
import com.example.lab8.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MainService implements Service {

    private PagingRepo<Long,User> userRepo;
    private PagingFriendshipRepo friendshipRepo;
    private Set<User> set;

    public MainService(PagingRepo<Long,User> userRepo, PagingFriendshipRepo friendshipRepo){
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    private List<User> getFriends(User user){
        Iterable<Friendship> allFriends = friendshipRepo.findAll();
        return StreamSupport.stream(allFriends.spliterator(),false)
                .filter(friendship -> friendship.getUser1().equals(user))
                .map(Friendship::getUser2)
                .toList();
    }

    /**
     * @param firstName first name of the user to be added
     * @param lastName last name of the user to be added
     */
    @Override
    public Optional<User> addUser(String username, String password, String firstName, String lastName) {
        //Optional<User> user = userRepo.save(new User(username, password,firstName, lastName));
        User user = new User(username,password,firstName,lastName);
        user.setPasswordWithSalt(password);

        Optional<User> addedUser = userRepo.save(user);
        if (addedUser.isEmpty()){
            UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.ADD, null));
        }
        return addedUser;    }

    /**
     * @param id id of the user to be deleted
     * @return the deleted user
     * @throws RepositoryException if the user does not exist
     */
    @Override
    public Optional<User> deleteUser(Long id) {
        Optional<User> user = userRepo.delete(id);
        user.ifPresent(value -> UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.DELETE, value)));
        return user;
    }


    public Optional<User> updateUser(Long id, String usernameN, String newPassword,String newFirstName, String newLastName) {
        Optional<User> oldUser = userRepo.findOne(id);
        if (oldUser.isPresent()) {
            User newUser = new User(usernameN,newPassword,newFirstName, newLastName);
            newUser.setId(id);
            Optional<User> user = userRepo.update(newUser);
            UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.UPDATE, newUser, oldUser.get()));
            return user;
        }
        return oldUser;
    }


    /**
     * @param id1 id of the first person
     * @param id2 id of the second person
     * @throws ServiceException if the first user is already friends with the second one
     */
    @Override
    public Optional<Friendship> addFriendship(Long id1, Long id2) {
        User u1 = userRepo.findOne(id1)
                .orElseThrow(() -> new ServiceException("user with id "+id1+" not found"));
        User u2 = userRepo.findOne(id2)
                .orElseThrow(() -> new ServiceException("user with id "+id2+" not found"));
        if(getFriends(u1).contains(u2) || getFriends(u2).contains(u1)){
            throw new ServiceException("friendship aready exists");
        }
        Friendship friendship = new Friendship(u1,u2, LocalDateTime.now(), FriendRequest.PENDING);
        Optional<Friendship> savedFr = friendshipRepo.save(friendship);
        if(savedFr.isEmpty()){
            FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD,null));
        }

        return savedFr;
    }


    /**
     * @param id1 id of the first person
     * @param id2 id of the second person
     * @return the deleted friendship
     */
    @Override
    public Optional<Friendship> deleteFriendship(Long id1, Long id2) {
        Tuple<Long,Long> id = new Tuple<>(id1, id2);
        Optional<Friendship> deletedFr = friendshipRepo.delete(id);
        if(deletedFr.isEmpty()) {
            Tuple<Long, Long> idd = new Tuple<>(id2, id1);
            Optional<Friendship> delFr = friendshipRepo.delete(idd);
            if (delFr.isEmpty())
                throw new ServiceException("friendship does not exist");
            else {
                FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, delFr.get()));
            }
            return delFr;
        }
        else {
            FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, deletedFr.get()));
        }
        return deletedFr;
    }

    private void DFS(User user, ArrayList<User> community){
        set.add(user); //visited
        community.add(user);

        getFriends(user).stream()
                .filter(friend -> !set.contains(friend))
                .forEach(friend -> DFS(friend, community));
    }

    /** gaseste componentele conexe din graf
     * o componenta conexa reprezinta o comunitate
     * @return numarul tuturor componentelor conexe
     */
    @Override
    public Integer getCommunitiesNo() {
        set = new HashSet<User>();
        Iterable<User> users = userRepo.findAll();
        List<List<User>> communities = StreamSupport.stream(users.spliterator(),false)
                .filter(user -> !set.contains(user))
                .map(user -> {
                    ArrayList<User> community = new ArrayList<>();
                    DFS(user, community);
                    return community;
                })
                .collect(Collectors.toList());
        return communities.size();
    }

    /**
     * @return communities - lista cu toate comunitatile, adica toate componentele conexe
     */
    public ArrayList<ArrayList<User>> getAllCommunities(){
        set = new HashSet<User>();

        Iterable<User> users = userRepo.findAll();
        return (ArrayList<ArrayList<User>>) StreamSupport.stream(users.spliterator(),false)
                .filter(user -> !set.contains(user))
                .map(user -> {
                    ArrayList<User> community = new ArrayList<>();
                    DFS(user, community);
                    return community;
                })
                .collect(Collectors.toList());
    }

    private Pair<Integer, Integer> BFS(int s, int noUsers, LinkedList<Integer>[] adj){
        int[] dis = new int[noUsers];
        Arrays.fill(dis, -1);
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        dis[s] = 0;
        while (!q.isEmpty()){
            int t = q.poll();
            for (int i = 0; i < adj[t].size(); i++){
                int v = adj[t].get(i);
                if (dis[v] == -1){
                    q.add(v);
                    dis[v] = dis[t] + 1;
                }
            }
        }
        int maxDis = 0;
        int nodeIdx = 0;
        for (int i = 0; i < noUsers; ++i){
            if (dis[i] > maxDis){
                maxDis = dis[i];
                nodeIdx = i;
            }
        }
        return new Pair<Integer, Integer>(nodeIdx, maxDis);
    }

    /**
     * @param community comunitatea careia urmeaza sa ii afle lungimea
     * @return drumul cel mai lung al comunitatii
     */
    private Integer longestPathLenght(ArrayList<User> community){
        int noUsers = community.size();
        LinkedList<Integer>[] adj = IntStream.range(0,noUsers)
                .mapToObj(i -> new LinkedList<Integer>())
                .toArray(LinkedList[]::new);


        for(User user : community){
            int userIndex = community.indexOf(user);
            for (User friend : getFriends(user)){
                int friendIndex = community.indexOf(friend);
                adj[userIndex].add(friendIndex);
            }
        }
        Pair<Integer,Integer> t1 = BFS(0, noUsers,adj);
        Pair<Integer,Integer> t2 = BFS(t1.first, noUsers,adj);
        return  t2.second;
    }

    /**
     * @return comunitatea cu cel mai lung drum
     */
    @Override
    public ArrayList<ArrayList<User>> mostSociableCommunity() {
        ArrayList<ArrayList<User>> communities = getAllCommunities();
        int max = communities.stream()
                .mapToInt(this::longestPathLenght)
                .max()
                .orElse(Integer.MIN_VALUE);

        return (ArrayList<ArrayList<User>>) communities.stream()
                .filter(community -> longestPathLenght(community) == max)
                .collect(Collectors.toList());
    }





    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();


    public Observable<UserChangeEvent> UserChange() {
        return new Observable<UserChangeEvent>() {

            @Override
            public void addObserver(Observer<UserChangeEvent> e) {
                observers.add(e);
            }

            @Override
            public void removeObserver(Observer<UserChangeEvent> e) {
                observers.remove(e);
            }

            @Override
            public void notifyObservers(UserChangeEvent t) {
                observers.forEach(x -> x.update(t));
            }
        };
    }


    private List<Observer<FriendshipChangeEvent>> observersF = new ArrayList<>();


    public Observable<FriendshipChangeEvent> FriendshipChange() {
        return new Observable<FriendshipChangeEvent>() {
            @Override
            public void addObserver(Observer<FriendshipChangeEvent> e) {
                observersF.add(e);
            }

            @Override
            public void removeObserver(Observer<FriendshipChangeEvent> e) {
                observersF.remove(e);
            }

            @Override
            public void notifyObservers(FriendshipChangeEvent t) {
                observersF.forEach(x -> x.update(t));
            }
        };
    }





    static class Pair<F, S>{
        F first;
        S second;
        Pair(F first, S second){
            this.first = first;
            this.second = second;
        }
    }


    public Optional<Friendship> updateFriendship(Tuple<Long,Long> id, LocalDateTime dataNou, FriendRequest statusNou){
        Optional<Friendship> oldFr = friendshipRepo.findOne(id);
        if(oldFr.isPresent()){
            User u1 = userRepo.findOne(id.getLeft())
                    .orElseThrow(() -> new ServiceException("user inexistent"));
            User u2 = userRepo.findOne(id.getRight())
                    .orElseThrow(() -> new ServiceException("user inexistent"));
            Friendship newFr = new Friendship(u1,u2, dataNou, statusNou);
            Optional<Friendship> fr = friendshipRepo.update(newFr);
            if(fr.isEmpty()) {
                FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, newFr, oldFr.get()));
                return oldFr;
            }
            return fr;
        }
        return oldFr;
    }




    /**
     * @return toti utilizatorii din aplicatie
     */
    public Iterable<User> getAllUsers(){return userRepo.findAll();}
    public Iterable<Friendship> getAllFriendships(){return friendshipRepo.findAll();}

    public Page<FriendDTO> getUserFriends(Pageable pageable, User user){
        Page<Friendship> friendshipPage = friendshipRepo.findAllOnPage(pageable,user);

        Iterable<Friendship> allFriendshipsOnPage = friendshipPage.getElemsOnPage();

        //User user = userRepo.findOne(id).orElseThrow(()-> new ServiceException("user dose not exist"));
        List<FriendDTO> friends = StreamSupport.stream(allFriendshipsOnPage.spliterator(),false)
                .filter(friendship -> friendship.getUser1().equals(user) ||
                                      friendship.getUser2().equals(user))
                .map(friendship -> {
                    if(friendship.getUser1().equals(user)){

                        return new FriendDTO(friendship.getUser2().getId(),friendship.getUser2().getUsername(), friendship.getUser2().getFirstName(),
                                friendship.getUser2().getLastName(),
                                friendship.getFriendsFrom(), friendship.getStatus());
                    }
                    else{
                        return new FriendDTO(friendship.getUser1().getId(),friendship.getUser1().getUsername(),friendship.getUser1().getFirstName(),
                                friendship.getUser1().getLastName(),
                                friendship.getFriendsFrom(), friendship.getStatus());
                    }
                })
                .toList();
        return new Page<>(friends,friendshipPage.getTotalNoOfElems());
    }

    public List<User> getStrangers(User user){
        Iterable<User> users = userRepo.findAll();
        List<User> friends = getFriends(user);
        return StreamSupport.stream(users.spliterator(),false)
                .filter(user1 -> !friends.contains(user1))
                .toList();
    }

    public Optional<User> findOneUsername(String username){
        Iterable<User> users = userRepo.findAll();
        return StreamSupport.stream(users.spliterator(),false)
                .filter(user ->user.getUsername().equals(username))
                .findFirst();
    }


    public Page<User> getUsersOnPage(Pageable pageable){
        return userRepo.findAllOnPage(pageable);
    }


}
