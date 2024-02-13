package com.example.companieaeriana.Controller;

import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.TicketDTO;
import com.example.companieaeriana.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class IanuarieController {
    private Service service;
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
    private TableColumn<TicketDTO, LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<TicketDTO,LocalDateTime> landingTimeColumn;
    @FXML
    private TableColumn<TicketDTO,LocalDateTime> purchaseTimeColumn;

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

    public void setService(Service service) {
        this.service = service;
        //service.FriendshipChange().addObserver(this);
        initializeTableData();
    }

    private void initializeTableData() {
        List<TicketDTO> tickets = service.getBilete24Ianuarie();
        model.setAll(tickets);
    }
}
