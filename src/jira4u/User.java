package jira4u;

public class User {

    //region fields
    private String login;
    private String password;
    private String name;
    private Access access;
    //endregion

    public User(String name, String password, Access access)
    {
        this.setName(name);
        this.setPassword(password);

        this.setAccess(access);


    }



    //region getters-setters

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
