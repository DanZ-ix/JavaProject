package proj;

import java.util.ArrayList;
import java.util.Objects;

public class UserBase {

    private static ArrayList<User> userArr = new ArrayList<>();

    private static User currentUser;

    public static void createUser(String name, String password, Access access)
    {
        User tempUser = new User(name, password, access);
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
        createUser("name1", "password", Access.HIGH);
        createUser("name2", "password", Access.HIGH);
        createUser("name3", "password", Access.HIGH);
        createUser("name4", "password", Access.HIGH);
        createUser("name5", "password", Access.HIGH);

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
        for (int i = 0; i < getUserCount();i++)
        {
            if (Objects.equals(name, getUser(i).getName()))
                return getUser(i);
        }

        throw new NoUserException();

    }


    public static User login(String login, String password) throws FailedLoginException, NoUserException
    {
        User user = getUserByName(login);

        if (Objects.equals(user.getPassword(), password))
        {
            return user;
        }
        else
            throw new FailedLoginException();

    }





}



class NoUserException extends Exception{

    public NoUserException(){
        super("Нет такого пользователя");
    }
}



class FailedLoginException extends Exception{

    public FailedLoginException(){
        super("Ошибка входа");
    }
}
