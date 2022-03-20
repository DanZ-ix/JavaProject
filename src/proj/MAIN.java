package proj;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class MAIN extends Application {

    public static Group root = new Group();

    //region constants
    public final static String STATUS_DONE = "Выполнено";
    public final static String STATUS_IN_WORK = "В работе";
    public final static String STATUS_All= "Все";

    public final static  String[] priorities = new String[]{"Самый низкий", "Низкий", "Средний", "Высокий", "Самый высокий", "Критический"};


    public final static int SC_WIDTH = 1600;
    public final static int SC_HEIGHT = 900;
    public final static Color BACKGROUND = Color.MISTYROSE;

    //endregion



    @Override
    public void start(Stage primaryStage) {

        UserBase.createSomeUsers();
        BackLog.createTasks();

        Navigation.showLoginPage(false);



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




