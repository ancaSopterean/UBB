package com.example.companieaeriana.Controller;

import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.Flight;
import com.example.companieaeriana.Domain.TicketDTO;
import com.example.companieaeriana.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ProfileClientController {
    private Service service;
    private Stage dialogStage;
    private Client client;
    public ObservableList<TicketDTO> model = FXCollections.observableArrayList();


    @FXML
    private TableView<TicketDTO> flightsTable;
    @FXML
    private TableColumn<TicketDTO,Long> idColumn;
    @FXML
    private TableColumn<TicketDTO,String> fromColumn;
    @FXML
    private TableColumn<TicketDTO,String> toColumn;
    @FXML
    private TableColumn<TicketDTO,LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<TicketDTO,LocalDateTime> landingTimeColumn;
    @FXML
    private TableColumn<TicketDTO,LocalDateTime> purchaseTimeColumn;

    @FXML
    private Label clientLabel;


    @FXML
    private void initialize(){
        //cum apar in dto:
        idColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, Long>("ticketId"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, String>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, String>("to"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, LocalDateTime>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, LocalDateTime>("landingTime"));
        purchaseTimeColumn.setCellValueFactory(new PropertyValueFactory<TicketDTO, LocalDateTime>("purchaseTime"));
        flightsTable.setItems(model);
        //listenNrUsersPageTxtField();
    }

    public void setService(Service service, Stage stage, Client client) {
        this.service = service;
        this.dialogStage = stage;
        this.client = client;
        clientLabel.setText(client.getName());
        //service.FriendshipChange().addObserver(this);
        initializeTableData();
    }

    private void initializeTableData() {
        List<TicketDTO> tickets = service.getClientTickets(this.client);
        model.setAll(tickets);
    }

    public void handle24IanuarieButton(){
        show24IanuarieDialog();
    }

    public void handleFiltrareButton(){
        showFiltrareDialog();
    }

    private void show24IanuarieDialog() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/companieaeriana/Views/24ianuarie-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("zboruri 24 ianuarie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            IanuarieController ianuarieController = loader.getController();
            ianuarieController.setService(service);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showFiltrareDialog() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/companieaeriana/Views/filtrare-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("filtrare zboruri");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            FiltrareController filtrareController = loader.getController();
            filtrareController.setService(service,client);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
