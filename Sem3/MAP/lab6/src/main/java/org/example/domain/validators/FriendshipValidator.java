package org.example.domain.validators;

import org.example.domain.Friendship;

import java.util.Objects;

public class FriendshipValidator implements Validator<Friendship>{

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(Objects.equals(entity.getId().getLeft(), entity.getId().getRight())){
            throw new ValidationException("cannot be friends with yourself");
        }
    }
}
