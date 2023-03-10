package com.example.socialtpygui.service.entityservice;


import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.repository.db.UserDb;
import com.example.socialtpygui.service.validators.NonExistingException;
import com.example.socialtpygui.service.validators.UserValidator;
import com.example.socialtpygui.service.validators.ValidationException;
import com.example.socialtpygui.utils.HashStringSHA_256;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDb repositoryUser;
    private final UserValidator validator;

    public UserService(UserDb userDb, UserValidator userValidator)
    {
        this.repositoryUser = userDb;
        this.validator = userValidator;
    }

    /**
     * Add a new user
     * @throws ValidationException when the email already exist
     */
    public void addUser(User newUser){
        validator.validate(newUser);

        if(repositoryUser.save(newUser)==null)
            throw new ValidationException("The email already exist!");
    }

    /**
     * Adds a new admin
     */
    public void addAdmin(User newUser){
        validator.validate(newUser);

        if(repositoryUser.save(newUser)!=null)
            throw new ValidationException("The email already exist!");
    }

    /**
     * @return all entities
     */
    public Iterable<User> findAll(int pageSize){return repositoryUser.findAll(pageSize);}

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    public User findOne(String id) {return repositoryUser.findOne(id);}

    /**
     * @return the size of the current elements in the repository
     */
    public int size(){return repositoryUser.size();}

    /**
     * Removes an user by id
     * @param id .
     * @throws ValidationException .
     * @throws NonExistingException .
     */
    public User removeUser(String id) {
        validator.validateEmail(id);
        User toremove = repositoryUser.remove(id);
        if (toremove == null)
            throw new NonExistingException("User does not exist!");
        return toremove;
    }

    /**
     * @param id .
     * @param password .
     * @return the user if the id and password are correct
     * @throws ValidationException if id or password is incorrect and if the user does not exist
     */
    public User logIn(String id, String password){
        validator.validateEmail(id);
        User user= repositoryUser.findOne(id);
        if (user==null)
            throw new ValidationException("User does not exist!");
        try {
            String hashStr = HashStringSHA_256.hashString(password);
            if (!user.getPassword().equals(hashStr))
                throw new ValidationException("Password is incorrect!");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Return a list with UserDto, where first_name and last_name contain s1 or s2 or reverse.
     * @param completName
     * @throws SQLException
     */
    public List<UserDTO> getUsersByName(String completName, int pageId) {
        if (completName.contains(" ")) {
            String[] splitName = completName.split(" ");
            String namePart1 = splitName[0];
            String namePart2 = splitName[1];
            return repositoryUser.getUsersByName(namePart1, namePart2,pageId);
        } else {
            return repositoryUser.getUsersByName(completName, "",pageId);
        }
    }

}

