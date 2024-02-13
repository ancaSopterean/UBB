package com.example.lab8.controller;

import com.example.lab8.controller.alerts.MessageAlert;
import com.example.lab8.domain.User;
import com.example.lab8.domain.validators.ValidationException;
import com.example.lab8.repo.RepositoryException;
import com.example.lab8.service.MainService;
import com.example.lab8.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class CreateAccountController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    private MainService service;
    @FXML
    private Stage dialogStage;


    private void initialize(){}

    public void setService(MainService service, Stage dialogStage){
        this.service =service;
        this.dialogStage =dialogStage;
    }

    @FXML
    public void handleSignUp(ActionEvent ev){
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        try{
            Optional<User> user = service.addUser(username,password,firstName,lastName);
            if(user.isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"created account","account created" +
                        "successfully");
                dialogStage.close();
            }
            else{
                MessageAlert.showErrorMessage(null, "username already exists");
            }

        } catch (RepositoryException | ServiceException | ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleHyperlink(ActionEvent ev){
        dialogStage.close();
    }

}
