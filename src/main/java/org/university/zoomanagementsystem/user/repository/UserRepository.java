package org.university.zoomanagementsystem.user.repository;

import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;

import java.util.List;

public interface UserRepository {
    int addUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    void deleteUserById(int id);

    void updateUserWithoutPasswordChangeById(User user, int id);

    List<User> getUsersByRole(Role role);
}
