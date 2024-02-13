import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import domain.validators.Validator;
import repo.InMemoryRepo;
import service.MainService;
import ui.Console;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        InMemoryRepo<Long, User> userRepo = new InMemoryRepo<>(userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        InMemoryRepo<Tuple<Long, Long>, Friendship> friendshipRepo = new InMemoryRepo<>(friendshipValidator);
        MainService service = new MainService(userRepo, friendshipRepo);
        Console console = new Console(service);
        console.run();
        }
    }
