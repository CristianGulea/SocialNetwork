package com.example.socialtpygui.repository.db;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventDb implements Repository<Integer, EventDTO> {
    String url,username,password;

    public EventDb(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public EventDTO findOne(Integer eventId) {
        String sql = "select name, description, date, location, user_event.email, users.first_name, users.last_name from event inner join user_event on event.id = user_event.id_event inner join users on users.email = user_event.email where event.id = ?";
        List<UserDTO> list = new ArrayList<>();
        String name = null;
        EventDTO eventDTO = null;
        String description = null;
        String location = null;
        LocalDate date = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                location = resultSet.getString("location");
                name = resultSet.getString("name");
                description = resultSet.getString("description");
                date = LocalDate.parse(resultSet.getString("date"));
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                UserDTO userDTO = new UserDTO(email, first_name, last_name);
                list.add(userDTO);
            }
            if (list.size() != 0) {eventDTO = new EventDTO(description, date, location, list, name);}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventDTO;
    }

    @Override
    public Iterable<EventDTO> findAll() {
        Set<EventDTO> events = new HashSet<>();
        List<UserDTO> list = new ArrayList<>();
        String sqlSelectAllEvents = "select * from event";
        String sqlSelectParticipants = "select users.first_name, users.last_name, user_event.email from user_event inner join users on users.email = user_event.email where id_event = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectAllEvents);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlSelectParticipants)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String location = resultSet.getString("location");
                String name = resultSet.getString("name");
                String  description = resultSet.getString("description");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));
                int id = resultSet.getInt("id");
                preparedStatement1.setInt(1, id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next())
                {
                    String first_name = resultSet1.getString("first_name");
                    String last_name = resultSet1.getString("last_name");
                    String email = resultSet1.getString("email");
                    UserDTO userDTO = new UserDTO(first_name, last_name, email);
                    list.add(userDTO);
                }
                EventDTO eventDTO = new EventDTO(description, date, location, list, name);
                eventDTO.setId(id);
                events.add(eventDTO);
                list.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public EventDTO save(EventDTO event) {
        String sqlInsertIntoEvent = "insert into event(description, date, location, name) values (?, ?, ?, ?) returning id";
        String sqlInsertIntoEventUser = "insert into user_event(id_event, email) values (?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlInsertIntoEvent);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlInsertIntoEventUser)) {
            preparedStatement1.setString(1, event.getDescription());
            preparedStatement1.setDate(2, Date.valueOf(event.getDate()));
            preparedStatement1.setString(3, event.getLocation());
            preparedStatement1.setString(4, event.getName());
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            event.setId(id);
            for (UserDTO user : event.getParticipants())
            {
                preparedStatement2.setInt(1, id);
                preparedStatement2.setString(2, user.getId());
                preparedStatement2.executeUpdate();
            }
            return event;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EventDTO remove(Integer eventId) {
        String sqlDeleteEvent = "delete from event where id = ?";
        String sqlDeleteMembers = "delete from user_event where id_event = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlDeleteEvent);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteMembers)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
            preparedStatement1.setInt(1, eventId);
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int size() {
        int size = 0;
        String sql = "select count(*) from event";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            size = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Add a user(participant) to user_event table.
     * @param user User
     * @param eventId Integer
     * @return user if the user was added and null if the user was not added
     */
    public User addParticipants(User user, int eventId)
    {
        String sql = "insert into user_event(id_event, email) values (?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.setString(2, user.getId());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remove a user(participant) from user_event table
     * @param email String
     * @param eventId Integer
     */
    public void removeParticipants(String email, int eventId)
    {
        String sql = "delete from user_event where id_event = ? and email = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}