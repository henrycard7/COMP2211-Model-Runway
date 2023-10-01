package runwaytool.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import runwaytool.model.AirportParameters;
import runwaytool.model.RunwayStripParameters;
import runwaytool.view.Window;

/**
 * The generic scene which is extended by the actual used scenes; handles all of the common
 * functionality between them.
 */
public abstract class GenericScene {

    protected final Window window;

    protected BorderPane root;
    protected Scene scene;

    /**
     * Create a new scene, passing the Window used to display it in
     */
    public GenericScene(Window window) { this.window = window; }

    public abstract void initialise();

    public abstract void build();

    public Scene makeScene() {
        var previous = window.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        this.scene = scene;
        return scene;
    }

    public Scene getScene() { return this.scene; }

    public void updateUI(AirportParameters aParams){}

    public void updateUIRecalculations(RunwayStripParameters recalculatedRunwayStripParameters){
        /*
        TODO: in SideScene and TopScene, take params from this argument and display them in the scene as appropriate
         */

    }
}
