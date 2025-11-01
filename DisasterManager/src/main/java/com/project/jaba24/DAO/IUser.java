package com.project.jaba24.DAO;

import com.project.jaba24.business.User;

import java.util.List;

public interface IUser {
    public boolean insertUser(User user);
    public boolean updateUser(User user);
    public boolean deleteUser(long code);
    public User getUser(long code);
    public User getUserByEmail(String email);
    public List<User> getAllUsers();
    public boolean LoginUser(String email, String password);
    public String hashPassword(String password);
    public boolean checkPassword(String password, String hash);
}
