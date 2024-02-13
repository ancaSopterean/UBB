package com.example.companieaeriana.Controller;

import com.example.companieaeriana.Controller.alerts.MessageAlert;
import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    private Service service;
    @FXML
    TextField usernameTextField;

    @FXML
    private void initialize(){
    }
    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleLogin(ActionEvent ev) {
        String username = usernameTextField.getText();
        if(username.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"empty field","field cannot be empty");
        }
        else {
            Optional<Client> client = service.findOneUsername(username);
            if(client.isPresent()){
                    showClientDialog(client.get());
            }
            else{
                MessageAlert.showErrorMessage(null, "inexistent username");
            }
        }
        //System.out.println(service.getBilete24Ianuarie());
    }

    private void showClientDialog(Client client) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/companieaeriana/Views/profileclient-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profile Client");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            ProfileClientController profileClientController = loader.getController();
            profileClientController.setService(service,dialogStage, client);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
