package com.example.lab7;

import com.example.lab7.controller.UserController;
import com.example.lab7.domain.Friendship;
import com.example.lab7.domain.Tuple;
import com.example.lab7.domain.User;
import com.example.lab7.domain.validators.FriendshipValidator;
import com.example.lab7.domain.validators.UserValidator;
import com.example.lab7.domain.validators.Validator;
import com.example.lab7.repo.FriendshipDBRepo;
import com.example.lab7.repo.Repo;
import com.example.lab7.repo.UserDBRepo;
import com.example.lab7.service.MainService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    private Repo<Long, User> userRepository;
    private Repo<Tuple<Long, Long>, Friendship> friendshipRepository;
    private MainService service;

    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Validator<User> userValidator = new UserValidator();
        userRepository = new UserDBRepo(userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        friendshipRepository = new FriendshipDBRepo(friendshipValidator);
        service = new MainService(userRepository, friendshipRepository);

        initView(primaryStage);
        primaryStage.setTitle("Social Network");
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = userLoader.getController();
        userController.setService(service);
    }
}
