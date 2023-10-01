package runwaytool.scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import runwaytool.component.*;
import runwaytool.controller.Controller;
import runwaytool.model.*;
import runwaytool.view.ThreeDimensionalWindow;
import runwaytool.view.Window;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.ArrayList;
import java.util.Locale;

public class SideScene extends GenericScene{
    private final Controller controller;
    private static final Logger logger = LogManager.getLogger(SideScene.class);
    RunwayStripParameters baseParameters;
    private final SidebarTabPane inputs;
    private boolean initialLoad = true;
    private final Text[] calculationsText = new Text[4];
    private StackPane combinedComponent;
    private Button threeDimensional;
    private float sideWidth;
    private float sideHeight;
    private StackPane arrowPane;
    private NotificationBox notificationBox;

    /**
     * Create a new scene, passing the Window used to display it in
     *
     * @param window
     */
    public SideScene(Window window, Controller controller, SidebarTabPane sidebarTabPane, Button threeDimensional, NotificationBox notiBox) {
        super(window);
        this.controller = controller;
        //this.inputs = new SidebarTabPane(controller);
        this.inputs = sidebarTabPane;
        this.inputs.getStyleClass().add("menuItem");
        this.threeDimensional = threeDimensional;
        sideWidth = 0.5f * window.getWidth() * 1.08f;
        sideHeight = 0.4f * window.getHeight() * 1.08f;
        this.notificationBox = notiBox;
        initialise();
    }

    @Override
    public void initialise() {
        if (initialLoad) {
            ArrayList<RunwayParameters> baseParametersList = new ArrayList<>();
            baseParametersList.add(0, new RunwayParameters(new Designator(9, DesignatorSuffix.L),
                    3902f, 3902f, 3902f, 3902f, 0f));
            baseParametersList.add(1, new RunwayParameters(new Designator(9, DesignatorSuffix.L),
                    3902f, 3902f, 3902f, 3902f, 0f));
            baseParameters = new RunwayStripParameters("Default", 3902, baseParametersList, 50f, 240f,
                    150f, 60f, 60f, 60f);
        }
    }

    private StackPane createArrows(SideComponent sideComponent, float width, float height, int index){

        float length = controller.getActiveRunwayStrip().getLength();
        float stopway = controller.getActiveRunwayStrip().getStopway();
        float clearway = controller.getActiveRunwayStrip().getClearway();
        float ALS = controller.getActiveRunwayStrip().getALS();
        float RESAWidth = controller.getActiveRunwayStrip().getRESAWidth();
        float stripend = controller.getActiveRunwayStrip().getStripEnd();
        float slopeCalculation = 0;

        float TORA1offset = stripend;
        float TODA1offset = stripend;
        float ASDA1offset = stripend;
        float LDA1offset = stripend;
        float TORA2offset = stripend + length;
        float TODA2offset = stripend + length;
        float ASDA2offset = stripend + length;
        float LDA2offset = stripend + length;

        float blastProtection = 0;
        float obstacleHeight = 0;

        if(controller.getActiveObstacle() != null) {
            float distance1 = controller.getActiveObstacle().getDistance0f();
            float distance2 = controller.getActiveObstacle().getDistance1f();
            blastProtection = controller.getActiveObstacle().getBlastAllowance();
            obstacleHeight = controller.getActiveObstacle().getHeight();
            slopeCalculation = obstacleHeight * ALS;

            System.out.println("RESA: " + RESAWidth);
            System.out.println("Slope: " + slopeCalculation);
            System.out.println("Blast: " + blastProtection);

            if(distance1 <= distance2) {    //closer to "left" end
                LDA1offset = distance1 + stripend + Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation));
                TORA1offset = stripend + distance1 + blastProtection;
                TODA1offset = stripend + distance1 + blastProtection;
                ASDA1offset = stripend + distance1 + blastProtection;
            }
            else {
                LDA2offset = stripend + length - distance2 - Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation));
                TORA2offset = stripend + length - distance2 - blastProtection;
                TODA2offset = stripend + length - distance2 - blastProtection;
                ASDA2offset = stripend + length - distance2 - blastProtection;
            }
        }

        StackPane arrowPane = new StackPane();

        float rightStart = sideComponent.getXPlacement(sideComponent.getLength());

        if(index == 0) {
            Arrow TODA1 = new Arrow(sideComponent.getXPlacement(TODA1offset), sideComponent.getHeight()*0.75f, sideComponent.getXPlacement(TODA1offset + sideComponent.getTODA()[0]),
                    sideComponent.getHeight()*0.75f, "TODA :" + sideComponent.getTODA()[0],Boolean.TRUE,Boolean.FALSE);

            Arrow ASDA1 = new Arrow(sideComponent.getXPlacement(ASDA1offset), sideComponent.getHeight()*0.8f, sideComponent.getXPlacement(ASDA1offset + sideComponent.getASDA()[0]),
                    sideComponent.getHeight()*0.8f, "ASDA :" + sideComponent.getASDA()[0],Boolean.TRUE,Boolean.FALSE);

            Arrow TORA1 = new Arrow(sideComponent.getXPlacement(TORA1offset), sideComponent.getHeight()*0.85f, sideComponent.getXPlacement(TORA1offset + sideComponent.getTORA()[0]),
                    sideComponent.getHeight()*0.85f, "TORA: " + sideComponent.getTORA()[0],Boolean.TRUE,Boolean.FALSE);

            Arrow LDA1 = new Arrow(sideComponent.getXPlacement(LDA1offset),sideComponent.getHeight()*0.9f, sideComponent.getXPlacement(LDA1offset+sideComponent.getLDA()[0])
                    , sideComponent.getHeight()*0.9f,"LDA: " + sideComponent.getLDA()[0], Boolean.TRUE, Boolean.FALSE);

            arrowPane.getChildren().addAll(TODA1,ASDA1,TORA1,LDA1);
        } else {
            Arrow TODA1 = new Arrow(sideComponent.getXPlacement(TODA2offset), sideComponent.getHeight()*0.75f, sideComponent.getXPlacement(TODA2offset-sideComponent.getTODA()[1]),
                    sideComponent.getHeight()*0.75f, "TODA :" + sideComponent.getTODA()[1],Boolean.TRUE,Boolean.FALSE);

            Arrow ASDA1 = new Arrow(sideComponent.getXPlacement(ASDA2offset), sideComponent.getHeight()*0.8f, sideComponent.getXPlacement(ASDA2offset-sideComponent.getASDA()[1]),
                    sideComponent.getHeight()*0.8f, "ASDA :" + sideComponent.getASDA()[1],Boolean.TRUE,Boolean.FALSE);

            Arrow TORA1 = new Arrow(sideComponent.getXPlacement(TORA2offset), sideComponent.getHeight()*0.85f, sideComponent.getXPlacement(TORA2offset-sideComponent.getTORA()[1]),
                    sideComponent.getHeight()*0.85f, "TORA: " + sideComponent.getTORA()[1],Boolean.TRUE,Boolean.FALSE);

            Arrow LDA1 = new Arrow(sideComponent.getXPlacement(LDA2offset),sideComponent.getHeight()*0.9f, sideComponent.getXPlacement(LDA2offset-sideComponent.getLDA()[1]),
                    sideComponent.getHeight()*0.9f,"LDA: " + sideComponent.getLDA()[1], Boolean.TRUE, Boolean.FALSE);

            //System.out.println(rightStart-sideComponent.getXPlacement(baseParameters.runwayParametersList.get(1).displacedThreshold-sideComponent.getLDA()[1]));
            arrowPane.getChildren().addAll(TODA1,ASDA1,TORA1,LDA1);
        }



        return arrowPane;
    }


    private StackPane makeObstacle(SideComponent sideComponent, float objectHeight, float windowWidth, float windowHeight, float distanceL
            , float distanceR, float width, float length) {
        StackPane obstaclePane = new StackPane();
        StackPane.setMargin(obstaclePane, new Insets(0,0,0,0));

        float startX = sideComponent.getXPlacement(sideComponent.getStripend() + distanceL);
        float startY = windowHeight*0.65f - windowHeight*0.3f;

        Pane obstacle = new Pane();
        Rectangle object = new Rectangle(startX, startY, sideComponent.getXPlacement(length), windowHeight*0.3f);
        object.setFill(Color.RED);
        obstacle.getChildren().add(object);

        obstaclePane.setPrefSize(windowWidth,windowHeight);
        obstaclePane.setMaxSize(windowWidth,windowHeight);
        obstaclePane.getChildren().add(obstacle);

        return obstaclePane;
    }

    private StackPane makeSlope(SideComponent sideComponent, float objectHeight, float windowWidth, float windowHeight, float distanceL
            , float distanceR, float width, float length, int index) {
        StackPane slopePane = new StackPane();
        StackPane.setMargin(slopePane, new Insets(0,0,0,0));

        float stripend = controller.getActiveRunwayStrip().getStripEnd();
        float LDAoffset = stripend;
        float blastProtection = 0;
        float slopeCalculation = 0;
        float slopeCalculation2 = 0;
        float ALS = controller.getActiveRunwayStrip().getALS();
        float TOCS = controller.getActiveRunwayStrip().getTOCS();
        float startX = 0f;
        float allLength = controller.getActiveRunwayStrip().getLength();
        if(controller.getActiveObstacle() != null) {
            blastProtection = controller.getActiveObstacle().getBlastAllowance();
            slopeCalculation = objectHeight * ALS;
            slopeCalculation2 = objectHeight * TOCS;

            startX = sideComponent.getXPlacement(sideComponent.getStripend() + distanceL);
            if (distanceL <= distanceR && index == 0) {
                LDAoffset = stripend + distanceL + Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(), slopeCalculation));
            } else if (distanceL > distanceR && index == 0)  {
                LDAoffset = distanceL - slopeCalculation2;
            } else if (distanceL <= distanceR && index == 1) {
                LDAoffset = distanceL + slopeCalculation2+stripend+stripend;
            } else {
                LDAoffset = stripend + distanceL - Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(), slopeCalculation));
            }
        }

        float startY = windowHeight*0.65f - windowHeight*0.3f;

        Pane slope = new Pane();
        Line object = new Line(startX, startY, sideComponent.getXPlacement(LDAoffset), startY+windowHeight*0.3f);
        Arrow RESA = new Arrow(startX, startY+windowHeight*0.3f, sideComponent.getXPlacement(LDAoffset), startY+windowHeight*0.3f,String.valueOf(Math.abs(LDAoffset-stripend-distanceL)),false,true);
        object.setStroke(Color.RED);
        object.getStrokeDashArray().addAll(25d, 10d);
        slope.getChildren().addAll(object,RESA);

        slopePane.setPrefSize(windowWidth,windowHeight);
        slopePane.setMaxSize(windowWidth,windowHeight);
        slopePane.getChildren().add(slope);

        return slopePane;
    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new BorderPane();
        root.setPadding(new Insets(50,50,30,50));
        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        StackPane slope = new StackPane();
        SideComponent sideComponent = null;
        sideWidth = 0.5f * window.getWidth() * 1.08f;
        sideHeight = 0.4f * window.getHeight() * 1.08f;

        if(obstacle != null) {
            try {
                sideComponent = new SideComponent(sideWidth,sideHeight,controller.recalculateRunwayStripParameters());
                sideComponent.paint();
                obstaclePane = makeObstacle(sideComponent,obstacle.getDistanceFromCentre(),sideWidth,sideHeight,
                        obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength());
                    slope = makeSlope(sideComponent, obstacle.getHeight()
                            , sideWidth, sideHeight,obstacle.getDistance0f(),obstacle.getDistance1f(),obstacle.getWidth(),obstacle.getLength(),
                            controller.getLogicalRunwayIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sideComponent = new SideComponent(sideWidth,sideHeight,baseParameters);
            sideComponent.paint();
        }

        //Background Colour
        Background background = new Background(new BackgroundFill(Color.LIGHTBLUE, null, null));
        root.setBackground(background);

        arrowPane = new StackPane();
        arrowPane = createArrows(sideComponent,sideWidth,sideHeight,controller.getLogicalRunwayIndex());

        combinedComponent = new StackPane();
        combinedComponent.setPrefSize(sideWidth,sideHeight);
        combinedComponent.setMaxSize(sideWidth,sideHeight);
        combinedComponent.getChildren().addAll(sideComponent,obstaclePane,slope,arrowPane);
        combinedComponent.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Text takeOffDirectionLabel = new Text(" Direction:");
        takeOffDirectionLabel.getStyleClass().add("heading");
        TakeOffDirectionComponent takeOff = new TakeOffDirectionComponent(100,60, controller.getLogicalRunwayIndex(), false);

        VBox takeOffDirection = new VBox(takeOffDirectionLabel,takeOff);
        takeOffDirection.setStyle("-fx-background-color: lightGrey;");
        takeOffDirection.getStyleClass().add("boxBackground");
        takeOffDirection.setMaxHeight(80);
        takeOffDirection.setMaxWidth(80);
        takeOffDirection.setOnMouseEntered(mouseEvent -> {
            takeOff.clear();
            takeOff.paintHover();
        });
        takeOffDirection.setOnMouseExited(mouseEvent -> {
            takeOff.clear();
            takeOff.paint();
        });
        takeOffDirection.setOnMouseClicked(mouseEvent -> {
            takeOff.clear();
            takeOff.facingLeft = !takeOff.facingLeft;
            takeOff.paintHover();

            int newIndex = controller.incrementLogicalRunwayIndex();
            controller.setActiveLogicalRunway(controller.getActiveRunwayStrip().getLogicalRunways().get(newIndex));
            controller.updateUI(inputs.getCurrentParameters());

        });

        Button todaAudio = new Button("Play TODA");
        todaAudio.setOnAction(event -> {
            System.out.println("queue audio");
//            String[] calculationsSplit = controller.getCalculationText().lines().toArray(String[]::new);
            String[] calcbox = calculationsText[1].getText().lines().toArray(String[]::new);

            if(calcbox.length>0){
                try{
//                    System.out.println(calculationsSplit[5]);
                    playTTS(calcbox[2]);
                }catch (Exception e){
                    System.out.println("audio failed");
                }

            }
        });
        Button toraAudio = new Button("Play TORA");
        toraAudio.setOnAction(event -> {
            System.out.println("queue audio");
//            String[] calculationsSplit = controller.getCalculationText().lines().toArray(String[]::new);
            String[] calcbox = calculationsText[0].getText().lines().toArray(String[]::new);

            if(calcbox.length>0){
                try{
//                    System.out.println(calculationsSplit[2]);
                    playTTS(calcbox[2]);
                }catch (Exception e){
                    System.out.println("audio failed");
                }

            }
        });
        Button asdaAudio = new Button("Play ASDA");
        asdaAudio.setOnAction(event -> {
            System.out.println("queue audio");
            String[] calcbox = calculationsText[2].getText().lines().toArray(String[]::new);
//            String[] calculationsSplit = controller.getCalculationText().lines().toArray(String[]::new);

            if(calcbox.length>0){
                try{
//                    System.out.println(calculationsSplit[8]);
                    playTTS(calcbox[2]);
                }catch (Exception e){
                    System.out.println("audio failed");
                }

            }
        });
        Button ldaAudio = new Button("Play LDA");
        ldaAudio.setOnAction(event -> {
            System.out.println("queue audio");
            String[] calcbox = calculationsText[3].getText().lines().toArray(String[]::new);
//            String[] calculationsSplit = controller.getCalculationText().lines().toArray(String[]::new);

            if(calcbox.length>0){
                try{
//                    System.out.println(calculationsSplit[5]);
                    playTTS(calcbox[2]);
                }catch (Exception e){
                    System.out.println("audio failed");
                }

            }
        });
        HBox audioButtons1 = new HBox(toraAudio,todaAudio);
        HBox audioButtons2 = new HBox(asdaAudio,ldaAudio);
        toraAudio.setPrefSize(window.getWidth() * 0.06,window.getHeight() * 0.06);
        todaAudio.setPrefSize(window.getWidth() * 0.06,window.getHeight() * 0.06);
        todaAudio.setTextAlignment(TextAlignment.LEFT);
        asdaAudio.setPrefSize(window.getWidth() * 0.06,window.getHeight() * 0.06);
        ldaAudio.setPrefSize(window.getWidth() * 0.06,window.getHeight() * 0.06);
        VBox audioButtons = new VBox(audioButtons1,audioButtons2);
        audioButtons.setMinWidth(150);

        ImageView compass = new ImageView(new Image(TopScene.class.getResource("/compass.png").toExternalForm()));
        compass.setFitHeight(window.getHeight()*0.18);
        compass.setFitWidth(window.getHeight()*0.18);
        compass.setTranslateX(-12.5);

        VBox calculations = new VBox();
        calculations.setPrefSize(window.getWidth()*0.8,window.getHeight()*0.25);
        calculations.getStyleClass().add("boxBackground");
        Text calculationsHeader = new Text("Calculations:");
        calculationsHeader.getStyleClass().add("heading");

        String calculationsString = controller.getCalculationText();
        ScrollPane[] textPanes = new ScrollPane[4];

        if(calculationsString != "") {
            String[] calculationsSplit = calculationsString.lines().toArray(String[]::new);
            for(int i = 0; i < 4; i++) {
                calculationsText[i] = new Text();
                calculationsText[i].setText(calculationsSplit[i*3] + "\n" + calculationsSplit[(i*3)+1] + "\n" + calculationsSplit[(i*3)+2]);
                textPanes[i] = new ScrollPane(calculationsText[i]);
                textPanes[i].setFitToHeight(true);
                textPanes[i].setMinSize(window.getWidth()*0.4,window.getHeight()*0.125);
            }
        } else {
            for(int i = 0; i < 4; i++) {
                calculationsText[i] = new Text();
                calculationsText[i].setText("");
                textPanes[i] = new ScrollPane(calculationsText[i]);
                textPanes[i].setFitToHeight(true);
                textPanes[i].setMinSize(window.getWidth()*0.4,window.getHeight()*0.125);
            }
        }

        HBox box1 = new HBox(textPanes[0],textPanes[1]);
        HBox box2 = new HBox(textPanes[2],textPanes[3]);
        VBox texts = new VBox(box1, box2);

        calculations.getChildren().addAll(calculationsHeader,texts);

        //Rotation Buttons
        Button rightRotateButton = new Button("Rotate Right");
        Button leftRotateButton = new Button("Rotate Left");
        rightRotateButton.getStyleClass().add("menuItem");
        rightRotateButton.setPrefSize(window.getWidth()*0.085,window.getHeight()*0.06);
        leftRotateButton.getStyleClass().add("menuItem");
        leftRotateButton.setPrefSize(window.getWidth()*0.085,window.getHeight()*0.06);
        VBox rotationButtons = new VBox(leftRotateButton,rightRotateButton);
        rotationButtons.setPadding(new Insets(55,10,0,0));
        rotationButtons.setSpacing(5);
        rotationButtons.setTranslateX(13.5);

        Button switchScene = new Button("Top-down-view");
        switchScene.setPrefSize(window.getWidth()*0.09,window.getHeight()*0.09);
        switchScene.setOnAction(actionEvent -> window.startTop());
        switchScene.getStyleClass().add("menuItem");
        switchScene.setTranslateX(-7);

        threeDimensional.getStyleClass().add("menuItem");
        threeDimensional.setTranslateX(-7);

        //Buttons Panel
        HBox buttons = new HBox();
        buttons.setPrefSize(sideWidth,window.getHeight()*0.25);
        buttons.getChildren().addAll(calculations, audioButtons);
        buttons.setSpacing(window.getWidth()*0.02);
        buttons.setAlignment(Pos.CENTER_LEFT);
        Platform.runLater( () -> root.requestFocus() );

        Text title = new Text("Side view");
        title.getStyleClass().add("title");

        root.setCenter(combinedComponent);
        Group bottomGroup = new Group(buttons, notificationBox);
        notificationBox.setTranslateY(10);
        notificationBox.setTranslateX(360);
        notificationBox.setTranslateY(110);
        root.setBottom(bottomGroup);
        root.setLeft(inputs);
        root.setTop(title);
        VBox rightComponents = new VBox(takeOffDirection, compass, switchScene,threeDimensional);
        rightComponents.setSpacing(10);
        rightComponents.setTranslateX(5);
        root.setRight(rightComponents);
        if (initialLoad) {
            initialLoad = false;
            updateUI(controller.getPresetAirportValues());
        }
    }

    public void updateUI(AirportParameters aParams) { //obstacle may contain null values where no obstacle exists for a runwayStrip

        float sideWidth = 0.5f * window.getWidth() * 1.08f;
        float sideHeight = 0.4f * window.getHeight() * 1.08f;
        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        SideComponent newSideComponent = null;
        StackPane slope = new StackPane();

        if(obstacle != null) {
            try {
                newSideComponent = new SideComponent(sideWidth,sideHeight,controller.recalculateRunwayStripParameters());
                newSideComponent.paint();
                obstaclePane = makeObstacle(newSideComponent, obstacle.getDistanceFromCentre()
                        , sideWidth, sideHeight,obstacle.getDistance0f(),obstacle.getDistance1f(),obstacle.getWidth(),obstacle.getLength());
                    slope = makeSlope(newSideComponent, obstacle.getHeight()
                            , sideWidth, sideHeight,obstacle.getDistance0f(),obstacle.getDistance1f(),obstacle.getWidth(),obstacle.getLength(),
                            controller.getLogicalRunwayIndex());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            baseParameters = aParams.runwayStripParameters[inputs.getSelectedRunway()];
            newSideComponent = new SideComponent(sideWidth,sideHeight,baseParameters);
            newSideComponent.paint();
        }

        this.root.getChildren().remove(root.getCenter());

        StackPane arrowPane;
        arrowPane = createArrows(newSideComponent, sideWidth, sideHeight,controller.getLogicalRunwayIndex());

        StackPane newCombined = new StackPane();
        newCombined.setPrefSize(sideWidth,sideHeight);
        newCombined.setMaxSize(sideWidth,sideHeight);
        newCombined.getChildren().addAll(newSideComponent,obstaclePane,slope,arrowPane);
        newCombined.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        newCombined.setMouseTransparent(true);
        newCombined.setFocusTraversable(false);

        controller.setCalculationText(controller.recalculateRunwayParametersString(controller.getActiveLogicalRunway().getDesignator()));
        String calculationsString = controller.getCalculationText();

        if(calculationsString != "") {
            String[] calculationsSplit = calculationsString.lines().toArray(String[]::new);
            for(int i = 0; i < 4; i++) {
                calculationsText[i].setText(calculationsSplit[i * 3] + "\n" + calculationsSplit[(i * 3) + 1] + "\n" + calculationsSplit[(i * 3) + 2]);
            }
        } else {
            for(int i = 0; i < 4; i++) {
                calculationsText[i].setText("");
            }
        }

        root.setCenter(newCombined);
    }

    public void updateUIRecalculations(RunwayStripParameters recalculatedRunwayStripParameters) {
        float sideWidth = 0.5f * window.getWidth() * 1.08f;
        float sideHeight = 0.4f * window.getHeight() * 1.08f;

        this.root.getChildren().remove(root.getCenter());
        SideComponent newSideComponent = new SideComponent(sideWidth,sideHeight,recalculatedRunwayStripParameters);
        newSideComponent.paint();

        StackPane arrowPane;
        arrowPane = createArrows(newSideComponent, sideWidth, sideHeight,controller.getLogicalRunwayIndex());

        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        StackPane slope = new StackPane();
        if(obstacle != null) {
            obstaclePane = makeObstacle(newSideComponent, obstacle.getDistanceFromCentre()
                    , sideWidth, sideHeight,obstacle.getDistance0f(),obstacle.getDistance1f(),obstacle.getWidth(),obstacle.getLength());
                slope = makeSlope(newSideComponent, obstacle.getHeight()
                        , sideWidth, sideHeight,obstacle.getDistance0f(),obstacle.getDistance1f(),obstacle.getWidth(),obstacle.getLength(),
                        controller.getLogicalRunwayIndex());

        }

        StackPane newCombined = new StackPane();
        newCombined.setPrefSize(sideWidth,sideHeight);
        newCombined.setMaxSize(sideWidth,sideHeight);
        newCombined.getChildren().addAll(newSideComponent,obstaclePane,slope,arrowPane);
        newCombined.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        newCombined.setMouseTransparent(true);
        newCombined.setFocusTraversable(false);

        controller.setCalculationText(controller.recalculateRunwayParametersString(controller.getActiveLogicalRunway().getDesignator()));
        String calculationsString = controller.getCalculationText();
        String[] calculationsSplit = calculationsString.lines().toArray(String[]::new);
        for(int i = 0; i < 4; i++) {
            calculationsText[i].setText(calculationsSplit[i * 3] + "\n" + calculationsSplit[(i * 3) + 1] + "\n" + calculationsSplit[(i * 3) + 2]);
        }

        root.setCenter(newCombined);

    }

    public float getSideWidth() {
        return sideWidth;
    }
    public float getSideHeight() {
        return sideHeight;
    }

    public Image getSnapshot() {
        arrowPane.setVisible(false);
        Image image = combinedComponent.snapshot(null, null);
        arrowPane.setVisible(true);
        return image;
    }

    public void playTTS(String toPlay){
        System.out.println("Play Audio");
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        try{
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(toPlay,null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.deallocate();
        }catch (EngineException | AudioException | InterruptedException e){
            e.printStackTrace();
        }
    }
}