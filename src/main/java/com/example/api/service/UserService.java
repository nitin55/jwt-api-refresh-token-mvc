package com.example.api.service;

import com.example.api.dao.UserDao;
import com.example.api.model.User;

import java.util.List;

public class UserService {
    private final UserDao dao = new UserDao();

    public List<User> getAllUsers() {
        return dao.getAll();
    }

    public void addUser(User user) { dao.add(user); }

    public void updateUser(Long id, User user) { dao.update(id, user); }

    public void deleteUser(Long id) { dao.delete(id); }

    public boolean existsByEmail(String email) { return dao.existsByEmail(email); }

    public boolean existsById(Long id) { return dao.existsById(id); }
}

