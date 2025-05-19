package org.university.zoomanagementsystem.user;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.UserValidationException;

import java.util.regex.Pattern;

@Component
@SuppressWarnings({"java:S1192", "java:S5998"})
public class UserValidator {
    public void validate(User user) {
        validateUserIsNotNull(user);
        validateName(user.getName());
        validatePassword(user.getPassword());
        validateEmail(user.getEmail());
        validateRole(user.getRole());
    }

    public void validateUserToDelete(User user) {
        validateRole(user.getRole());
    }

    public void validateUsersForUpdate(User userToUpdate, User user) {
        user.setPassword("User1234");

        if(user.getName() == null) {
            user.setName(userToUpdate.getName());
        }
        if(user.getEmail() == null) {
            user.setEmail(userToUpdate.getEmail());
        }
        if(user.getRole() == null) {
            user.setRole(userToUpdate.getRole());
        }

        validate(user);
    }

    private void validateUserIsNotNull(User user) {
        if (user == null) {
            throw new UserValidationException("User was null");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new UserValidationException("User role was null");
        }
        if (role == Role.ADMIN) {
            throw new UserValidationException("User has role 'ADMIN'");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new UserValidationException("User name was null");
        }
        if (name.isBlank()) {
            throw new UserValidationException("User name was empty");
        }
        if (name.length() > 30 || name.length() < 2) {
            throw new UserValidationException("User name had wrong length (must be 2 to 30 characters)");
        }
        Pattern pattern = Pattern.compile("^[A-Z][a-z]+(?: [A-Z][a-z]*)?$");
        if (!pattern.matcher(name).find()) {
            throw new UserValidationException("User name had invalid characters");
        }
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new UserValidationException("User email was null");
        }
        if (email.isBlank()) {
            throw new UserValidationException("User email was empty");
        }
        validateEmailHasProperStructure(email);
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new UserValidationException("User password was null");
        }
        if (password.isBlank()) {
            throw new UserValidationException("User password was empty");
        }
        if (password.length() > 20 || password.length() < 8) {
            throw new UserValidationException("User password must have 8 to 20 characters");
        }
        if (!validateSymbols(password)) {
            throw new UserValidationException("User password must have at least 1 uppercase, 1 lowercase letter and 1 digit");
        }
    }

    private boolean validateSymbols(String str) {
        int upperCase = 0;
        int lowerCase = 0;
        int digits = 0;
        for (char symbol : str.toCharArray()) {
            if (symbol >= 'A' && symbol <= 'Z') {
                upperCase++;
            } else if (symbol >= 'a' && symbol <= 'z') {
                lowerCase++;
            } else if (symbol >= '0' && symbol <= '9') {
                digits++;
            }
        }
        return upperCase != 0
            && lowerCase != 0
            && digits != 0;
    }

    private void validateEmailHasProperStructure(String email) {
        Pattern characters = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        if (!characters.matcher(email).find()) {
            throw new UserValidationException("User email had invalid structure");
        }
    }
}
