package com.example.api.dao;

import com.example.api.util.DbUtil;

import java.sql.*;
import java.util.Optional;

public class RefreshTokenDao {

    public static void save(String jti, String username, Timestamp expiresAt) {
        String sql = "INSERT INTO refresh_tokens(jti, username, expires_at) VALUES(?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, jti);
            ps.setString(2, username);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValid(String jti) {
        String sql = "SELECT expires_at FROM refresh_tokens WHERE jti=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, jti);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp expires = rs.getTimestamp("expires_at");
                return expires.after(new Timestamp(System.currentTimeMillis()));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    public static void revoke(String jti) {
        String sql = "DELETE FROM refresh_tokens WHERE jti=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, jti);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}

