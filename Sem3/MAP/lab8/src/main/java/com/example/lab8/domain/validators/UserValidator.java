package com.example.lab8.domain.validators;

import com.example.lab8.domain.User;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getUsername().isEmpty()){
            throw new ValidationException("username cannot be null");
        }
        if(entity.getPassword().isEmpty()){
            throw new ValidationException("password cannot be null");
        }

        if(entity.getFirstName().isEmpty()){
            throw new ValidationException("first name cannot be null");
        }
        if(entity.getLastName().isEmpty()){
            throw new ValidationException("last name cannot be null");
        }
    }
}
