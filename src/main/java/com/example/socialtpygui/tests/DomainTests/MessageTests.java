package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageTests {

    private MessageTests(){}

    public static void runTest(){
        testSetGet();
    }

    private static void testSetGet()
    {

        User user = new User("Gulea", "Cristian","gulea@gmail.com","a");
        User user1 = new User("Paul", "Marian","marian@gmail.com","a2");
        User user2 = new User("George", "Mihai","mihai@gmail.com","a3");
        List<String> list = new ArrayList<>(); list.add("marian@gmail.com"); list.add("mihai@gmail.com");
        List<String> list1 = new ArrayList<>(); list1.add("marian@gmail.com"); list1.add("mihai@gmail.com"); list1.add("gulea@gmail.com");
        LocalDate date = LocalDate.of(2020, 1, 8);
        LocalDate date1 = LocalDate.of(2001, 1, 8);
        MessageDTO messageDTO = new MessageDTO( "gulea@gmail.com", list, "Reminder! Tema Laborator!", date);
        messageDTO.setId(123);
        assert (messageDTO.getMessage().equals("Reminder! Tema Laborator!"));
        assert (messageDTO.getFrom().equals("gulea@gmail.com"));
        assert (messageDTO.getId() == 123);
        assert (messageDTO.getTo().get(0).equals("marian@gmail.com"));
        assert (messageDTO.getTo().get(1).equals("mihai@gmail.com"));
        assert (messageDTO.getData().equals(date));
        messageDTO.setId(1234);
        messageDTO.setData(date1);
        messageDTO.setTo(list1);
        messageDTO.setMessage("REMINDER! Tema Laborator!");
        messageDTO.setFrom("gg@gmail.com");
        assert (messageDTO.getId() == 1234);
        assert (messageDTO.getMessage().equals("REMINDER! Tema Laborator!"));
        assert (messageDTO.getFrom().equals("gg@gmail.com"));
        assert (messageDTO.getTo().get(0).equals("marian@gmail.com"));
        assert (messageDTO.getTo().get(1).equals("mihai@gmail.com"));
        assert (messageDTO.getTo().get(2).equals("gulea@gmail.com"));
        assert (messageDTO.getData().equals(date1));


    }
}
