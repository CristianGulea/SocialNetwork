package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField passwordTxt;
    @FXML
    private Button logInBtn;

    private SuperService service;

    @FXML
    private void handlerLogIn() throws IOException {
        String username= usernameTxt.getText();
        String password= passwordTxt.getText();
        try {
            User user = service.logIn(username, password);

            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("mainWindow.fxml"));
            Stage manWindowStage= new Stage();
            AnchorPane panel= fxmlLoader.load();

            MainWindowController mainWindowController= fxmlLoader.getController();
            mainWindowController.setService(service);
            mainWindowController.setLoggedUser(user);

            Scene scene = new Scene(panel, 545, 400);
            manWindowStage.setScene(scene);
            manWindowStage.initModality(Modality.NONE);
            manWindowStage.show();

            Stage logInStage =(Stage) logInBtn.getScene().getWindow();
            logInStage.close();
        }catch (ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();

        }
    }

    public void setService(SuperService service) {
        this.service = service;
    }
}
