package runwaytool;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import runwaytool.controller.Controller;
import runwaytool.view.Window;


public class App extends Application {

    private int width = 1366; //1920
    private int height = 844; //1080

    private static App instance;

    private static final Logger logger = LogManager.getLogger(App.class);

    private Stage stage;

    private Controller controller;

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;
        this.stage.setResizable(false);

        controller = new Controller();
        System.setProperty("java.io.tmpdir", "/java");

        openWindow();
    }

    public void openWindow(){
        logger.info("Opening window");

        var window = new Window(stage,width,height, controller);
        controller.window = window;
        stage.show();
    }

    public static void main(String[] args) {
        logger.info("Client started");
        launch();
    }
}