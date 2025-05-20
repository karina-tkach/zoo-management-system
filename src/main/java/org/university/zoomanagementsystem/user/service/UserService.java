package org.university.zoomanagementsystem.user.service;

import org.university.zoomanagementsystem.exception.not_found.UserNotFoundException;
import org.university.zoomanagementsystem.exception.validation.UserValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.UserValidator;
import org.university.zoomanagementsystem.user.repository.UserRepository;

import java.util.List;


@Service
public class UserService {
    private final UserValidator validator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserValidator userValidator;

    public UserService(UserValidator validator, UserRepository userRepository,
                       UserValidator userValidator) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.userValidator = userValidator;
    }

    public User addUser(User user) {
        try {
            logger.info("Try to add user");
            userValidator.validate(user);
            User existsUser = getUserByEmail(user.getEmail());
            if (existsUser != null) {
                throw new UserValidationException(
                        "User with email '" + existsUser.getEmail() + "' already exists"
                );
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            int id = userRepository.addUser(user);
            if (id == -1) {
                throw new UserValidationException("Unable to retrieve the generated key");
            }

            user.setId(id);
            logger.info("User was added:\n{}", user);
            return getUserById(id);

        } catch (UserValidationException | DataAccessException exception) {
            logger.warn("User wasn't added: {}\n{}", user, exception.getMessage());
            throw exception;
        }
    }

    public User getUserById(int id) {
        try {
            logger.info("Try to get user by id");
            User user = userRepository.getUserById(id);
            if (user == null) {
                throw new UserNotFoundException(String.format("User with id %d was not found", id));
            }
            logger.info("User was fetched successfully");
            return user;
        } catch (UserNotFoundException | DataAccessException exception) {
            logger.warn("User wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public User getUserByEmail(String email) {
        try {
            logger.info("Try to get user by email");
            User user = userRepository.getUserByEmail(email);
            logger.info("User was fetched by email successfully");
            return user;
        } catch (DataAccessException exception) {
            logger.warn("User wasn't fetched by email\n{}", exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteUserById(int id) {
        try {
            User userToDel = getUserById(id);
            logger.info("Try to delete user");
            validator.validateUserToDelete(userToDel);

            userRepository.deleteUserById(id);
            logger.info("User was deleted:\n{}", userToDel);
            return true;
        } catch (UserNotFoundException | UserValidationException | DataAccessException exception) {
            logger.warn("User wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public User updateUserWithoutPasswordChangeById(User user, int id) {
        try {
            User userToUpdate = getUserById(id);
            logger.info("Try to update user without password change");

            validator.validateUsersForUpdate(userToUpdate, user);

            userRepository.updateUserWithoutPasswordChangeById(user, id);

            logger.info("User was updated without password change:\n{}", user);
            return getUserById(id);
        } catch (UserNotFoundException | UserValidationException | DataAccessException exception) {
            logger.warn("User wasn't updated without password change: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<User> getUsersByRole(Role role) {
        try {
            logger.info("Try to get users by role");
            List<User> users = userRepository.getUsersByRole(role);
            logger.info("Users were fetched by role successfully");
            return users;
        } catch (DataAccessException exception) {
            logger.warn("Users weren't fetched by role\n{}", exception.getMessage());
            throw exception;
        }
    }

}
