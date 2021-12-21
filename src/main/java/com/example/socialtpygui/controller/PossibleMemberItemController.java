package com.example.socialtpygui.controller;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.ItemSelected;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PossibleMemberItemController {

    @FXML
    private Label labelPossibleMember;

    @FXML
    private Button btnPossibleMember;

    SuperService service;
    private UserDTO userDTO;
    int groupId;


    public void handlerAddMemberBtn(ActionEvent actionEvent) {
        service.addUserToGroup(userDTO.getId(), groupId);
        btnPossibleMember.fireEvent(new ItemSelected(ItemSelected.ADD_MEMBER, userDTO.getId()));
    }

    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Set userDTO and set text to labelPossibleMember.
     * @param userDTO UserDTO
     */
    public void setData(UserDTO userDTO)
    {
        this.userDTO = userDTO;
        labelPossibleMember.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
    }

    /**
     * Set groupId
     * @param groupId Integer
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
