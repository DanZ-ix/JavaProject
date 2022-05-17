package proj;

import java.util.Objects;

public class User {

    //region fields
    private String name;
    private String password;


    private static User currentUser;
    //endregion

    public User(String name, String password)
    {
        this.setName(name);
        this.setPassword(password);
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


    //region getters-setters

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion
}
