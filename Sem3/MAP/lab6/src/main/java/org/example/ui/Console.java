package org.example.ui;

import org.example.domain.User;
import org.example.domain.validators.ValidationException;
import org.example.repo.RepositoryException;
import org.example.service.MainService;
import org.example.service.ServiceException;

import java.util.ArrayList;
import java.util.Scanner;

public class Console {
    private MainService service;
    private static final Scanner scanner = new Scanner(System.in);

    public Console(MainService service){
        this.service = service;
    }

    private void printMenu(){
        System.out.println("\nMenu:");
        System.out.println("1) Show users");
        System.out.println("2) Add user");
        System.out.println("3) Delete user");
        System.out.println("4) Add friendship");
        System.out.println("5) Delete friendship");
        System.out.println("6) Number of communities");
        System.out.println("7) Most sociable community");
        System.out.println("8) Show friends from a user in a specific month");
        System.out.println("0) Exit");
    }

    private void addUserUi(){
        System.out.print("first name: ");
        String firstName = scanner.nextLine();
        System.out.print("last name: ");
        String lastName = scanner.nextLine();
        service.addUser(firstName,lastName);
        System.out.println("user addded successfully");
    }

    private void deleteUserUi(){
        System.out.print("id:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        service.deleteUser(id);
        System.out.println("user deleted successfully");
    }

    private void showUsersUi(){
        for(User user : service.getAllUsers()){
            System.out.println(user);
        }
    }

    private void addFriendshipUi(){
        System.out.print("id user1: ");
        Long id1 = scanner.nextLong();
        System.out.print("id user2: ");
        Long id2 = scanner.nextLong();
        scanner.nextLine();
        service.addFriendship(id1,id2);
        System.out.print("friend" +
                "ship created successfully");
    }

    private void deleteFriendshipUi(){
        System.out.print("id user1: ");
        Long id1 = scanner.nextLong();
        System.out.print("id user2: ");
        Long id2 = scanner.nextLong();
        scanner.nextLine();
        service.deleteFriendship(id1,id2);
        System.out.println("friendship deleted successfully");
    }


    private void mostSociableCommunityUi(){
        ArrayList<ArrayList<User>> most = service.mostSociableCommunity();
        System.out.println("the most sociable community is:");
        for(ArrayList<User> arr : most)
            System.out.println(arr);
    }

    private void getCommunitiesNoUi(){
        int communityNo = service.getCommunitiesNo();
        System.out.print("the number of communities:");
        System.out.println(communityNo);
    }

    private void getUserFriendsUI() {
        System.out.print("id: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Month: ");
        int month = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        service.getUserFriends(id, month, year).forEach(System.out::println);
    }

    public void run(){
        boolean running = true;
        while(running){
            printMenu();
            System.out.print(">>>");
            int opt = scanner.nextInt();
            scanner.nextLine();
            try{
                switch (opt){
                    case 0:
                        running = false;
                        break;
                    case 1:
                        showUsersUi();
                        break;
                    case 2:
                        addUserUi();
                        break;
                    case 3:
                        deleteUserUi();
                        break;
                    case 4:
                        addFriendshipUi();
                        break;
                    case 5:
                        deleteFriendshipUi();;
                        break;
                    case 6:
                        getCommunitiesNoUi();
                        break;
                    case 7:
                        mostSociableCommunityUi();
                        break;
                    case 8:
                        getUserFriendsUI();
                        break;

                }
            }
            catch (ValidationException | RepositoryException | ServiceException exc){
                System.out.println(exc.getMessage());
            }
        }
    }
}
