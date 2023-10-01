package runwaytool.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import runwaytool.component.NotificationBox;
import runwaytool.component.SidebarTabPane;
import runwaytool.controller.Controller;
import runwaytool.model.AirportParameters;
import runwaytool.model.Obstacle;
import runwaytool.model.RunwayStripParameters;
import runwaytool.scene.GenericScene;
import runwaytool.scene.SideScene;
import runwaytool.scene.TopScene;

public class Window {
    private Controller controller;
    private static final Logger logger = LogManager.getLogger(Window.class);
    private int width;
    private int height;
    private final Stage stage;
    private Scene scene;
    private GenericScene currentScene;
    private TopScene topscene;
    private SideScene sidescene;
    private SidebarTabPane sidebarTabPane;
    private NotificationBox notificationBox;

    public Window(Stage stage, int width, int height, Controller controller) {
        this.controller = controller;
        this.width = width;
        this.height = height;
        this.stage = stage;

        setupStage();

        this.scene = new Scene(new Pane(), width, height);
        stage.setScene(this.scene);
        stage.setMaximized(false);
        stage.setResizable(true);
        sidebarTabPane = new SidebarTabPane(controller);
        Button threeDimensional = new Button("3D-view");
        threeDimensional.setPrefSize(getWidth()*0.09,getHeight()*0.09);
        threeDimensional.setOnAction(actionEvent -> {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            logger.info("Opening 3D visualisation");

            boolean revertToTop = false;
            if (currentScene == topscene) {
                revertToTop = true;
            }
            startTop();
            Image topImage = topscene.getSnapshot();
            int topDownWidth = (int) topscene.getTopDownWidth();
            int topDownHeight = (int) topscene.getTopDownHeight();
            startSide();
            Image sideImage = sidescene.getSnapshot();
            int sideWidth = (int) sidescene.getSideWidth();
            int sideHeight = (int) sidescene.getSideHeight();
            if (revertToTop) {
                startTop();
            }

            WritableImage newTopImage = new WritableImage(topImage.getPixelReader(), 0, 0, topDownWidth, topDownHeight);
            PixelReader reader = sideImage.getPixelReader();
            WritableImage newSideImage = new WritableImage(reader, 0, 0, sideWidth, sideHeight);
            PixelWriter writer = newSideImage.getPixelWriter();
            for (int x = 0; x < sideWidth; x++) {
                for (int y = 0; y < sideHeight; y++) {
                    Color pixelC = reader.getColor(x, y);
                    Color newCol = new Color(pixelC.getRed(), pixelC.getGreen(), pixelC.getBlue(), 0.5f);
                    writer.setColor(x, y, newCol);
                }
            }

            var window = new ThreeDimensionalWindow(newStage,(2*(topDownWidth/topDownHeight)),2,controller,newTopImage, newSideImage);
            newStage.close();
            newStage.show();

            newStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        newStage.close();
                    }
            });
        });
        notificationBox = new NotificationBox();
        topscene = new TopScene(this, controller, sidebarTabPane, threeDimensional, notificationBox);
        sidescene = new SideScene(this, controller, sidebarTabPane, threeDimensional, notificationBox);
        startSide();//Needs to be called twice so UI can be updated even when not shown
        startTop();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Loads fonts and other resources
     */
    private void loadResources() {

    }

    public void setupStage() {
        stage.setTitle("Runway Re-declaration Tool");
        stage.setMinWidth(width);
        stage.setMinHeight(height + 20);
    }

    public void loadScene(GenericScene newScene) {
        newScene.build();
        currentScene = newScene;
        scene = newScene.makeScene();
        stage.setScene(scene);

        Platform.runLater(() -> currentScene.initialise());
    }

    public void startTop() {
        loadScene(topscene);
    }

    public void startSide() {
        loadScene(sidescene);
    }

    public void sendValuesToSidebar(AirportParameters aParams) {
        sidebarTabPane.setValues(aParams);
    }
    public void sendValuesToSidebar(Obstacle obstacle) {
        sidebarTabPane.setOValues(obstacle);
    }

    public void updateUI(AirportParameters aParams) {
        sidescene.updateUI(aParams);
        topscene.updateUI(aParams);
    }

    public void updateUIRecalculations(RunwayStripParameters recalculatedRunwayStripParameters){
        sidescene.updateUIRecalculations(recalculatedRunwayStripParameters);
        topscene.updateUIRecalculations(recalculatedRunwayStripParameters);
    }

    public void sendNotification(String message) {
        notificationBox.sendMessage(message);
    }
}