package proj;


import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static proj.MAIN.*; //импортируем константы, методы и т д из главного класса

public class Navigation {  //собрание методов, которые отвечают за отрисовку страничек

    private static final Color TYPES_NOT_IN_FOCUS = Color.rgb(252, 204, 233); //константы, отвечают за цвет 
    private static final Color TYPE_IN_FOCUS = Color.rgb(255, 150, 214);
    public final static Color TASK_COLOR = Color.WHITE;


    public static void addNewTask()    //функция отрисовки страницы добавлния задания 
    {
        VBox box = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND)); //отступ сверху


        HBox nameStatusPriority = new HBox(); // название задачи + приоритет 

        //для расположения элементов
        BorderPane namePane = getPaneWithText("", SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGE, "Название задачи");
        TextField nameTextField = addFieldToNode(namePane); //поле с вводом 
        nameStatusPriority.getChildren().add(namePane); //добавляем в горизонтальную коробку з+пр

        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));//отступы
        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        //то же самое с приоитетом
        BorderPane priorityPane = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGERED, "Приоритет");
        ComboBox<String> priorityComboBox = addComboBox(priorityPane, priorities);
        nameStatusPriority.getChildren().add(priorityPane);


        // в большую вертикальную коробку добавляем маленькую с н з + пр
        box.getChildren().add(nameStatusPriority);
        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND)); // отступ


        //описание 
        BorderPane descPane = getPaneWithText(" ", SC_HEIGHT / 4, (int) (SC_WIDTH * 0.8), Color.WHITE, "Описание");
        TextArea descTextArea = addAreaToNode(descPane);
        box.getChildren().add(descPane);
        
        //коробка нижняя горизонтальная + коробка вертикальная для людей
        HBox lowDesc = new HBox();
        VBox people = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND)); //отступ

        //добавляем выпадающий список с людьми
        BorderPane worker = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Исполнитель");
        ComboBox<String> workerCombobox = addComboBox(worker, DbConnector.getAllUserNames());
        people.getChildren().add(worker);



        people.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));//отступ

        //проверяющие
        BorderPane reviewer = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Проверяющий");
        ComboBox<String> reviewerComboBox = addComboBox(reviewer, DbConnector.getAllUserNames());
        people.getChildren().add(reviewer);

        //добавляем коробку с людьми в горизонтальную
        lowDesc.getChildren().add(people);
        //отступ справа
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        
        BorderPane deadLine = getPaneWithText(" ", SC_HEIGHT / 7, SC_WIDTH / 5, Color.WHITE, "Срок сдачи");
        TextField deadLineTxtField = addFieldToNode(deadLine);
        lowDesc.getChildren().add(deadLine);

        
        StackPane button = getPaneWithText("Добавить 💘", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.15), Color.rgb(179, 229, 255));//функция принимает только int значения, а там получется дробь, поэтому умножаем 
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));//отступ справа от даты

        //выставляем действия, которые происходят при нажатии на кнопку
        button.setOnMouseClicked((MouseEvent click) -> {

            Task newTask = new Task(            // функция возвращает текст, который написан в данный момент в поле
                    nameTextField.getText(),
                    priorityComboBox.getValue(),
                    descTextArea.getText(),
                    deadLineTxtField.getText(),
                    workerCombobox.getValue(),
                    reviewerComboBox.getValue(),
                    1, STATUS_IN_WORK
            );

            DbConnector.addTask(newTask);

            removeAll(); //при нажатии кнопки добавить, появляется главный экран
            showTasks(STATUS_All);

        });


        lowDesc.getChildren().add(button); //в нижнюю горизонтальную коробку добавляется кнопка "добавить"

        StackPane backButton = getPaneWithText("Назад", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.15), Color.PALETURQUOISE);

        backButton.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            showTasks(STATUS_All);
        });

        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));//отступ справа от даты

        lowDesc.getChildren().add(backButton);

        box.getChildren().add(lowDesc); //в большую вертикальную добавляем

        box.setLayoutY(SC_HEIGHT * 0.05); //отступы
        box.setLayoutX(SC_WIDTH * 0.1);

        root.getChildren().add(box); // добавляем все в главный экран (рут)
    }


    public static void showTasks(String status)
    {
        showTasks(status, 1);
    }


    public static void showTasks(String status, int page) { //экран с задачами


        HBox mainScreen = new HBox();

        VBox tasks = new VBox();

        HBox taskTypes = new HBox();

        //taskTypes.getChildren().add(new Rectangle(15, 15, BACKGROUND));

        addTaskTypes(taskTypes, status); //функция, чтобы добавить типы задач, передаем гориз коробку и статус - переменная которая отвечают за нажатую кнопку

        tasks.getChildren().add(taskTypes);

        ArrayList<Task> taskArr = DbConnector.getTasks(0);

        int task_count = 0;
        int last_task = 0;

        if (status == STATUS_All)
        {
            if (page * 8 < taskArr.size())
                last_task = page * 8;
            else
                last_task = taskArr.size();
            task_count = taskArr.size();
        }



        if (status == STATUS_DONE)
        {
            for (Task task: taskArr)
            {
                if (Objects.equals(task.getStatus(), STATUS_DONE))
                    task_count++;
            }
            if (page * 8 < task_count)
                last_task = page * 8;
            else
                last_task = task_count;
        }

        if (status == STATUS_IN_WORK)
        {
            for (Task task: taskArr)
            {
                if (Objects.equals(task.getStatus(), STATUS_IN_WORK))
                    task_count++;
            }
            if (page * 8 < task_count)
                last_task = page * 8;
            else
                last_task = task_count;

        }
        int finalLast_task = last_task;


        for (int i = (page - 1) * 8; i < last_task; i++) {                  //цикл добавления задач, проходится по всем задачам

            String taskStatus = taskArr.get(i).getStatus();

            if (status == STATUS_IN_WORK && Objects.equals(taskStatus, STATUS_DONE))
            {
                last_task++;
                continue;
            }


            if (status == STATUS_DONE && Objects.equals(taskStatus, STATUS_IN_WORK))
            {
                last_task++;
                continue;
            }




            //создание кнопки одной задачи
            StackPane oneTaskGroup = getPaneWithText(taskArr.get(i).getName(), SC_HEIGHT / 15, SC_WIDTH / 2, TASK_COLOR);

            final int taskNum = i;

            oneTaskGroup.setOnMouseClicked((MouseEvent click) -> {
                removeAll();
                showOneTask(taskArr.get(taskNum), 0);
            });


            tasks.getChildren().add(new Rectangle(10, 10, BACKGROUND));
            tasks.getChildren().add(oneTaskGroup);

        }


        tasks.setLayoutX(SC_WIDTH / 8); //отступ слева
        tasks.setLayoutY(SC_HEIGHT * 0.1); //отступ сверху

        tasks.getChildren().add(new Rectangle(10, 20, BACKGROUND));

        HBox next_prev_buttons = new HBox();

        StackPane prev = getPaneWithText("prev", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);
        StackPane page_num = getPaneWithText("" + page, (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);
        StackPane next = getPaneWithText("next", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);


        int finalTask_count = task_count;
        next.setOnMouseClicked((MouseEvent click) ->
        {
            if (finalLast_task < finalTask_count)
            {
                removeAll();
                showTasks(status, page + 1);
            }

        });

        prev.setOnMouseClicked((MouseEvent click) ->
        {
            if (page != 1)
            {
                removeAll();
                showTasks(status, page - 1);
            }
        });


        next_prev_buttons.getChildren().addAll(prev,
                new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ),
                page_num,
                new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ),
                next);


        tasks.getChildren().add(next_prev_buttons);
        mainScreen.getChildren().add(tasks);

        VBox rightSide = new VBox();
      
        rightSide.getChildren().add(getPaneWithText(UserBase.getCurrentUser().getName(), (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.WHITE));
        rightSide.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ));

        StackPane logout = getPaneWithText("Выйти 🐾", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);

        logout.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            showLoginPage(false);//
        });

        rightSide.getChildren().add(logout);

        rightSide.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ));
        StackPane recycleBin = getPaneWithText("Корзина",  (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);
        recycleBin.setOnMouseClicked((MouseEvent cl) ->
        {
            removeAll();
            showRecycleBin(1);
        });
        rightSide.getChildren().add(recycleBin);

        rightSide.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.2, BACKGROUND));

        StackPane button = getPaneWithText("+", (int) (SC_HEIGHT*0.1), (int) (SC_HEIGHT*0.1), Color.rgb(161, 219, 136));
        button.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            addNewTask();
        });

        rightSide.getChildren().add(button);

        mainScreen.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND));
        mainScreen.getChildren().add(rightSide);

        mainScreen.setLayoutX(SC_WIDTH*0.1);
        mainScreen.setLayoutY(SC_HEIGHT*0.1);
        root.getChildren().add(mainScreen);
    }


    public static void showRecycleBin(int page)
    {
        HBox mainScreen = new HBox();

        VBox tasks = new VBox();

        tasks.getChildren().add(getPaneWithText("Корзина",  (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER));

        ArrayList<Task> taskArr = DbConnector.getTasks(1);


        int task_count;
        int last_task;
        if (page * 8 < taskArr.size())
            last_task = page * 8;
        else
            last_task = taskArr.size();
        task_count = taskArr.size();

        int finalLast_task = last_task;

        for (int i = (page - 1) * 8; i < last_task; i++) {                  //цикл добавления задач, проходится по всем задачам

            //создание кнопки одной задачи
            StackPane oneTaskGroup = getPaneWithText(taskArr.get(i).getName(), SC_HEIGHT / 15, SC_WIDTH / 2, TASK_COLOR);

            final int taskNum = i;

            oneTaskGroup.setOnMouseClicked((MouseEvent click) -> {
                removeAll();
                showOneTask(taskArr.get(taskNum), 1);
            });


            tasks.getChildren().add(new Rectangle(10, 10, BACKGROUND));
            tasks.getChildren().add(oneTaskGroup);

        }


        tasks.setLayoutX(SC_WIDTH / 8); //отступ слева
        tasks.setLayoutY(SC_HEIGHT * 0.1); //отступ сверху

        tasks.getChildren().add(new Rectangle(10, 20, BACKGROUND));

        HBox next_prev_buttons = new HBox();

        StackPane prev = getPaneWithText("prev", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);
        StackPane page_num = getPaneWithText("" + page, (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);
        StackPane next = getPaneWithText("next", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.LAVENDER);


        int finalTask_count = task_count;
        next.setOnMouseClicked((MouseEvent click) ->
        {
            if (finalLast_task < finalTask_count)
            {
                removeAll();
                showRecycleBin( page + 1);
            }

        });

        prev.setOnMouseClicked((MouseEvent click) ->
        {
            if (page != 1)
            {
                removeAll();
                showRecycleBin(page - 1);
            }
        });


        next_prev_buttons.getChildren().addAll(prev,
                new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ),
                page_num,
                new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ),
                next);


        tasks.getChildren().add(next_prev_buttons);
        mainScreen.getChildren().add(tasks);

        VBox rightSide = new VBox();


        rightSide.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND ));

        StackPane logout = getPaneWithText("Вернуться 🐾", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.15), Color.LAVENDER);

        logout.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            showTasks(STATUS_All);
        });

        rightSide.getChildren().add(logout);


        mainScreen.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.1, BACKGROUND));
        mainScreen.getChildren().add(rightSide);

        mainScreen.setLayoutX(SC_WIDTH*0.1);
        mainScreen.setLayoutY(SC_HEIGHT*0.1);
        root.getChildren().add(mainScreen);
    }

    public static void showOneTask(Task task, int deleted) {
        VBox box = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        HBox nameStatusPriority = new HBox();

        nameStatusPriority.getChildren().add(getPaneWithText(task.getName(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.rgb(255, 213, 171), "Название задачи"));
        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        BorderPane status = new BorderPane();
        if (Objects.equals(task.getStatus(), STATUS_IN_WORK))
        {
            status = getPaneWithText(task.getStatus(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.rgb(238, 204, 255), "Статус, нажмите чтобы выполнить");
            status.setOnMouseClicked((click)->
            {
                DbConnector.setTaskDone(task.getTaskId());
                removeAll();
                showTasks(STATUS_All);
            });
        }
        else
        {
            status = getPaneWithText(task.getStatus(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.rgb(238, 204, 255), "Статус");
        }

        nameStatusPriority.getChildren().add(status);

        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));
        nameStatusPriority.getChildren().add(getPaneWithText(task.getPriority(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.rgb(255, 133, 117), "Приоритет"));

        box.getChildren().add(nameStatusPriority);

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        box.getChildren().add(getPaneWithText(task.getDescription(), SC_HEIGHT / 4, (int) (SC_WIDTH * 0.8), Color.WHITE, "Описание"));

        HBox lowDesc = new HBox();
        VBox people = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));

        people.getChildren().add(getPaneWithText(task.getWorker_str(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Исполнитель"));
        people.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));
        people.getChildren().add(getPaneWithText(task.getReviewer_str(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Проверяющий"));

        lowDesc.getChildren().add(people);
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        VBox deadlineDelete = new VBox();

        deadlineDelete.getChildren().add(getPaneWithText(task.getDeadLine(), SC_HEIGHT / 10, SC_WIDTH / 5, Color.WHITE, "Срок сдачи"));

        StackPane deleteButton;
        if (deleted == 0)
        {
            deleteButton = getPaneWithText("Удалить", SC_HEIGHT / 10, SC_WIDTH / 5, Color.RED);
            deleteButton.setOnMouseClicked((MouseEvent click) ->
            {
                DbConnector.deleteTask(task);
                removeAll();
                showTasks(STATUS_All);
            });
        }
        else
        {
            deleteButton = getPaneWithText("Восстановить", SC_HEIGHT / 10, SC_WIDTH / 5, Color.RED);
            deleteButton.setOnMouseClicked((MouseEvent click) ->
            {
                DbConnector.restoreTask(task);
                removeAll();
                showTasks(STATUS_All);
            });
        }

        deadlineDelete.getChildren().add(new Rectangle(SC_WIDTH * 0.05, SC_HEIGHT * 0.05, BACKGROUND));
        deadlineDelete.getChildren().add(deleteButton);

        lowDesc.getChildren().add(deadlineDelete);

        StackPane button = getPaneWithText("Вернуться 🌹", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.15), Color.rgb(199, 247, 255));
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        button.setOnMouseClicked((MouseEvent click) -> {
            removeAll();
            showTasks(STATUS_All);

        });


        lowDesc.getChildren().add(button);


        box.getChildren().add(lowDesc);

        box.setLayoutY(SC_HEIGHT * 0.05);
        box.setLayoutX(SC_WIDTH * 0.1);

        root.getChildren().add(box);
    }


    public static void showLoginPage(boolean withError)
    {

        BorderPane loginPage = new BorderPane();

        VBox loginFields = new VBox();


        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        BorderPane loginPane = getPaneWithText("", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.4), Color.WHITE, "Логин");
        TextField loginTextField = addFieldToNode(loginPane);
        loginFields.getChildren().add(loginPane);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        BorderPane passwordPane = getPaneWithText("", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.4), Color.WHITE, "Пароль");
        TextField passwordTextField = addFieldToNode(passwordPane);
        loginFields.getChildren().add(passwordPane);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        StackPane loginButton = getPaneWithText("Войти  💖", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.PALETURQUOISE);
        StackPane registerButton = getPaneWithText("Зарегистрироваться 💕", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.PALETURQUOISE);






        registerButton.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            showRegisterPage(false);
        });


        loginButton.setOnMouseClicked((MouseEvent click) ->
                {

                    try {

                        UserBase.setCurrentUser(UserBase.login(loginTextField.getText(), passwordTextField.getText()));
                        removeAll();
                        showTasks(STATUS_All);

                    } catch (NoUserException | FailedLoginException ex) {
                        removeAll();
                        System.out.println(ex);
                        showLoginPage(true);
                    }

                });







        loginFields.getChildren().add(loginButton);
        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));
        loginFields.getChildren().add(registerButton);
        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        if (withError)
        {
            loginFields.getChildren().add(getPaneWithText("Ошибка входа", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.LIGHTCORAL));
        }


        loginPage.setCenter(loginFields);
        loginPage.setLeft(new Rectangle(SC_WIDTH*0.35, SC_HEIGHT*0.1, BACKGROUND));

        root.getChildren().add(loginPage);
    }

    public static void showRegisterPage(boolean withError)
    {

        BorderPane loginPage = new BorderPane();

        VBox loginFields = new VBox();


        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        BorderPane loginPane = getPaneWithText("", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.4), Color.WHITE, "Логин");
        TextField loginTextField = addFieldToNode(loginPane);
        loginFields.getChildren().add(loginPane);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        BorderPane passwordPane = getPaneWithText("", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.4), Color.WHITE, "Пароль");
        TextField passwordTextField = addFieldToNode(passwordPane);
        loginFields.getChildren().add(passwordPane);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));

        BorderPane passwordRepeatPane = getPaneWithText("", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.4), Color.WHITE, "Повторите Пароль");
        TextField passwordRepeatTextField = addFieldToNode(passwordRepeatPane);
        loginFields.getChildren().add(passwordRepeatPane);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));


        StackPane registerButton = getPaneWithText("Зарегистрироваться 🍒", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.PALETURQUOISE);



        registerButton.setOnMouseClicked((MouseEvent click) ->
        {

            if (Objects.equals(passwordRepeatTextField.getText(), passwordTextField.getText()))
            {
                try
                {
                    User newUser = DbConnector.addUser(loginTextField.getText(), passwordTextField.getText());
                    UserBase.setCurrentUser(newUser);
                    removeAll();
                    showTasks(STATUS_All);
                }
                catch (SQLException ex)
                {
                    removeAll();
                    System.out.println(ex.getMessage());
                    showRegisterPage(true);
                }
            }
            else
            {
                removeAll();
                showRegisterPage(true);
            }

        });







        
        //loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.1, BACKGROUND));
        loginFields.getChildren().add(registerButton);
        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.05, BACKGROUND));

        StackPane backButton = getPaneWithText("Назад", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.PALETURQUOISE);

        backButton.setOnMouseClicked((MouseEvent click) ->
        {
            removeAll();
            showLoginPage(false);
        });


        loginFields.getChildren().add(backButton);

        loginFields.getChildren().add(new Rectangle(SC_WIDTH*0.3, SC_HEIGHT*0.05, BACKGROUND));

        if (withError)
        {
            loginFields.getChildren().add(getPaneWithText("Ошибка регистрации", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.LIGHTCORAL));
        }


        loginPage.setCenter(loginFields);
        loginPage.setLeft(new Rectangle(SC_WIDTH*0.35, SC_HEIGHT*0.1, BACKGROUND));

        root.getChildren().add(loginPage);
    }




    //----------------------PRIVATE METHODS---------------------------------

    private static void addTaskTypes(HBox taskTypes, String status) {


        StackPane actualPane = new StackPane();
        StackPane finishedPane = new StackPane();
        StackPane allPane = new StackPane();


        switch (status)
        {
            case STATUS_All:
                actualPane = getPaneWithText("Актуальные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                finishedPane = getPaneWithText("Завершенные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                allPane = getPaneWithText("Все", SC_HEIGHT / 17, SC_WIDTH / 6, TYPE_IN_FOCUS);
                break;

            case STATUS_DONE:
                actualPane = getPaneWithText("Актуальные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                finishedPane = getPaneWithText("Завершенные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPE_IN_FOCUS);
                allPane = getPaneWithText("Все", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                break;

            case STATUS_IN_WORK:
                actualPane = getPaneWithText("Актуальные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPE_IN_FOCUS);
                finishedPane = getPaneWithText("Завершенные", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                allPane = getPaneWithText("Все", SC_HEIGHT / 17, SC_WIDTH / 6, TYPES_NOT_IN_FOCUS);
                break;

        }

        actualPane.setOnMouseClicked((MouseEvent click) -> {
            removeAll();
            showTasks(STATUS_IN_WORK);

        });

        finishedPane.setOnMouseClicked((MouseEvent click) -> {
            removeAll();
            showTasks(STATUS_DONE);
        });

        allPane.setOnMouseClicked((MouseEvent click) -> {
            removeAll();
            showTasks(STATUS_All);
        });


        taskTypes.getChildren().addAll(
                actualPane,
                new Rectangle(10, 10, BACKGROUND),
                finishedPane,
                new Rectangle(10, 10, BACKGROUND),
                allPane);
    }


    private static ComboBox<String> addComboBox(BorderPane node, String[] list)
    {
        ObservableList<String> observableList = FXCollections.observableArrayList(list);

        ComboBox<String> comboBox = new ComboBox<>(observableList);
        comboBox.setValue(list[0]);
        node.setCenter(comboBox);

        return comboBox;
    }


    private static TextField addFieldToNode(BorderPane node)
    {
        TextField txtField = new TextField();
        node.setCenter(txtField);
        return txtField;
    }

    private static TextArea addAreaToNode(BorderPane node)
    {
        TextArea txtArea = new TextArea();
        node.setCenter(txtArea);
        return txtArea;
    }
    //перегрузка (без подписи)
    private static StackPane getPaneWithText(String text, int height, int width, Color color) {
        StackPane pane = new StackPane();

        Rectangle rect = new Rectangle();


        Text txt = new Text(text);

        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(new Font("Times New Roman", 30));

        rect.setX(0);
        rect.setY(0);

        rect.setArcHeight(15);
        rect.setArcWidth(15);

        rect.setWidth(width);
        rect.setHeight(height);
        rect.setFill(color);

        rect.setStyle(" -fx-stroke: lightgrey; -fx-stroke-width: 1;");


        pane.getChildren().add(rect);
        pane.getChildren().add(txt);


        return pane;
    }


    //Перегрузка (С описанием)
    private static BorderPane getPaneWithText(String text, int height, int width, Color color, String label) {

        BorderPane borderPane = new BorderPane();

        StackPane pane = getPaneWithText(text, height, width, color);

        Label lbl = new Label(label);
        lbl.setFont(new Font("Times New Roman", 20));
        borderPane.setCenter(pane);
        borderPane.setTop(lbl);

        return borderPane;
    }


    private static void removeAll() {
        root.getChildren().removeAll(root.getChildren());
    }
}
