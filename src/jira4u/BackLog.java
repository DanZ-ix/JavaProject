package jira4u;


import java.util.ArrayList;

public class BackLog {

    private static ArrayList<Task> taskArr = new ArrayList<>();

    public static void addTask(Task task)
    {
        taskArr.add(task);
    }

    public static Task getTask(int num)
    {
        return taskArr.get(num);
    }

    public static int getLength()
    {
        return taskArr.size();
    }


    public static void createTasks()            //Костыль пока задачи создаются тут
    {
        User user1 = UserBase.getUser(1);



        Task task1 = new Task("Задача", Jira4U.priorities[3], "blbablablalba","11.02.2022", user1, user1);
        Task task2 = new Task("Задача", Jira4U.priorities[3], "blbablablalba","11.02.2022", user1, user1);
        Task task3 = new Task("Задачакек", Jira4U.priorities[3], "blbablablalba","11.02.2022", user1, user1);
        Task task4 = new Task("kek4", Jira4U.priorities[3], "blbablablalba","11.02.2022", user1, user1);
        Task task5 = new Task("kek5", Jira4U.priorities[3], "blbablablalba","11.02.2022", user1, user1);



        addTask(task1);
        addTask(task2);
        addTask(task3);
        addTask(task4);
        addTask(task5);



    }
}
