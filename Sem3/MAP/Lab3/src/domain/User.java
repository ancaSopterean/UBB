package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<User> friends;
    private static Long generatedId = 0l;

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        generatedId++;
        this.setId(generatedId);
        this.friends = new ArrayList<>();
    }

    /**
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName sets the first name of the user to be firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName sets the last name of the user to be lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return a list of all the friends of the user
     */
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    /**
     * adds a user to the friends list
     * @param newFriend - the user we want to add tho the user's friends lsit
     */
    public void addFriend(User newFriend){
        this.friends.add(newFriend);
    }

    /**
     * @param friend - the user we want to remove from the friends list
     */
    public void removeFriend(User friend){
        friends.remove(friend);
    }

    /**
     * @return a string of all the importat values of the user
     */
    @Override
    public String toString() {
        StringBuilder friendsString = new StringBuilder();
        int size = friends.size();
        for (User user : friends){
            friendsString.append(user.getId());
            if(size != 1){
                friendsString.append(", ");
            }
            size--;
        }
        return "User{" +
                "id="+getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friendsString +
                '}';
    }

    /**
     * @param o the object to be compared with
     * @return true, if the main object is equal to the object o
     *          false, otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }
}
