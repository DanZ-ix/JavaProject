package jira4u;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Jira4U extends Application {

    public static Group root = new Group();

    //region constants
    public final static String STATUS_DONE = "Выполнено";
    public final static String STATUS_IN_WORK = "В работе";
    public final static String STATUS_All= "Все";

    public final static  String[] priorities = new String[]{"Самый низкий", "Низкий", "Средний", "Высокий", "Самый высокий", "Критический"};


    public final static int SC_WIDTH = 1600;
    public final static int SC_HEIGHT = 900;
    public final static Color BACKGROUND = Color.AQUA;
    //endregion



    @Override
    public void start(Stage primaryStage) {

        BackLog.createTasks();
        Navigation.showTasks(STATUS_All);


        Scene scene = new Scene(root, SC_WIDTH, SC_HEIGHT);
        scene.setFill(BACKGROUND);




        primaryStage.setTitle("Jira4U");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }



}




