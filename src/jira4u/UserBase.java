package jira4u;

import java.util.ArrayList;

public class UserBase {

    private static ArrayList<User> userArr = new ArrayList<>();

    private static User currentUser;

    public static void createUser(String login, String password, String name, Access access)
    {
        User tempUser = new User(login, password, name, access);
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
        createUser("login1", "password", "name1", Access.HIGH);
        createUser("login2", "password", "name2", Access.HIGH);
        createUser("login3", "password", "name3", Access.HIGH);
        createUser("login4", "password", "name4", Access.HIGH);
        createUser("login5", "password", "name5", Access.HIGH);

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
            if (name == getUser(i).getName())
                return getUser(i);
        }

        throw new NoUserException("Нет такого пользователя");

    }
}


class NoUserException extends Exception{

    public NoUserException(String message){
        super(message);
    }
}