package proj;

public class User {

    //region fields
    private String name;
    private String password;

    //endregion

    public User(String name, String password)
    {
        this.setName(name);
        this.setPassword(password);




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

    //endregion
}
