package com.example.companieaeriana;

import com.example.companieaeriana.Controller.LoginController;
import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.Flight;
import com.example.companieaeriana.Domain.Ticket;
import com.example.companieaeriana.Repository.ClientRepository;
import com.example.companieaeriana.Repository.FlightRepository;
import com.example.companieaeriana.Repository.Repository;
import com.example.companieaeriana.Repository.TicketRepository;
import com.example.companieaeriana.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    private Repository<Long, Client> clientRepository;
    private Repository<Long, Flight> flightRepository;
    private Repository<Long, Ticket> ticketRepository;
    private Service service;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        clientRepository = new ClientRepository();
        flightRepository = new FlightRepository();
        ticketRepository = new TicketRepository()   ;
        service = new Service(clientRepository,ticketRepository,flightRepository);

        initView(primaryStage);
        primaryStage.setTitle("Companie Aeriana");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader clientLoader = new FXMLLoader();
        clientLoader.setLocation(getClass().getResource("Views/login-view.fxml"));
        AnchorPane userLayout = clientLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LoginController loginController = clientLoader.getController();
        loginController.setService(service);
    }
}
