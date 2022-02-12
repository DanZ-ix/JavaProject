package jira4u;

public class User {

    //region fields
    private String login;
    private String password;
    private String name;
    private Access access;
    //endregion

    public User(String login, String password, String name, Access access)
    {
        this.setLogin(login);
        this.setPassword(password);
        this.setName(name);
        this.setAccess(access);


    }



    //region getters-setters

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }
    //endregion
}
