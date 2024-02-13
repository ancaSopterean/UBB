package com.example.lab7.controller;

import com.example.lab7.controller.alerts.MessageAlert;
import com.example.lab7.domain.User;
import com.example.lab7.service.MainService;
import com.example.lab7.utils.events.UserChangeEvent;
import com.example.lab7.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {
    private MainService service;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    public ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userTable.setItems(model);
    }

    public void setService(MainService service){
        this.service = service;
        service.addObserver(this);
        initializeTableData();
    }

    public void initializeTableData(){
        Iterable<User> users = service.getAllUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .toList();
        model.setAll(userList);
    }
    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initializeTableData();
    }

    public void showUserEditDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab7/views/edituser-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditUserController editUserController = loader.getController();
            editUserController.setService(service, dialogStage, user);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddUser(ActionEvent ev) {
        showUserEditDialog(null);
    }

    @FXML
    public void handleUpdateUser(ActionEvent ev) {
        User selected = (User) userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showUserEditDialog(selected);
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a user!");
        }
    }

    @FXML
    public void handleDeleteUser(ActionEvent ev) {
        User selected = (User) userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Optional<User> deleted = service.deleteUser(selected.getId());
            if (deleted.isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "User deleted!");
            }
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a user!");
        }
    }
}
