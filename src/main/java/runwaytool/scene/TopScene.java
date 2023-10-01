package runwaytool.scene;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.css.Rect;
import runwaytool.component.*;
import runwaytool.controller.Controller;
import runwaytool.model.*;
import runwaytool.view.ThreeDimensionalWindow;
import runwaytool.view.Window;

import javax.imageio.ImageIO;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;


public class TopScene extends GenericScene{
    private Controller controller;
    private static final Logger logger = LogManager.getLogger(TopScene.class);
    RunwayStripParameters baseParameters;
    private SidebarTabPane inputs;
    private boolean initialLoad = true;
    private double compassAngle;
    private double rotate;
    private Text calculationsText[] = new Text[4];
    private ImageView compass;
    private Arrow TORA,TODA,ASDA,LDA,RESA;
    private StackPane combinedComponent;
    private Button threeDimensional;
    private float topDownWidth;
    private float topDownHeight;
    private Slider angleSlider = null;
    private boolean isPLaying= false;
    private final Object lock = new Object(); // create a lock object
    private Synthesizer synthesizer;
    private NotificationBox notificationBox;


    /**
     * Create a new scene, passing the Window used to display it in
     *
     * @param window
     * @param notiBox
     */
    public TopScene(Window window, Controller controller, SidebarTabPane sidebarTabPane, Button threeDimensional, NotificationBox notiBox) {
        super(window);
        this.controller = controller;
        this.inputs = sidebarTabPane;
        inputs.getStyleClass().add("menuItem");
        this.threeDimensional = threeDimensional;
        topDownWidth = 0.5f * window.getWidth() * 1.08f;
        topDownHeight = 0.4f * window.getHeight() * 1.08f;
        this.notificationBox = notiBox;
        initialise();
    }

    @Override
    public void initialise() {
        if (initialLoad) {
            ArrayList<RunwayParameters> baseParametersList = new ArrayList<>();
            baseParametersList.add(0, new RunwayParameters(new Designator(9, DesignatorSuffix.L),
                    3902f,3902f,3902f,3902f,0f));
            baseParametersList.add(1, new RunwayParameters(new Designator(27, DesignatorSuffix.R),
                    3902f,3902f,3902f,3902f,0f));
            baseParameters = new RunwayStripParameters("Default",3902,baseParametersList,50f,240f,
                    75f,105f,150f,300f);
            rotate = 0;
        }

    }

    private StackPane makeArrows(TopDownComponent topDownComponent, float width, float height, int index){
        RunwayStrip activeRunwayStrip = controller.getActiveRunwayStrip();
        float length = activeRunwayStrip.getLength();
        float stopway = activeRunwayStrip.getStopway();
        float clearway = activeRunwayStrip.getClearway();
        float ALS = activeRunwayStrip.getALS();
        float RESAWidth = activeRunwayStrip.getRESAWidth();
        float stripend = activeRunwayStrip.getStripEnd();
        float slopeCalculation = 0;

        float TORA1offset = stripend;
        float TODA1offset = stripend;
        float ASDA1offset = stripend;
        float LDA1offset = stripend + activeRunwayStrip.getLogicalRunways().get(index).getDisplacedThreshold();
        float TORA2offset = stripend + length;
        float TODA2offset = stripend + length;
        float ASDA2offset = stripend + length;
        float LDA2offset = stripend + length - activeRunwayStrip.getLogicalRunways().get(index).getDisplacedThreshold();

        float blastProtection = 0;
        float obstacleHeight = 0;

        if(controller.getActiveObstacle() != null) {
            Obstacle activeObstacle = controller.getActiveObstacle();
            float distance1 = activeObstacle.getDistance0f();
            float distance2 = activeObstacle.getDistance1f();
            blastProtection = activeObstacle.getBlastAllowance();
            obstacleHeight = activeObstacle.getHeight();
            slopeCalculation = obstacleHeight * ALS;
            System.out.println("RESA: " + RESAWidth);
            System.out.println("Slope: " + slopeCalculation);
            System.out.println("Blast: " + blastProtection);
            System.out.println("Displaced Thresh: " + activeRunwayStrip.getLogicalRunways().get(index).getDisplacedThreshold());
            if(distance1 <= distance2) {    //closer to "left" end
                if (index == 0) {
                    LDA1offset = distance1 + stripend + Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation));
                } else {
                    LDA1offset = distance1 + stripend + Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation)) + activeRunwayStrip.getLogicalRunways().get(index).getDisplacedThreshold();

                }
                TORA1offset = stripend + distance1 + blastProtection;
                TODA1offset = stripend + distance1 + blastProtection;
                ASDA1offset = stripend + distance1 + blastProtection;
            }
            else {
                if (index == 0) {
                    LDA2offset = stripend + length - distance2 - Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation)) - activeRunwayStrip.getLogicalRunways().get(index).getDisplacedThreshold();
                } else {
                    LDA2offset = stripend + length - distance2 - Float.max(blastProtection, Float.max(RESAWidth,slopeCalculation));

                }
                TORA2offset = stripend + length - distance2 - blastProtection;
                TODA2offset = stripend + length - distance2 - blastProtection;
                ASDA2offset = stripend + length - distance2 - blastProtection;
            }
        }

        StackPane arrowPane = new StackPane();
        StackPane.setMargin(arrowPane, new Insets(topDownComponent.getHeight()*0.05,topDownComponent.getWidth()*0.95
                ,topDownComponent.getHeight()*0.95,topDownComponent.getWidth()*0.05));

        if(index == 0) {
            TODA = new Arrow(topDownComponent.getXPlacement(TODA1offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*0.75f)
                    , topDownComponent.getXPlacement(topDownComponent.getTODA()[0]+TODA1offset),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*0.75f),  "TODA: " +topDownComponent.getTODA()[0],Boolean.TRUE, Boolean.FALSE);
            TODA.setStroke(Color.WHITE);
            TODA.setTextFill(Color.RED);
            ASDA = new Arrow(topDownComponent.getXPlacement(ASDA1offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd())
                    , topDownComponent.getXPlacement(topDownComponent.getASDA()[0]+ASDA1offset),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()),  "ASDA: " +topDownComponent.getASDA()[0], Boolean.TRUE, Boolean.FALSE);
            ASDA.setStroke(Color.WHITE);
            ASDA.setTextFill(Color.RED);
            TORA = new Arrow(topDownComponent.getXPlacement(TORA1offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*1.25f)
                    , topDownComponent.getXPlacement(topDownComponent.getTORA()[0]+TORA1offset),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*1.25f),  "TORA: " +topDownComponent.getTORA()[0], Boolean.TRUE, Boolean.FALSE);
            TORA.setStroke(Color.WHITE);
            TORA.setTextFill(Color.RED);
            LDA = new Arrow(topDownComponent.getXPlacement(LDA1offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*1.5f)
                    , topDownComponent.getXPlacement(topDownComponent.getLDA()[0] + LDA1offset),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*1.5f),  "LDA: " +topDownComponent.getLDA()[0], Boolean.TRUE, Boolean.FALSE);
            LDA.setStroke(Color.WHITE);
            LDA.setTextFill(Color.RED);
            arrowPane.getChildren().addAll(TODA,ASDA,TORA,LDA);
        } else {
            TODA = new Arrow( topDownComponent.getXPlacement(TODA2offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*2.5f)
                    ,topDownComponent.getXPlacement(TODA2offset-topDownComponent.getTODA()[1]),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*2.5f),  "TODA: " +topDownComponent.getTODA()[1],Boolean.FALSE, Boolean.FALSE);
            TODA.setStroke(Color.WHITE);
            TODA.setTextFill(Color.RED);
            ASDA = new Arrow(topDownComponent.getXPlacement(ASDA2offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*2.75f)
                    ,topDownComponent.getXPlacement(ASDA2offset - topDownComponent.getASDA()[1]),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*2.75f),  "ASDA: " +topDownComponent.getASDA()[1], Boolean.FALSE, Boolean.FALSE);
            ASDA.setStroke(Color.WHITE);
            ASDA.setTextFill(Color.RED);
            TORA = new Arrow(topDownComponent.getXPlacement(TORA2offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*3f)
                    ,topDownComponent.getXPlacement(TORA2offset-topDownComponent.getTORA()[1]),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*3f),  "TORA: " +topDownComponent.getTORA()[1], Boolean.FALSE, Boolean.FALSE);
            TORA.setStroke(Color.WHITE);
            TORA.setTextFill(Color.RED);
            LDA = new Arrow(topDownComponent.getXPlacement(LDA2offset), topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*3.25f)
                    ,topDownComponent.getXPlacement(LDA2offset-topDownComponent.getLDA()[1]),
                    topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*3.25f),  "LDA: " +topDownComponent.getLDA()[1], Boolean.FALSE, Boolean.FALSE);
            LDA.setStroke(Color.WHITE);
            LDA.setTextFill(Color.RED);
            arrowPane.getChildren().addAll(TODA,ASDA,TORA,LDA);
        }

        arrowPane.setPrefSize(width,height);
        arrowPane.setMaxSize(width,height);
        arrowPane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return arrowPane;

    }

    private StackPane makeObstacle(TopDownComponent topDownComponent, float centreDistance, float windowWidth,
                                   float windowHeight, float distanceL, float distanceR, float width, float length) {
        StackPane obstaclePane = new StackPane();
        StackPane.setMargin(obstaclePane, new Insets(topDownComponent.getHeight()*0.05,topDownComponent.getWidth()*0.95
                ,topDownComponent.getHeight()*0.95,topDownComponent.getWidth()*0.05));

        if(length < 100) length = 100;
        if(width < 25) width = 25;

        float startX = topDownComponent.getXPlacement(topDownComponent.getStripend() + distanceL - (length/2));
        float startY = topDownComponent.getYPlacement(topDownComponent.getCenterToClearedEnd()*2 + centreDistance - (width/2));

        System.out.println(startX + "," + startY + "," + topDownComponent.getXPlacement(length) + "," + topDownComponent.getYPlacement(width));

        Pane obstacle = new Pane();
        Rectangle object = new Rectangle(startX, startY, topDownComponent.getXPlacement(length), topDownComponent.getYPlacement(width));
        object.setFill(Color.RED);
        obstacle.getChildren().add(object);
        topDownComponent.setColor(controller.getColorVar());

        obstaclePane.setPrefSize(windowWidth,windowHeight);
        obstaclePane.setMaxSize(windowWidth,windowHeight);
        obstaclePane.setAlignment(Pos.CENTER);
        obstaclePane.getChildren().add(obstacle);
        return obstaclePane;
    }

    private StackPane makeRESAIndicators(TopDownComponent topDownComponent, float centreDistance, float windowWidth,
                                         float windowHeight, float distanceL, float distanceR, float width, float length, float objectHeight, int index) {
        StackPane resaPane = new StackPane();
        StackPane.setMargin(resaPane, new Insets(topDownComponent.getHeight()*0.05,topDownComponent.getWidth()*0.95
                ,topDownComponent.getHeight()*0.95,topDownComponent.getWidth()*0.05));

        float stripend = controller.getActiveRunwayStrip().getStripEnd();
        float LDA1offset = stripend;
        float LDA2offset = stripend + controller.getActiveRunwayStrip().getLength();
        float blastProtection = 0;
        float slopeCalculation = 0;
        float ALS = controller.getActiveRunwayStrip().getALS();

        if(controller.getActiveObstacle() != null) {
            blastProtection = controller.getActiveObstacle().getBlastAllowance();
            slopeCalculation = objectHeight * ALS;

            if(distanceL <= distanceR) {
                LDA1offset = stripend + distanceL + Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation));
                LDA2offset = stripend + length - distanceR - Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation));
            } else {
                LDA1offset = stripend + distanceL - Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation));
                LDA2offset = stripend + length - distanceR - Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation));
            }
        }

        float startX = topDownComponent.getXPlacement(topDownComponent.getStripend() + distanceL);
        float startY;

        Pane slope = new Pane();
        if(index == 0){
            startY = windowHeight*0.38f;
            RESA = new Arrow(startX, startY, topDownComponent.getXPlacement(LDA1offset), startY,String.valueOf(Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation))),false,true);
            if(distanceL>distanceR) RESA.setVisible(false);
        }
        else {
            startY = windowHeight*0.52f;
            RESA = new Arrow(startX, startY, topDownComponent.getXPlacement(LDA1offset), startY,String.valueOf(Float.max(blastProtection, Float.max(controller.getActiveRunwayStrip().getRESAWidth(),slopeCalculation))),true,true);
            if(distanceL<=distanceR) RESA.setVisible(false);
        }

        RESA.setStroke(Color.RED);;
        slope.getChildren().add(RESA);

        resaPane.setPrefSize(windowWidth,windowHeight);
        resaPane.setMaxSize(windowWidth,windowHeight);
        resaPane.getChildren().add(slope);

        return resaPane;
    }

    @Override
    public void build() {
        System.out.println("Building " + this.getClass().getName());

        root = new BorderPane();
        root.setPadding(new Insets(50, 50, 30, 50));
        topDownWidth = 0.5f * window.getWidth() * 1.08f;
        topDownHeight = 0.4f * window.getHeight() * 1.08f;
        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        StackPane resaPane = new StackPane();
        TopDownComponent topDownComponent = null;
        compassAngle = (controller.getActiveRunwayStrip().getLogicalRunways().get(0).getDesignator().getDegree()*10)+90;

        if(obstacle != null) {
            try {
                topDownComponent = new TopDownComponent(topDownWidth,topDownHeight,controller.recalculateRunwayStripParameters());
                topDownComponent.paint();
                topDownComponent.setColor(controller.getColorVar());
                obstaclePane = makeObstacle(topDownComponent,obstacle.getDistanceFromCentre(),topDownWidth,topDownHeight,
                        obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength());
                resaPane = makeRESAIndicators(topDownComponent,obstacle.getDistanceFromCentre(),topDownWidth,topDownHeight,
                        obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength(), obstacle.getHeight(),controller.getLogicalRunwayIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            topDownComponent = new TopDownComponent(topDownWidth,topDownHeight, baseParameters);
            topDownComponent.paint();
            topDownComponent.setColor(controller.getColorVar());
        }

        //Background Colour
        Background background = new Background(new BackgroundFill(Color.LIGHTBLUE, null, null));
        root.setBackground(background);

        TopDownComponent finalTopDownComponent = topDownComponent;
        inputs.setColorChangeListener((color) -> finalTopDownComponent.setColor(controller.getColorVar()));

        StackPane arrowPane = new StackPane();
        arrowPane = makeArrows(topDownComponent, topDownWidth, topDownHeight,controller.getLogicalRunwayIndex());
        arrowPane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        combinedComponent = new StackPane();
        combinedComponent.setPrefSize(topDownWidth, topDownHeight);
        combinedComponent.setMaxSize(topDownWidth, topDownHeight);
        combinedComponent.getChildren().addAll(topDownComponent, obstaclePane,resaPane,arrowPane);
        //combinedComponent.setTranslateY(-50);
        Text takeOffDirectionLabel = new Text(" Direction:");
        takeOffDirectionLabel.getStyleClass().add("heading");
        TakeOffDirectionComponent takeOff = new TakeOffDirectionComponent(100,60,controller.getLogicalRunwayIndex(), false);
        VBox takeOffDirection = new VBox(takeOffDirectionLabel,takeOff);
        takeOffDirection.setStyle("-fx-background-color: lightGrey;");
        takeOffDirection.getStyleClass().add("boxBackground");
        takeOffDirection.setMaxHeight(80);
        takeOffDirection.setMaxWidth(80);
        takeOff.setRotate(rotate);
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

        compass = new ImageView(new Image(TopScene.class.getResource("/compass.png").toExternalForm()));
        compass.setFitHeight(window.getHeight()*0.18);
        compass.setFitWidth(window.getHeight()*0.18);
        compass.setTranslateX(-12.5);
        compass.setRotate(compassAngle + rotate);

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

//        calcsTop.setSpacing(1200);
        HBox box1 = new HBox(textPanes[0],textPanes[1]);
        HBox box2 = new HBox(textPanes[2],textPanes[3]);
        VBox texts = new VBox(box1, box2);

        calculations.getChildren().addAll(calculationsHeader,texts);

        angleSlider = new Slider(0,360,0);
        angleSlider.setMajorTickUnit(90);
        angleSlider.setMinorTickCount(45);
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                rotate = (double) newValue;
                compass.setRotate(compassAngle+rotate);
                combinedComponent.setRotate(rotate);
                ASDA.rotate(-rotate);
                TORA.rotate(-rotate);
                LDA.rotate(-rotate);
                TODA.rotate(-rotate);
                RESA.rotate(-rotate);
                combinedComponent.setScaleX(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
                combinedComponent.setScaleY(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
                //combinedComponent.setTranslateY(-50+(-1*(((Math.sin(Math.toRadians(rotate+90)))*5)+6)));
                takeOff.setRotate(rotate);
            }
        });
        VBox rotationButtons = new VBox(angleSlider);
        rotationButtons.setPadding(new Insets(55, 10, 0, 0));
        rotationButtons.setSpacing(5);
        rotationButtons.setTranslateX(5);


        Button switchScene = new Button("Side-view");
        switchScene.setPrefSize(window.getWidth() * 0.09, window.getHeight() * 0.09);
        switchScene.setOnAction(actionEvent -> window.startSide());
        switchScene.getStyleClass().add("menuItem");
        switchScene.setTranslateX(-7);

        threeDimensional.getStyleClass().add("menuItem");
        threeDimensional.setTranslateX(-7);

        VBox buttons2 = new VBox(audioButtons, rotationButtons);
        buttons2.setSpacing(30);
        buttons2.setMinWidth(150);

        HBox buttons = new HBox();
        buttons.setPrefSize(topDownWidth, window.getHeight() * 0.25);
        buttons.getChildren().addAll(calculations, buttons2);
        buttons.setSpacing(window.getWidth() * 0.02);
        buttons.setAlignment(Pos.CENTER_LEFT);
        Platform.runLater(() -> root.requestFocus());

        Text title = new Text("Top-down view");
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
        //rightComponents.setTranslateX(20);
        root.setRight(rightComponents);
        if (initialLoad) {
            initialLoad = false;
            updateUI(controller.getPresetAirportValues());
        }
    }

    public void updateUI(AirportParameters aParams) { //obstacle may contain null values where no obstacle exists for a runwayStrip

        float topDownWidth = 0.5f * window.getWidth() * 1.08f;
        float topDownHeight = 0.4f * window.getHeight() * 1.08f;
        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        StackPane resaPane = new StackPane();
        TopDownComponent newTopDown = null;
        compassAngle = (controller.getActiveRunwayStrip().getLogicalRunways().get(0).getDesignator().getDegree()*10)+90;

        if(obstacle != null) {
            try{
                newTopDown = new TopDownComponent(topDownWidth,topDownHeight,controller.recalculateRunwayStripParameters());
                newTopDown.paint();
                newTopDown.setColor(controller.getColorVar());
                obstaclePane = makeObstacle(newTopDown, obstacle.getDistanceFromCentre(), topDownWidth, topDownHeight,
                        obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength());
                resaPane = makeRESAIndicators(newTopDown,obstacle.getDistanceFromCentre(),topDownWidth,topDownHeight,
                        obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength(), obstacle.getHeight(),controller.getLogicalRunwayIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            baseParameters = aParams.runwayStripParameters[0];
            newTopDown = new TopDownComponent(topDownWidth,topDownHeight,baseParameters);
            newTopDown.paint();
            newTopDown.setColor(controller.getColorVar());
        }

        this.root.getChildren().remove(root.getCenter());

        StackPane arrowPane;
        arrowPane = makeArrows(newTopDown, topDownWidth, topDownHeight,controller.getLogicalRunwayIndex());
        arrowPane.setPickOnBounds(false);
        arrowPane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        combinedComponent = new StackPane();
        combinedComponent.setPrefSize(topDownWidth,topDownHeight);
        combinedComponent.setMaxSize(topDownWidth,topDownHeight);
        combinedComponent.getChildren().addAll(newTopDown,obstaclePane,resaPane,arrowPane);
        combinedComponent.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        combinedComponent.setMouseTransparent(true);
        combinedComponent.setFocusTraversable(false);
        combinedComponent.setRotate(rotate);
        ASDA.rotate(-rotate);
        TORA.rotate(-rotate);
        LDA.rotate(-rotate);
        TODA.rotate(-rotate);
        combinedComponent.setScaleX(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
        combinedComponent.setScaleY(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
        //combinedComponent.setTranslateY(-50+(-1*(((Math.sin(Math.toRadians(rotate+90)))*5)+6)));

        compass.setRotate(compassAngle+rotate);

        TopDownComponent finalNewTopDown = newTopDown;
        inputs.setColorChangeListener((color) -> finalNewTopDown.setColor(controller.getColorVar()));

        String calculationsString = controller.getCalculationText();

        if(calculationsString != "") {

            String[] calculationsSplit = calculationsString.lines().toArray(String[]::new);
            for(int i = 0; i < 4; i++) {
                calculationsText[i].setText(calculationsSplit[i * 3] + "\n" + calculationsSplit[(i * 3) + 1] + "\n" + calculationsSplit[(i * 3) + 2]);;
            }
        } else {
            for(int i = 0; i < 4; i++) {
                calculationsText[i].setText("");
            }
        }
        root.setCenter(combinedComponent);


    }

    public void updateUIRecalculations(RunwayStripParameters recalculatedRunwayStripParameters) {
        float topDownWidth = 0.5f * window.getWidth() * 1.08f;
        float topDownHeight = 0.4f * window.getHeight() * 1.08f;

        this.root.getChildren().remove(root.getCenter());
        TopDownComponent newTopDown = new TopDownComponent(topDownWidth,topDownHeight,recalculatedRunwayStripParameters);
        newTopDown.paint();
        compassAngle = (controller.getActiveRunwayStrip().getLogicalRunways().get(0).getDesignator().getDegree()*10)+90;

        StackPane arrowPane;
        arrowPane = makeArrows(newTopDown, topDownWidth, topDownHeight,controller.getLogicalRunwayIndex());
        arrowPane.setPickOnBounds(false);
        //arrowPane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Obstacle obstacle = controller.getActiveObstacle();
        StackPane obstaclePane = new StackPane();
        StackPane resaPane = new StackPane();
        if(obstacle != null) {
            obstaclePane = makeObstacle(newTopDown, obstacle.getDistanceFromCentre(), topDownWidth, topDownHeight,
                    obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength());
            resaPane = makeRESAIndicators(newTopDown,obstacle.getDistanceFromCentre(),topDownWidth,topDownHeight,
                    obstacle.getDistance0f(), obstacle.getDistance1f(), obstacle.getWidth(), obstacle.getLength(), obstacle.getHeight(),controller.getLogicalRunwayIndex());
        }

        combinedComponent = new StackPane();
        combinedComponent.setPrefSize(topDownWidth,topDownHeight);
        combinedComponent.setMaxSize(topDownWidth,topDownHeight);
        combinedComponent.getChildren().addAll(newTopDown,obstaclePane,resaPane,arrowPane);
        combinedComponent.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        combinedComponent.setMouseTransparent(true);
        combinedComponent.setFocusTraversable(false);
        combinedComponent.setRotate(rotate);
        ASDA.rotate(-rotate);
        TORA.rotate(-rotate);
        LDA.rotate(-rotate);
        TODA.rotate(-rotate);
        combinedComponent.setScaleX(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
        combinedComponent.setScaleY(((0.2*Math.cos(Math.toRadians(2*rotate)))/1)+0.8);
        //combinedComponent.setTranslateY(-50+(-1*(((Math.sin(Math.toRadians(rotate+90)))*5)+6)));


        compass.setRotate(compassAngle+rotate);

        inputs.setColorChangeListener((color) -> newTopDown.setColor(controller.getColorVar()));

        String calculationsString = controller.getCalculationText();
        String[] calculationsSplit = calculationsString.lines().toArray(String[]::new);
        for(int i = 0; i < 4; i++) {
            calculationsText[i].setText(calculationsSplit[i * 3] + "\n" + calculationsSplit[(i * 3) + 1] + "\n" + calculationsSplit[(i * 3) + 2]);
        }

        root.setCenter(combinedComponent);


    }

    public void playTTS(String toPlay) throws EngineException {
//        synchronized(lock){
//            if(isPLaying){
//                System.out.println("isPlaying: "+isPLaying);
//                System.out.println("audio fail");
//                return;
//            }
//            isPLaying=true;
//        }
//        synthesizer.deallocate();
        System.out.println("Play Audio");
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        try{
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(toPlay,null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.cancelAll();
        }catch (EngineException | AudioException | InterruptedException e) {
            System.out.println("fail");
            e.printStackTrace();
        }
//        }finally {
////            synthesizer.deallocate(); // deallocate synthesizer
//            synchronized(lock){
//                isPLaying = false;
//                System.out.println("isPlaying: "+isPLaying);
//            }
//        }
    }


    public float getTopDownWidth() {
        return topDownWidth;
    }
    public float getTopDownHeight() {
        return topDownHeight;
    }

    public Image getSnapshot() {
        double val = angleSlider.getValue();
        angleSlider.setValue(0);
        Image image = combinedComponent.snapshot(null, null);
        angleSlider.setValue(val);
        return image;
    }
}