package com.example.lab8;

import com.example.lab8.controller.LoginController;
import com.example.lab8.controller.MessageController;
import com.example.lab8.controller.UserController;
import com.example.lab8.domain.Friendship;
import com.example.lab8.domain.Message;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.User;
import com.example.lab8.domain.validators.FriendshipValidator;
import com.example.lab8.domain.validators.UserValidator;
import com.example.lab8.domain.validators.Validator;
import com.example.lab8.repo.*;
import com.example.lab8.repo.paging.PagingRepo;
import com.example.lab8.service.MainService;
import com.example.lab8.service.MessageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    private PagingRepo<Long, User> userRepository;
    private PagingFriendshipRepo friendshipRepository;
    private Repo<Long, Message> messageRepo;
    private MainService service;
    private MessageService messageService;

    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Validator<User> userValidator = new UserValidator();
        userRepository = new UserDBRepo(userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        friendshipRepository = new FriendshipDBRepo(friendshipValidator);
        messageRepo = new MessageDBRepo();
        service = new MainService(userRepository, friendshipRepository);
        messageService = new MessageService(messageRepo, userRepository);


        initView(primaryStage);
        primaryStage.setTitle("Social Network");
        //primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LoginController loginController = userLoader.getController();
        loginController.setService(service,messageService);
    }
}
