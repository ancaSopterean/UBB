package org.example;

import org.example.domain.Friendship;
import org.example.domain.Tuple;
import org.example.domain.User;
import org.example.domain.validators.FriendshipValidator;
import org.example.domain.validators.UserValidator;
import org.example.domain.validators.Validator;
import org.example.repo.FriendshipDBRepo;
import org.example.repo.InMemoryRepo;
import org.example.repo.Repo;
import org.example.repo.UserDBRepo;
import org.example.service.MainService;
import org.example.ui.Console;


public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        //InMemoryRepo<Long, User> userRepo = new InMemoryRepo<>(userValidator);
        Repo<Long,User> userRepo = new UserDBRepo(userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        //InMemoryRepo<Tuple<Long, Long>, Friendship> friendshipRepo = new InMemoryRepo<>(friendshipValidator);
        Repo<Tuple<Long,Long>, Friendship> friendshipRepo = new FriendshipDBRepo(friendshipValidator);
        MainService service = new MainService(userRepo, friendshipRepo);
        Console console = new Console(service);
        console.run();
    }
}
