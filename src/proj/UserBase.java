package proj;

import java.util.ArrayList;
import java.util.Objects;

public class UserBase {

    private static ArrayList<User> userArr = new ArrayList<>();

    private static User currentUser;

    public static void createUser(String name, String password)
    {
        User tempUser = new User(name, password);
        addUser(tempUser);
    }

    public static void addUser(User user)
    {
        userArr.add(user);
    }

    public static User getUser(int num)
    {
        return userArr.get(num);
    }

    public static int getUserCount()
    {
        return userArr.size();
    }


    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserBase.currentUser = currentUser;
    }

    public static void createSomeUsers()
    {
        createUser("Екатерина", "1234");
        createUser("Павел", "1234");
        createUser("Даниил", "1234");
        createUser("Татьяна", "1234");
        createUser("Сергей", "1234");
        createUser("", "");

    }

    public static String[] getAllUserNames()
    {
        String[] retString = new String[getUserCount()];

        for (int i = 0; i < getUserCount(); i++)
        {
            retString[i] = getUser(i).getName();
        }
        return retString;
    }


    public static User getUserByName(String name) throws NoUserException
    {
        return (DbConnector.getUserByLogin(name));
    }


    public static User login(String login, String password) throws FailedLoginException, NoUserException
    {
        if (login == "")
        {
            return UserBase.getUserByName("");
        }
        User user = getUserByName(login);

        if (Objects.equals(user.getPassword(), password))
        {
            return user;
        }
        else
            throw new FailedLoginException();

    }
}







