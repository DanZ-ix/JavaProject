package proj;

import java.sql.*;
import java.util.Locale;


public class DbConnector {
    private static String dbConnection = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String username = "proj";
    private static String DBpassword = "proj";
    private static Connection connection;
    private static Statement statement;


    public static void setConnection() throws SQLException
    {
        Locale.setDefault(Locale.US);

        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        try
        {
            connection = DriverManager.getConnection(dbConnection, username, DBpassword);
            statement = connection.createStatement();
            System.out.println("БД подключена");
        }
        catch (SQLException ex)
        {
            System.out.println("Ошибка в БД");
            System.out.println(ex.getMessage());
        }
    }

    public static User getUserByLogin(String login) throws NoUserException
    {
        User retUser;
        try
        {
            ResultSet rsUser = statement.executeQuery("Select * from USERS where deleted = 0 and login = " + login + ";");
            retUser = new User(rsUser.getString("login"), rsUser.getString("password"));
            addLog("LOG_IN", retUser);
        }
        catch (SQLException ex)
        {
            throw new NoUserException();
        }

        return retUser;
    }

    public static User addUser(String login, String password) throws SQLException
    {

        String query = "Insert INTO USERS (login, password) VALUES ("+ login +", "+ password + ")";

        statement.executeUpdate(query);
        User newUser = new User(login, password);

        addLog("REGISTRATION", newUser);

        return newUser;
    }

    public static void addLog(String action)                //Перегрузка логирования с одним аргументом
    {                                                       //Используется когда есть действующий пользователь,
        addLog(action, UserBase.getCurrentUser());          //он ставится по умолчанию
    }

    public static void addLog(String action, User user)     //Перегрузка логирования с двумя аргументами
    {                                                       //Используется когда нет действующего пользователя
        try
        {
            ResultSet resAction = statement.executeQuery("Select * from ACTION_LIST where name = " + action + "; ");
            int action_id = resAction.getInt("action_id");

            statement.executeUpdate("Insert INTO Logging (user_id, action_id) VALUES ("+
                    user.getName() +", "+ action_id + " );");

        }
        catch (SQLException ex)
        {
            System.out.println("Ошибка вставки лога");
        }
    }

}
