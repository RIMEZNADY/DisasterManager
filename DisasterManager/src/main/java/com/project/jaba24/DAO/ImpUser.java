package com.project.jaba24.DAO;

import com.project.jaba24.business.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpUser implements IUser {
    private final String url = "jdbc:mysql://localhost:3306/" + System.getenv("DB_NAME");
    private final String user = System.getenv("DB_USER");
    private final String password = System.getenv("DB_PASSWORD");
    private Connection connection;
    private PreparedStatement preparedStatement;

    public ImpUser() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean insertUser(User user) {
        String query = "INSERT INTO user (username, email, password, location) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.setString(4, user.getLocation());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        user.setCode(resultSet.getLong(1));
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean LoginUser(String email, String password) {
        String query = "select * from user where email=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return checkPassword(password, resultSet.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        String query;
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            query = "update user set username=?, email=?, password=?, location=? where id=?";
        } else {
            query = "update user set username=?, email=?, location=? where id=?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                preparedStatement.setString(3, hashPassword(user.getPassword()));
                preparedStatement.setString(4, user.getLocation());
                preparedStatement.setLong(5, user.getCode());
            } else {
                preparedStatement.setString(3, user.getLocation());
                preparedStatement.setLong(4, user.getCode());
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(long code) {
        try {
            preparedStatement = connection.prepareStatement("delete from user where id=?");
            preparedStatement.setLong(1, code);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public User getUser(long code) {
        User user = null;
        try {
            preparedStatement = connection.prepareStatement("select * from user where id=?");
            preparedStatement.setLong(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("location"));
                user.setCode(resultSet.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        try {
            preparedStatement = connection.prepareStatement("select * from user where email=?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("location"));
                user.setCode(resultSet.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select * from user");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("location"));
                user.setCode(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public Connection getConnection() {
        return connection;
    }
}
