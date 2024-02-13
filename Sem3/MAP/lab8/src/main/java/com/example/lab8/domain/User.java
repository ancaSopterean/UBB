package com.example.lab8.domain;

import com.example.lab8.utils.security.PasswordEncoder;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String salt;

    public User(String username, String password, String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }


    public void setPasswordWithSalt(String password){
        this.salt = PasswordEncoder.generateSalt();
        String hassedPassword = PasswordEncoder.hashPassword(password,salt);
        this.password = hassedPassword;
    }
    public String getSalt(){return salt;}
    public void setSalt(String salt){this.salt = salt;}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return username;
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
        return getUsername(). equals(that.getUsername());
    }
}
