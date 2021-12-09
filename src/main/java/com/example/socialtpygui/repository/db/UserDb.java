package com.example.socialtpygui.repository.db;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.repository.Repository;
import com.example.socialtpygui.service.validators.ValidationException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDb implements Repository<String,User> {
    String url,username,password;

    public UserDb(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public User findOne(String s) {
        User user = null;
        String sql = "select * from users where email= ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1,s);
            ResultSet resultSet= statement.executeQuery();
            while (resultSet.next())
                user = new User(resultSet.getString("first_name")
                        ,resultSet.getString("last_name")
                        ,s,resultSet.getString("password"),resultSet.getBoolean("admin"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                Boolean admin = resultSet.getBoolean("admin");

                User user = new User(firstName,lastName,email,password,admin);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity==null)
            throw new ValidationException("Entity must not be null");

        User a = this.findOne(entity.getId());
        if(a != null) return a;

        String sql = "insert into users (email,first_name, last_name,password,admin ) values (?, ?, ?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3,entity.getLastName());
            ps.setString(4,entity.getPassword());
            ps.setBoolean(5,entity.isAdmin());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public User remove(String s) {
        User toremove = findOne(s);

        String sql = "delete from users where email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,s);

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toremove;
    }

    @Override
    public int size() {
        int size=0;
        String sql = "select COUNT(*) from users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next())
                size=resultSet.getInt(1);
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return size;
    }

}
