package com.example.lab7.domain.validators;

import com.example.lab7.domain.Friendship;

import java.util.Objects;

public class FriendshipValidator implements Validator<Friendship>{

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(Objects.equals(entity.getId().getLeft(), entity.getId().getRight())){
            throw new ValidationException("cannot be friends with yourself");
        }
    }
}
