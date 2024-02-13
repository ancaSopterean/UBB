package com.example.companieaeriana.Controller;

import com.example.companieaeriana.Controller.alerts.MessageAlert;
import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.Flight;
import com.example.companieaeriana.Domain.TicketDTO;
import com.example.companieaeriana.Service.Service;
import com.example.companieaeriana.utils.events.FlightChangeEvent;
import com.example.companieaeriana.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FiltrareController implements Observer<FlightChangeEvent> {
    private Service service;

    public ObservableList<Flight> model = FXCollections.observableArrayList();
    public ObservableList<String> modelFromCBox = FXCollections.observableArrayList();
    public ObservableList<String> modelTOCBox = FXCollections.observableArrayList();
    @FXML
    private TableView<Flight> flightsTable;
    @FXML
    private TableColumn<Flight,String> fromColumn;
    @FXML
    private TableColumn<Flight,String> toColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<Flight,LocalDateTime> landingTimeColumn;
    @FXML
    private TableColumn<Flight,Integer> seatsColumn;
    @FXML
    private ComboBox<String> fromCBox;
    @FXML
    private ComboBox<String> toCBox;
    @FXML
    private DatePicker datePicker;
    private Client client;

    @FXML
    private void initialize(){
        //cum apar in dto:
        fromColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("to"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("landingTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Flight,Integer>("seats"));
        flightsTable.setItems(model);
        fromCBox.valueProperty().addListener(observable -> {
            initializeTableData();
        });
        toCBox.valueProperty().addListener(observable -> {
            initializeTableData();
        });
        datePicker.valueProperty().addListener(observable -> {
            initializeTableData();
        });
        //listenNrUsersPageTxtField();
    }

    public void setService(Service service,Client client) {
        this.service = service;
        this.client = client;
        service.addObserver(this);
        initializeCBox();
        initializeTableData();
    }

    private void initializeTableData() {
        if(fromCBox.getValue() != null && toCBox.getValue() != null && datePicker.getValue() != null) {
            List<Flight> flights = service.filtrareData(fromCBox.getValue(), toCBox.getValue(), datePicker.getValue());
            model.setAll(flights);
        }
    }

    private void initializeCBox(){
        Set<String> setFrom = service.getAllCitiesFrom();
        Set<String> setTo = service.getAllCitiesTo();
        modelFromCBox.setAll(setFrom);
        fromCBox.setItems(modelFromCBox);
        modelTOCBox.setAll(setTo);
        toCBox.setItems(modelTOCBox);
    }

    public void handleCumpara(ActionEvent ev){
        flightsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Flight flight = flightsTable.getSelectionModel().getSelectedItem();
            if(flight == null){
                MessageAlert.showMessage(null, Alert.AlertType.WARNING,"flight not selected","select a flight");
            }
            else {
                service.cumparaBilet(flight, this.client);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"info","ticket bought successfully");
            }
    }

    @Override
    public void update(FlightChangeEvent flightChangeEvent) {
        initializeTableData();
    }
}
