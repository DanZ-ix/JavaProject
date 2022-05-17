package proj;

import java.sql.*;
import java.util.ArrayList;
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
            ResultSet rsUser = statement.executeQuery("Select * from USERS where deleted = 0 and login = '" + login + "'");
            rsUser.next();

            retUser = new User(rsUser.getString("login"), rsUser.getString("password"));
            addLog("LOGIN", retUser);
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
            throw new NoUserException();
        }

        return retUser;
    }

    public static User addUser(String login, String password) throws SQLException
    {

        String query = "Insert INTO USERS (login, password) VALUES ('"+ login+ "', '" + password + "')";

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
    {                                                       //Используется когда нет задачи
        addLog(action, user, 0);
    }

    public static void addLog(String action, User user, int task_id)     //Перегрузка логирования с тремя аргументами
    {                                                               //Используется когда все есть
        try
        {
            ResultSet resAction = statement.executeQuery("Select * from ACTION_LIST where deleted = 0 and name = '" + action + "' ");
            resAction.next();
            int action_id = resAction.getInt("action_id");

            ResultSet userRs = statement.executeQuery("select user_id from users where deleted = 0 and login = '" + user.getName()+"'");
            userRs.next();
            int user_id = userRs.getInt("user_id");
            statement.executeUpdate("Insert INTO Logging (users_user_id, action_list_action_id, task_id) VALUES ( "+
                    user_id +", "+ action_id + ",  " + task_id + " )" );

        }
        catch (SQLException ex)
        {
            System.out.println("Ошибка вставки лога");
            System.out.println(ex);
        }
    }

    public static ArrayList<Task> getTasks(int deleted)
    {

        String query = "select t.name task_name, p.name pr_name, t.description description, t.deadline deadline, " +
                "r.login worker, v.login reviewer, t.task_id task_id, s.name status from tasks t "+
                "inner join status s on t.STATUS_STATUS_ID = s.status_id " +
                "inner join priority p on t.priority_priority_id = p.priority_id " +
                "inner join users r on t.responsible_id = r.user_id " +
                "inner join users v on t.verifying_id = v.user_id " +
                "where t.deleted = " + deleted;
        //System.out.println(query);
        try
        {

            ResultSet res = statement.executeQuery(query);

            ArrayList<Task> taskArr = new ArrayList<Task>();

            while(res.next())
            {

                Task oneTask = new Task(res.getString("task_name"),
                        res.getString("pr_name"),
                        res.getString("description"),
                        res.getString("deadline"),
                        res.getString("worker"),
                        res.getString("reviewer"),
                        res.getInt("task_id"),
                        res.getString("status"));
                taskArr.add(oneTask);

            }

            return taskArr;
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
        return null;
    }

    public static void setTaskDone(int taskId)
    {
        String query = "update tasks set status_status_id = 2 where task_id = " + taskId;

        try
        {
            statement.executeUpdate(query);
            addLog("TASK_DONE", UserBase.getCurrentUser(), taskId);
        }
        catch(SQLException ex)
        {
            System.out.println("Ошибка изменения статуса задачи");
            System.out.println(ex);
        }
    }

    public static void addTask(Task task)
    {
        try
        {
            ResultSet respResSet = statement.executeQuery("select user_id from users where login = '" + task.getWorker_str() + "'");
            respResSet.next();
            int resp_id = respResSet.getInt("user_id");

            ResultSet verResSet = statement.executeQuery("select user_id from users where login = '" + task.getReviewer_str() + "'");
            verResSet.next();
            int ver_id = verResSet.getInt("user_id");

            ResultSet priorResSet = statement.executeQuery("select PRIORITY_ID from priority where name = '" + task.getPriority() + "'");
            priorResSet.next();
            int prior_id = priorResSet.getInt("PRIORITY_ID");


            String query = "insert into tasks (name, description, responsible_id, " +
                    "VERIFYING_ID, STATUS_STATUS_ID, deadline, PRIORITY_PRIORITY_ID) " +
                    "VALUES ('" + task.getName() + "', '" + task.getDescription() +  "', " +
                    resp_id + ", " + ver_id + ", " + 1 + ", '" + task.getDeadLine() + "', " +
                    prior_id + ")";

            statement.executeUpdate(query);

            ResultSet taskRs = statement.executeQuery("select task_seq.currval from dual");

            taskRs.next();
            int task_id = taskRs.getInt("CURRVAL");


            addLog("TASK_CREATED", UserBase.getCurrentUser(), task_id);
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }


    public static String[] getAllUserNames()
    {

        try
        {
            ResultSet countRS = statement.executeQuery("select count(*) c from users where deleted = 0");
            countRS.next();
            int count = countRS.getInt("c");

            ResultSet rs = statement.executeQuery("select login from users where deleted = 0");

            String[] retString = new String[count];
            int i = 0;
            while (rs.next())
            {
                retString[i] = rs.getString("login");
                i++;
            }

            return retString;
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
        }


        return null;
    }

    public static void deleteTask(Task task)
    {
        try
        {
            String query = "update tasks set deleted = 1 where task_id = " + task.getTaskId();
            statement.executeQuery(query);
            addLog("TASK_DELETED", UserBase.getCurrentUser(), task.getTaskId());
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }

    public static void restoreTask(Task task)
    {
        try
        {
            String query = "update tasks set deleted = 0 where task_id = " + task.getTaskId();
            statement.executeQuery(query);
            addLog("TASK_RESTORED", UserBase.getCurrentUser(), task.getTaskId());
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }



}
