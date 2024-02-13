package org.example.service;

import org.example.domain.FriendDTO;
import org.example.domain.Friendship;
import org.example.domain.Tuple;
import org.example.domain.User;
import org.example.repo.Repo;
import org.example.repo.RepositoryException;
import org.example.repo.UserDBRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MainService implements Service{

    private Repo<Long,User> userRepo;
    private Repo<Tuple<Long,Long>, Friendship> friendshipRepo;
    private Set<User> set;

    public MainService(Repo<Long,User> userRepo, Repo<Tuple<Long,Long>, Friendship> friendshipRepo){
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
    public void addUser(String firstName, String lastName) {
        userRepo.save(new User(firstName, lastName));
    }

    /**
     * @param id id of the user to be deleted
     * @return the deleted user
     * @throws RepositoryException if the user does not exist
     */
    @Override
    public Optional<User> deleteUser(Long id) {
        return userRepo.delete(id);
 /*               .map(user ->{
                    if(getFriends(user) != null){
                        getFriends(user).forEach(friend -> deleteFriendship(user.getId(), friend.getId()));
                    }
                    return userRepo.delete(id);
                })
                .orElseThrow(() -> new ServiceException("user does not exist"));*/

    }


    /**
     * @param id1 id of the first person
     * @param id2 id of the second person
     * @throws ServiceException if the first user is already friends with the second one
     */
    @Override
    public void addFriendship(Long id1, Long id2) {
        User u1 = userRepo.findOne(id1)
                .orElseThrow(() -> new ServiceException("user with id "+id1+" not found"));
        User u2 = userRepo.findOne(id2)
                .orElseThrow(() -> new ServiceException("user with id "+id2+" not found"));
        if(getFriends(u1).contains(u2) || getFriends(u2).contains(u1)){
            throw new ServiceException("friendship aready exists");
        }
        Friendship friendship = new Friendship(u1,u2, LocalDateTime.now());
        friendshipRepo.save(friendship);
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
        if(deletedFr.isEmpty()){
            Tuple<Long,Long> idd = new Tuple<>(id2, id1);
            Optional<Friendship> delFr = friendshipRepo.delete(idd);
            if(delFr.isEmpty())
                throw new ServiceException("friendship does not exist");
            return friendshipRepo.delete(idd);
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

    static class Pair<F, S>{
        F first;
        S second;
        Pair(F first, S second){
            this.first = first;
            this.second = second;
        }
    }

    /**
     * @return toti utilizatorii din aplicatie
     */
    public Iterable<User> getAllUsers(){return userRepo.findAll();}
    public Iterable<Friendship> getAllFriendships(){return friendshipRepo.findAll();}

    public List<FriendDTO> getUserFriends(Long id, int month, int year){
        Iterable<Friendship> allFriendships = friendshipRepo.findAll();
        User user = userRepo.findOne(id).orElseThrow(()-> new ServiceException("user dose not exist"));
        return StreamSupport.stream(allFriendships.spliterator(),false)
                .filter(friendship -> (friendship.getFriendsFrom().getMonth().getValue() == month &&
                        friendship.getFriendsFrom().getYear()==year))
                .filter(friendship -> friendship.getUser1().equals(user) ||
                        friendship.getUser2().equals(user))
                .map(friendship -> {
                    if(friendship.getUser1().equals(user)){
                        return new FriendDTO(friendship.getUser2().getFirstName(),
                                friendship.getUser2().getLastName(),
                                friendship.getFriendsFrom());
                    }
                    else{
                        return new FriendDTO(friendship.getUser1().getFirstName(),
                                friendship.getUser1().getLastName(),
                                friendship.getFriendsFrom());
                    }
                })
                .toList();
    }

}
