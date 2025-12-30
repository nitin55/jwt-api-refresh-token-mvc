package com.example.api.dao;

import com.example.api.model.User;
import com.example.api.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class UserDao {

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, email FROM users";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(rs.getLong("id"),
                        "***",
                        rs.getString("username"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void add(User user) {
        String sql = "INSERT INTO users(username,password,email) VALUES (?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) user.setId(rs.getLong(1));
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

 public  Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }
    public void update(Long id, User user) {
        String sql = "UPDATE users SET username=?, email=? WHERE id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setLong(3, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM users WHERE id=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}

