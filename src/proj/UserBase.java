package proj;

import java.util.ArrayList;
import java.util.Objects;

public class UserBase {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserBase.currentUser = currentUser;
    }

    public static User login(String login, String password) throws FailedLoginException, NoUserException
    {
        User user = DbConnector.getUserByLogin(login);

        if (Objects.equals(user.getPassword(), password))
        {
            return user;
        }
        else
            throw new FailedLoginException();

    }
}







