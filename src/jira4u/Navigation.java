package jira4u;


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
import jira4u.*;

import static jira4u.Jira4U.*;

public class Navigation {

    private static final Color TYPES_NOT_IN_FOCUS = Color.LIGHTBLUE;
    private static final Color TYPE_IN_FOCUS = Color.INDIANRED;
    public final static Color TASK_COLOR = Color.WHITE;

    public static void addNewTask()
    {
        VBox box = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        HBox nameStatusPriority = new HBox();


        BorderPane namePane = getPaneWithText("", SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGE, "Название задачи");
        TextField nameTextField = addFieldToNode(namePane);
        nameStatusPriority.getChildren().add(namePane);

        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));
        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        BorderPane priorityPane = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGERED, "Приоритет");
        ComboBox<String> priorityComboBox = addComboBox(priorityPane, priorities);
        nameStatusPriority.getChildren().add(priorityPane);



        box.getChildren().add(nameStatusPriority);
        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));



        BorderPane descPane = getPaneWithText(" ", SC_HEIGHT / 4, (int) (SC_WIDTH * 0.8), Color.WHITE, "Описание");
        TextArea descTextArea = addAreaToNode(descPane);
        box.getChildren().add(descPane);

        HBox lowDesc = new HBox();
        VBox people = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));


        BorderPane worker = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Исполнитель");
        ComboBox<String> workerCombobox = addComboBox(worker, UserBase.getAllUserNames());
        people.getChildren().add(worker);



        people.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));

        BorderPane reviewer = getPaneWithText(" ", SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Проверяющий");
        ComboBox<String> reviewerComboBox = addComboBox(reviewer, UserBase.getAllUserNames());
        people.getChildren().add(reviewer);

        lowDesc.getChildren().add(people);
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        BorderPane deadLine = getPaneWithText(" ", SC_HEIGHT / 7, SC_WIDTH / 5, Color.WHITE, "Срок сдачи");
        TextField deadLineTxtField = addFieldToNode(deadLine);
        lowDesc.getChildren().add(deadLine);


        StackPane button = getPaneWithText("Добавить", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.GREEN);
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        button.setOnMouseClicked((MouseEvent click) -> {

            User workerUser = new User("", "", "", Access.LOW);
            User reviewerUser = workerUser;

            try
            {
                reviewerUser = UserBase.getUserByName(reviewerComboBox.getValue());
            }
            catch (NoUserException ex)
            {
                reviewerUser = new User("", "", ex.getMessage(), Access.LOW);
            }

            try
            {
                workerUser = UserBase.getUserByName(workerCombobox.getValue());
            }
            catch (NoUserException ex)
            {
                workerUser = new User("", "", ex.getMessage(), Access.LOW);
            }

            Task newTask = new Task(
                    nameTextField.getText(),
                    priorityComboBox.getValue(),
                    descTextArea.getText(),
                    deadLineTxtField.getText(),
                    workerUser,
                    reviewerUser
            );

            BackLog.addTask(newTask);


            removeAll();
            showTasks(STATUS_All);

        });


        lowDesc.getChildren().add(button);


        box.getChildren().add(lowDesc);

        box.setLayoutY(SC_HEIGHT * 0.05);
        box.setLayoutX(SC_WIDTH * 0.1);

        root.getChildren().add(box);
    }


    public static ComboBox<String> addComboBox(BorderPane node, String[] list)
    {
        ObservableList<String> observableList = FXCollections.observableArrayList(list);

        ComboBox<String> comboBox = new ComboBox<String>(observableList);
        comboBox.setValue(list[0]);
        node.setCenter(comboBox);

        return comboBox;
    }


    public static TextField addFieldToNode(BorderPane node)
    {
        TextField txtField = new TextField();
        node.setCenter(txtField);
        return txtField;
    }

    public static TextArea addAreaToNode(BorderPane node)
    {
        TextArea txtArea = new TextArea();
        node.setCenter(txtArea);
        return txtArea;
    }

    public static void showTasks(String status) {


        HBox mainScreen = new HBox();

        VBox tasks = new VBox();

        HBox taskTypes = new HBox();

        taskTypes.getChildren().add(new Rectangle(15, 15, BACKGROUND));


        addTaskTypes(taskTypes, status);

        tasks.getChildren().add(taskTypes);


        for (int i = 0; i < BackLog.getLength(); i++) {
            if (status == STATUS_IN_WORK && BackLog.getTask(i).getStatus() == STATUS_DONE)
                continue;

            if (status == STATUS_DONE && BackLog.getTask(i).getStatus() == STATUS_IN_WORK)
                continue;


            StackPane oneTaskGroup = getPaneWithText(BackLog.getTask(i).getName(), SC_HEIGHT / 15, SC_WIDTH / 2, TASK_COLOR);

            final int taskNum = i;

            oneTaskGroup.setOnMouseClicked((MouseEvent click) -> {
                removeAll();
                showOneTask(BackLog.getTask(taskNum));
            });


            tasks.getChildren().add(new Rectangle(10, 10, BACKGROUND));
            tasks.getChildren().add(oneTaskGroup);

        }


        tasks.setLayoutX(SC_WIDTH / 8);
        tasks.setLayoutY(SC_HEIGHT * 0.1);

        mainScreen.getChildren().add(tasks);

        VBox rightSide = new VBox();

        rightSide.getChildren().add(getPaneWithText("Имя пользователя", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.2), Color.WHITE));

        rightSide.getChildren().add(new Rectangle(SC_WIDTH*0.1, SC_HEIGHT*0.6, BACKGROUND));

        StackPane button = getPaneWithText("+", (int) (SC_HEIGHT*0.1), (int) (SC_HEIGHT*0.1), Color.GREEN);
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


    public static void showOneTask(Task task) {
        VBox box = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        HBox nameStatusPriority = new HBox();

        nameStatusPriority.getChildren().add(getPaneWithText(task.getName(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGE, "Название задачи"));
        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));


        BorderPane status = new BorderPane();
        if (task.getStatus() == STATUS_IN_WORK)
        {
            status = getPaneWithText(task.getStatus(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.VIOLET, "Статус, нажмите чтобы выполнить");
            status.setOnMouseClicked((click)->
            {
                task.setDone();
                removeAll();
                showTasks(STATUS_All);
            });
        }
        else
        {
            status = getPaneWithText(task.getStatus(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.VIOLET, "Статус");
        }

        nameStatusPriority.getChildren().add(status);

        nameStatusPriority.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));
        nameStatusPriority.getChildren().add(getPaneWithText(task.getPriority(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.ORANGERED, "Приоритет"));

        box.getChildren().add(nameStatusPriority);

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));

        box.getChildren().add(getPaneWithText(task.getDescription(), SC_HEIGHT / 4, (int) (SC_WIDTH * 0.8), Color.WHITE, "Описание"));

        HBox lowDesc = new HBox();
        VBox people = new VBox();

        box.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));

        people.getChildren().add(getPaneWithText(task.getWorker().getName(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Исполнитель"));
        people.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.05, BACKGROUND));
        people.getChildren().add(getPaneWithText(task.getReviewer().getName(), SC_HEIGHT / 15, SC_WIDTH / 5, Color.WHITE, "Проверяющий"));

        lowDesc.getChildren().add(people);
        lowDesc.getChildren().add(new Rectangle(SC_WIDTH * 0.1, SC_HEIGHT * 0.1, BACKGROUND));
        lowDesc.getChildren().add(getPaneWithText(task.getDeadLine(), SC_HEIGHT / 7, SC_WIDTH / 5, Color.WHITE, "Срок сдачи"));


        StackPane button = getPaneWithText("Вернуться", (int) (SC_HEIGHT*0.1), (int) (SC_WIDTH*0.1), Color.GREEN);
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


    //Перегрузка (Без подписи)
    public static StackPane getPaneWithText(String text, int height, int width, Color color) {
        StackPane pane = new StackPane();

        Rectangle rect = new Rectangle();


        Text txt = new Text(text);

        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(new Font(30));

        rect.setX(0);
        rect.setY(0);

        rect.setArcHeight(15);
        rect.setArcWidth(15);

        rect.setWidth(width);
        rect.setHeight(height);
        rect.setFill(color);

        rect.setStyle(" -fx-stroke: black; -fx-stroke-width: 2;");


        pane.getChildren().add(rect);
        pane.getChildren().add(txt);


        return pane;
    }


    //Перегрузка (С описанием)
    public static BorderPane getPaneWithText(String text, int height, int width, Color color, String label) {

        BorderPane borderPane = new BorderPane();

        StackPane pane = getPaneWithText(text, height, width, color);

        Label lbl = new Label(label);

        borderPane.setCenter(pane);
        borderPane.setTop(lbl);

        return borderPane;
    }


    public static void removeAll() {
        root.getChildren().removeAll(root.getChildren());
    }
}
