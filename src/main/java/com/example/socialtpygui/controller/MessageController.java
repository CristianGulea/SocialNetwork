package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.UserSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MessageController {

    @FXML
    private TextField searchFriendBar;
    @FXML
    private BorderPane convPane;

    private SuperService service;
    private User loggedUser;

    /**
     * handle the press of a key
     * @param e .
     * @throws IOException .
     */
    @FXML
    private void handlerSearchBarKeyPressed(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER)){
            FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("searchFriendConv-view.fxml"));
            Pane item = loader.load();
            SearchFriendConvController searchFriendConvController= loader.getController();
            searchFriendConvController.load(service,loggedUser,searchFriendBar.getText());
            convPane.setCenter(item);
        }
    }

    /**
     * handle the custom UserSelected Event
     * @param e .
     */
    private void handlerForSelectedFriend(UserSelected e) {
        //here should be load the conv and the messages
        if (e.getEventType().equals(UserSelected.USER_SELECTED)) {
            System.out.println(e.getSelectedUserId());
        }
        else if (e.getEventType().equals(UserSelected.USER_LOAD_MSJ))
        {
            System.out.println(e.getSelectedUserId());
        }
    }

    /**
     * sets the service and loggedUser
     * @param service the SuperService
     * @param loggedUser  the user currently logged in
     */
    public void load(SuperService service, User loggedUser) throws IOException {
        convPane.getParent().addEventFilter(UserSelected.ANY,this::handlerForSelectedFriend);
        this.service=service;
        this.loggedUser=loggedUser;
        loadAllFriendsWithConv();
    }

    /**
     * Load all friends with conversation in BorderPane (convPane).
     */
    private void loadAllFriendsWithConv()
    {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("conversation-view.fxml"));
        Pane item = null;
        try {
            item = loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ConversationController conversationController = loader.getController();
        conversationController.setLoggedUser(loggedUser);
        conversationController.setService(service);
        conversationController.load();
        convPane.setCenter(item);
    }
}