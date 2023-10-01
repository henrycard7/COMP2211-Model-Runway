package runwaytool.view;

import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import runwaytool.controller.Controller;

public class ThreeDimensionalWindow {
    private final Stage stage;
    private final double width;
    private final double height;
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate translate = new Translate(0, 0, 0);
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Translate cameraTranslate = new Translate(0, 0, -6);

    public ThreeDimensionalWindow(Stage stage, double width, double height, Controller controller, Image topImage, Image sideImage) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        AmbientLight pointLight = new AmbientLight(Color.WHITE);
        pointLight.setTranslateZ(-500);
        pointLight.setBlendMode(BlendMode.ADD);

        Box topBox = new Box(width, height, 0);
        PhongMaterial topMaterial = new PhongMaterial();
        topMaterial.setDiffuseMap(topImage);
        topBox.setMaterial(topMaterial);
        //
        Box sideBox = new Box(width, height, 0);
        PhongMaterial sideMaterial = new PhongMaterial();
        sideMaterial.setDiffuseMap(sideImage);
        sideBox.setMaterial(sideMaterial);
        sideBox.setRotationAxis(Rotate.X_AXIS);
        sideBox.setRotate(90);
        sideBox.setTranslateZ(-0.1f);
        sideBox.setScaleZ(1);
        sideBox.setScaleY(0.3);


        Group root = new Group(topBox, sideBox);
        root.getChildren().addAll(pointLight);
        root.getTransforms().addAll(rotateX, rotateY, rotateZ, translate);

        System.out.println(width + ", " + height);


        Scene scene = new Scene(root, 800, 600,true,SceneAntialiasing.DISABLED);
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        camera.getTransforms().addAll(cameraTranslate);

        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            double deltaX = (mousePosX - mouseOldX);
            double deltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                double newAngleX = rotateX.getAngle() - (deltaY * 180 / scene.getHeight());
                double newAngleY = rotateY.getAngle() + (deltaX * 180 / scene.getWidth());
                if(newAngleX > -90 && newAngleX < 90) rotateX.setAngle(newAngleX);
                if(newAngleY > -90 && newAngleY < 90) rotateY.setAngle(newAngleY);
                if (rotateX.getAngle() > 0) {
                    sideBox.setScaleZ(-1);
                } else {
                    sideBox.setScaleZ(1);
                }
            } else if (me.isSecondaryButtonDown()) {
                rotateZ.setAngle(rotateZ.getAngle() - (deltaX * 180 / scene.getWidth()));
            }
        });

        scene.setOnScroll(event -> {
            double deltaScroll = event.getDeltaY();
            double delta = 0.5;
            double maxZoomOut = -10;
            double maxZoomIn = -2;
            if(deltaScroll > 0) {
                if(cameraTranslate.getZ() + delta >= maxZoomIn)
                    cameraTranslate.setZ(maxZoomIn);
                else
                    cameraTranslate.setZ(cameraTranslate.getZ() + delta);
            } else {
                if(cameraTranslate.getZ() - delta <= maxZoomOut)
                    cameraTranslate.setZ(maxZoomOut);
                else
                    cameraTranslate.setZ(cameraTranslate.getZ() - delta);
            }
        });

        stage.setScene(scene);
        setupStage();
    }

    public void setupStage() {
        stage.setTitle("3D Runway View");
        stage.setMinWidth(width);
        stage.setMinHeight(height + 20);
    }
}