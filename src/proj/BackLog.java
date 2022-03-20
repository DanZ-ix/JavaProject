package proj;


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
        User user2 = UserBase.getUser(3);
        User user3 = UserBase.getUser(2);
        User user4 = UserBase.getUser(0);

        Task task1 = new Task("Дизайн", MAIN.priorities[3], "Создать паттерн для обложки видео","11.04.2022", user1, user2);
        Task task2 = new Task("Копирайтинг", MAIN.priorities[3], "Написать 10 постов на тему путешествий","6.04.2022", user2, user3);
        Task task3 = new Task("Презентация", MAIN.priorities[3], "Презентовать заказчику дизайн сайта","3.04.2022", user3, user4);
//        Task task4 = new Task("kek4", MAIN.priorities[3], "blbablablalba","11.02.2022", user1, user1);
//        Task task5 = new Task("kek5", MAIN.priorities[3], "blbablablalba","11.02.2022", user1, user1);



        addTask(task1);
        addTask(task2);
        addTask(task3);
//        addTask(task4);
//        addTask(task5);



    }
}
