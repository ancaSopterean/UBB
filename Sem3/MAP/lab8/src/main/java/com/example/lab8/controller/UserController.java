package com.example.lab8.controller;

import com.example.lab8.controller.alerts.MessageAlert;
import com.example.lab8.domain.User;
import com.example.lab8.repo.paging.Page;
import com.example.lab8.repo.paging.Pageable;
import com.example.lab8.service.MainService;
import com.example.lab8.service.MessageService;
import com.example.lab8.utils.events.UserChangeEvent;
import com.example.lab8.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {
    private MainService service;
    private MessageService messageService;
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User,String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;

    @FXML
    private Label pageNumberLabel;

    @FXML
    private TextField nrUsersPageTxtField;

    private int pageSize = 3;
    private int currentPage = 0;
    private int totalNoOfElems = 0;

    public ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userTable.setItems(model);
        listenNrUsersPageTxtField();
    }

    public void setService(MainService service,MessageService messageService){
        this.service = service;
        this.messageService = messageService;
        service.UserChange().addObserver(this);
        initializeTableData();
    }

    public void initializeTableData(){
//        Iterable<User> users = service.getAllUsers();
//        List<User> userList = StreamSupport.stream(users.spliterator(), false)
//                .toList();
//        model.setAll(userList);

        if(nrUsersPageTxtField.getText().isEmpty()){
            pageSize = 1;
        }
        else {
            pageSize = Integer.parseInt(nrUsersPageTxtField.getText());
        }

        Page<User> page = service.getUsersOnPage(new Pageable(currentPage,pageSize));

        int maxPage = (int) Math.ceil((double)page.getTotalNoOfElems()/pageSize) - 1;

        if(maxPage == -1){ maxPage = 0; }
        if(currentPage > maxPage){
            currentPage = maxPage;
            page = service.getUsersOnPage(new Pageable(currentPage,pageSize));
        }

        model.setAll(StreamSupport.stream(page.getElemsOnPage().spliterator(),
                false).collect(Collectors.toList()));

        totalNoOfElems = page.getTotalNoOfElems();

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage + 1) * pageSize >= totalNoOfElems );

        pageNumberLabel.setText((currentPage + 1) + "/" + (maxPage + 1));
    }


    private void listenNrUsersPageTxtField(){

        nrUsersPageTxtField.textProperty().addListener(o ->{
            //you might want to put a try catch here!!
            currentPage = 0;
            initializeTableData();
        });
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initializeTableData();
    }

    public void showUserEditDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab8/views/edituser-view.fxml"));
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

    @FXML
    public void handleProfileBtn(ActionEvent ev){
        User selected = (User) userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showProfileDialog(selected);
        }
        else {
            MessageAlert.showErrorMessage(null, "You did not select a user!");
        }
    }

    public void showProfileDialog(User user){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab8/views/profileuser-view.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profile User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            ProfileUserController profileUserController = loader.getController();
            profileUserController.setService(service, messageService,dialogStage, user);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handlePrev(ActionEvent ev){
        currentPage--;
        initializeTableData();
    }
    public void handleNext(ActionEvent ev){
        currentPage++;
        initializeTableData();
    }
}
