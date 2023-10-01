package runwaytool.component;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import runwaytool.controller.Controller;
import runwaytool.model.*;
import runwaytool.scene.TopScene;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class SidebarTabPane extends VBox implements ActionListener {
    public static boolean isSelected;
    private Controller controller;
    private AirportParameters currentParameters;
    private Obstacle[] currentObstacles = new Obstacle[2];
    private TextField airportNameInput;
    private TextField runwayNoInput;
    private String oldRunwayNo;
    private ComboBox runwayDropdown;
    private Label runwayNameLabel;
    private TextField designator1Input;
    private TextField designator2Input;
    private String[] oldDesignators = new String[2];
    private TextField runwayLengthInput;
    private String oldLength;
    private TextField resaLengthInput;
    private String oldResaLength;
    private InputTable table;
    private String[][] oldTable;
    private int selectedRunway = 0; //0 or 1
    private boolean firstLoad = true;
    private boolean ofirstLoad = true;
    private Consumer<Color> colorChangeListener;
    private Button applyButton;
    private Label obstacleTabRunwayText;
    private Button addObstacleButton;
    private Button oApplyButton;
    private Button removeObstacleButton;
    private VBox obstacleTabContents;
    private VBox obstacleInputBox;
    private HBox obstacleRunwayHBox;
    private TextField odistance0;
    private TextField odistance1;
    private Label distanceToEnd0;
    private Label distanceToEnd1;
    private Tab obstacleTab;
    // Obstacle parameters
    private TextField oname;
    private ComboBox otype;
    private TextField oheight;
    private String oldOheight;
    private TextField oblastAllowance;
    private String oldOblastAllowance;
    private TextField odistanceToCentre;
    private String oldOdistanceToCentre;
    private TextField owidth;
    private String oldOwidth;
    private TextField olength;
    private String oldOlength;
    private boolean changedLength = false;

    public SidebarTabPane(Controller controller) {
        this.controller = controller;
        // Create a TabPane
        TabPane tabPane = new TabPane();
        // Create Tabs
        Tab runwaysTab = new Tab("Runways");
        obstacleTab = new Tab ("Obstacle");
        Tab xmlTab = new Tab("XML");
        Tab presetsTab = new Tab("Presets");
        Tab settingsTab = new Tab("Settings");

        // Set closable property to false
        runwaysTab.setClosable(false);
        obstacleTab.setClosable(false);
        xmlTab.setClosable(false);
        presetsTab.setClosable(false);
        settingsTab.setClosable(false);

        //Input table
        this.table = new InputTable();

        //Input Tab
        Label prompt1 = new Label("Airport Name: ");
        this.airportNameInput = new TextField();
        Label prompt2 = new Label("Number of Runways: ");
        this.runwayNoInput = new TextField();
        this.runwayNoInput.setMaxWidth(40);
        Label dropdownLabel = new Label("Select Runway: ");
        runwayDropdown = new ComboBox();
        runwayDropdown.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue oValue, String oldValue, String newValue) {
                switchRunway(oldValue, newValue);
            }
        });
        runwayNameLabel = new Label("Input for runway 09L/27R: ");
        runwayNameLabel.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        Label prompt3 = new Label("Designators: ");
        this.designator1Input = new TextField();
        designator1Input.setMaxWidth(40);
        this.designator2Input = new TextField();
        designator2Input.setMaxWidth(40);
        Label prompt4 = new Label("Length: ");
        this.runwayLengthInput = new TextField();
        runwayLengthInput.setMaxWidth(80);
        Label prompt5 = new Label("RESA Length: ");
        this.resaLengthInput = new TextField();
        resaLengthInput.setMaxWidth(80);
        HBox airportNameBox = new HBox(prompt1, airportNameInput);
        HBox runwayNoBox = new HBox(prompt2, runwayNoInput);
        HBox dropdownBox = new HBox(dropdownLabel, runwayDropdown);
        HBox designatorsBox = new HBox(prompt3, designator1Input, designator2Input);
        HBox runwayLengthBox = new HBox(prompt4,runwayLengthInput);
        HBox resaBox = new HBox(prompt5,resaLengthInput);
        applyButton = new Button("Apply");
        applyButton.getStyleClass().add("menuItem");
        Text errortext = new Text("Invalid input");
        errortext.setFill( Color.TRANSPARENT);
        //input tab layout
        VBox runwaysTabContents = new VBox(airportNameBox, runwayNoBox, dropdownBox, runwayNameLabel, designatorsBox, runwayLengthBox, resaBox, table, applyButton, errortext);
        runwaysTabContents.setSpacing(4);
        runwaysTabContents.setPadding(new Insets(10, 0, 0, 10));
        runwaysTab.setContent(runwaysTabContents);

        //stores table values as a string and updates model
        applyButton.setOnAction(event -> {
            if (checkInputValues()) {
                //Update model
                updateModel();
                errortext.setFill(Color.TRANSPARENT);
                //Update UI
                distanceToEnd0.setText(designator1Input.getText());
                distanceToEnd1.setText(designator2Input.getText());
                runwayNameLabel.setText("Input for runway " + designator1Input.getText() + "/" + designator2Input.getText() + ":");
                table.setLabels(designator1Input.getText(), designator2Input.getText());
                runwayDropdown.getItems().clear();
                String designator1 = currentParameters.runwayStripParameters[0].runwayParametersList.get(0).designator.getStringDesignator();
                String designator2 = currentParameters.runwayStripParameters[0].runwayParametersList.get(1).designator.getStringDesignator();
                runwayDropdown.getItems().add(designator1 + "/" + designator2);
                if (currentParameters.runwayStripParameters.length == 2) {
                    String designator3 = currentParameters.runwayStripParameters[1].runwayParametersList.get(0).designator.getStringDesignator();
                    String designator4 = currentParameters.runwayStripParameters[1].runwayParametersList.get(1).designator.getStringDesignator();
                    runwayDropdown.getItems().add(designator3 + "/" + designator4);
                } else {
                    selectedRunway = 0;
                }
                runwayDropdown.setValue(runwayDropdown.getItems().get(selectedRunway));
                obstacleTabRunwayText.setText(designator1 + "/" + designator2);
                if (changedLength) {
                    odistance0.setText(String.valueOf( Float.parseFloat(runwayLengthInput.getText()) / 2));//Updating distance0 causes distance1 to update too
                    if (controller.getActiveObstacle() != null) {
                        oApplyButton.fire();
                    }
                    changedLength = false;
                }
                if (controller.getActiveObstacle() != null) {
                    oApplyButton.fire();
                }
                updateUI();
            } else {
                errortext.setFill(Color.RED);
                //SEND ALERT VIA UI (Invalid values provided)
            }
        });

        //Obstacle tab layout
        Text objectErrorText = new Text("Invalid input");
        objectErrorText.setFill(Color.TRANSPARENT);
        Label obstacleTabRunwayLabel = new Label("Current Runway: ");
        obstacleTabRunwayText = new Label("09L/27R");
        obstacleTabRunwayText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        obstacleTabRunwayLabel.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        obstacleRunwayHBox = new HBox(obstacleTabRunwayLabel, obstacleTabRunwayText);
        addObstacleButton = new Button("Add obstacle");
        addObstacleButton.setOnAction(event -> {
            obstacleTabContents = new VBox(obstacleRunwayHBox, obstacleInputBox, objectErrorText);
            obstacleTabContents.setSpacing(2);
            obstacleTabContents.setPadding(new Insets(10, 0, 0, 10));
            obstacleTab.setContent(obstacleTabContents);
            oApplyButton.fire();
        });
        //
        Label oprompt1 = new Label("Name: ");
        oname = new TextField();
        HBox onameBox = new HBox(oprompt1, oname);
        Label oprompt2 = new Label("Obstacle Type: ");
        otype = new ComboBox();
        otype.getItems().add("Grounded Aircraft");
        otype.getItems().add("Foreign Object Debris");
        otype.setValue(otype.getItems().get(0));
        HBox otypeBox = new HBox(oprompt2, otype);
        Label oprompt3 = new Label("Height: ");
        oheight = new TextField();
        oheight.setMaxWidth(80);
        HBox oheightBox = new HBox(oprompt3, oheight);
        Label oprompt4 = new Label("Blast Allowance: ");
        oblastAllowance = new TextField();
        oblastAllowance.setMaxWidth(80);
        HBox oblastAllowanceBox = new HBox(oprompt4, oblastAllowance);
        otype.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue oValue, String oldValue, String newValue) {
                switch (newValue) {
                    //case "Grounded Aircraft" -> oblastAllowanceBox.setVisible(true);
                    //case "Foreign Object Debris" -> oblastAllowanceBox.setVisible(false);
                }
            }
        });
        Label oprompt5 = new Label("Distance to Centreline: ");
        odistanceToCentre = new TextField();
        odistanceToCentre.setMaxWidth(80);
        HBox odistanceToCentreBox = new HBox(oprompt5, odistanceToCentre);
        Label oprompt6 = new Label("Distances to Ends: ");
        distanceToEnd0 = new Label("09L: ");
        odistance0 = new TextField();
        distanceToEnd1 = new Label("27R: ");
        odistance1 = new TextField();
        odistance0.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue oValue, String oldValue, String newValue) {
                odistanceChanged(0, oldValue, newValue);
            }
        });
        odistance1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue oValue, String oldValue, String newValue) {
                odistanceChanged(1, oldValue, newValue);
            }
        });
        odistance0.setMaxWidth(50);
        Label spacer1 = new Label("    ");
        Label spacer2 = new Label("    ");
        odistance1.setMaxWidth(50);
        HBox odistancesBox = new HBox(spacer1, distanceToEnd0, odistance0, spacer2, distanceToEnd1, odistance1);
        Label oprompt9 = new Label("Width: ");
        owidth = new TextField();
        owidth.setMaxWidth(50);
        HBox owidthBox = new HBox(oprompt9, owidth);
        Label oprompt10 = new Label("Length: ");
        olength = new TextField();
        olength.setMaxWidth(50);
        HBox olengthBox = new HBox(oprompt10, olength);
        oApplyButton = new Button("Apply");
        oApplyButton.getStyleClass().add("menuItem");
        oApplyButton.setOnAction(event -> {
            if (ocheckInputValues()) {
                //Update model
                updateoModel();
                objectErrorText.setFill(Color.TRANSPARENT);
                controller.updateUIRecalculations();
            } else {
                //SEND ALERT VIA UI (Invalid values provided)
                objectErrorText.setFill(Color.RED);
            }
        });
        removeObstacleButton = new Button("Remove Obstacle");
        removeObstacleButton.setOnAction(event -> {
            obstacleTabContents = new VBox(obstacleRunwayHBox, addObstacleButton);
            obstacleTabContents.setSpacing(4);
            obstacleTabContents.setPadding(new Insets(10, 0, 0, 10));
            obstacleTab.setContent(obstacleTabContents);
            //
            currentObstacles[selectedRunway] = null;
            controller.setObstacle(null);
            updateUI();
        });

        obstacleInputBox = new VBox(onameBox, otypeBox, oheightBox, odistanceToCentreBox, oprompt6, odistancesBox, owidthBox, olengthBox, oblastAllowanceBox, oApplyButton, removeObstacleButton,objectErrorText);
        obstacleInputBox.setSpacing(4);
        obstacleInputBox.setPadding(new Insets(0, 0, 0, 0));
        obstacleTabContents = new VBox(obstacleRunwayHBox, addObstacleButton);
        obstacleTabContents.setSpacing(4);
        obstacleTabContents.setPadding(new Insets(10, 0, 0, 10));
        obstacleTab.setContent(obstacleTabContents);

        //XML tab layout
        ChooseFileButton fileButton1 = new ChooseFileButton("Choose File", 0);
        fileButton1.addListener(this);
        ChooseDirectoryButton directoryButton1 = new ChooseDirectoryButton("Choose Directory", 1);
        directoryButton1.addListener(this);
        ChooseFileButton fileButton2 = new ChooseFileButton("Choose File", 2);
        fileButton2.addListener(this);
        ChooseDirectoryButton directoryButton2 = new ChooseDirectoryButton("Choose Directory", 3);
        directoryButton2.addListener(this);
        Label label1 = new Label("Airport Input: ");
        Label label2 = new Label("Airport Output: ");
        Label label3 = new Label("Obstacle Input: ");
        Label label4 = new Label("Obstacle Output: ");
        HBox airportInput = new HBox(label1,fileButton1);
        HBox airportOutput = new HBox(label2,directoryButton1);
        HBox ObstacleInput = new HBox(label3,fileButton2);
        HBox ObstacleOutput = new HBox(label4,directoryButton2);
        VBox xmlTabContents = new VBox(airportInput,airportOutput,ObstacleInput,ObstacleOutput);
        xmlTabContents.setSpacing(10);
        xmlTabContents.setPadding(new Insets(10, 0, 0, 10));
        xmlTab.setContent(xmlTabContents);

        //Presets Tab Layout
        Label airportPresetsLabel = new Label("Airport Presets:");
        airportPresetsLabel.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        Button preset1Button = new Button("London Heathrow");
        preset1Button.setOnAction(event -> {
            controller.inputAirportXML(this.getClass().getClassLoader().getResourceAsStream("XML_Files/LHR.xml"));
            controller.sendNotification("!Preset 'London Heathrow' applied.");
        });
        Button preset2Button = new Button("London Luton");
        preset2Button.setOnAction(event -> {
            controller.inputAirportXML(this.getClass().getClassLoader().getResourceAsStream("XML_Files/LTN.xml"));
            controller.sendNotification("!Preset 'London Luton' applied.");
        });
        Label obstaclePresetsLabel = new Label("Obstacle Presets:");
        obstaclePresetsLabel.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        Button preset3Button = new Button("Boeing 747-400");
        preset3Button.setOnAction(event -> {
            controller.inputObstacleXML(this.getClass().getClassLoader().getResourceAsStream("XML_Files/Boeing 747-400.xml"));
            controller.sendNotification("!Preset 'Boeing 747-400' applied.");
            oApplyButton.fire();
        });
        Button preset4Button = new Button("Airbus ACJ318");
        preset4Button.setOnAction(event -> {
            controller.inputObstacleXML(this.getClass().getClassLoader().getResourceAsStream("XML_Files/Airbus ACJ318.xml"));
            controller.sendNotification("!Preset 'Airbus ACJ318' applied.");
            oApplyButton.fire();
        });
        Button preset5Button = new Button("Large Vehicle");
        preset5Button.setOnAction(event -> {
            controller.inputObstacleXML(this.getClass().getClassLoader().getResourceAsStream("XML_Files/Large Vehicle.xml"));
            controller.sendNotification("!Preset 'Large Vehicle' applied.");
            oApplyButton.fire();
        });
        VBox presetButtons = new VBox(airportPresetsLabel,preset1Button,preset2Button,obstaclePresetsLabel,preset3Button,preset4Button,preset5Button);
        presetButtons.setPadding(new Insets(10, 10, 10, 10));
        presetButtons.setSpacing(10);
        presetsTab.setContent(presetButtons);

        //Settings Tab
        Text colourText = new Text("Colours:");
        Button colourButton1 = new Button("Change Colour!");
        colourButton1.setOnAction(event -> {
            controller.setColorVar(Color.GRAY);
            if(colorChangeListener != null){
                colorChangeListener.accept(Color.GRAY);
            }
        });
        Button colourButton2 = new Button("Change Colour!");
        colourButton2.setOnAction(event -> {
            controller.setColorVar(Color.DARKCYAN);
            if(colorChangeListener != null){
                colorChangeListener.accept(Color.DARKCYAN);
            }
        });
        Button colourButton3 = new Button("Change Colour!");
        colourButton3.setOnAction(event -> {
            controller.setColorVar(Color.BLUE);
            if(colorChangeListener != null){
                colorChangeListener.accept(Color.BLUE);
            }
        });
        Label button1label = new Label("Achromatopsia Colour Scheme: ");
        Label button2label = new Label("Default Colour Scheme: ");
        Label button3label = new Label("Alternative Colour Scheme: ");
        HBox colour1 = new HBox(button1label,colourButton1);
        HBox colour2 = new HBox(button2label,colourButton2);
        HBox colour3 = new HBox(button3label,colourButton3);
        //
        Text helpSectionText = new Text("Help:");
        Text helpButtonLabel = new Text("For help using this application, click the \nbutton below:");
        helpSectionText.setFont(new Font(16));
        colourText.setFont(new Font(16));
        VBox colourSection = new VBox(colourText,colour1,colour2,colour3);
        colourSection.setPadding(new Insets(5, 5 ,5, 5));
        colourSection.setSpacing(4);
        //
        Button infoButton = new Button("help");
        infoButton.setOnAction(e -> InfoPage.display());
        VBox helpSection = new VBox(helpSectionText,helpButtonLabel,infoButton);
        helpSection.setPadding(new Insets(5, 5, 5, 5));
        helpSection.setSpacing(4);
        //
        Text emailText = new Text("If you have any issues please let us know " +
                "at the \nemail below:");
        Hyperlink emailAddress = new Hyperlink("example@example.com");
        //email link
        emailAddress.setOnAction(event -> {
            String email = "example@example.com";
            String subject = "Runway tool help";
            String body = "";
            try {
                subject = URLEncoder.encode(subject, "UTF-8");
                body = URLEncoder.encode(body, "UTF-8");
                String mailto = String.format("mailto:%s?subject=%s&body=%s", email, subject, body);
                Desktop.getDesktop().browse(new URI(mailto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        ToggleButton toggleButton1 = new ToggleButton("Toggle TTS");
//        isSelected = toggleButton1.isSelected();
//        if(isSelected){
//            System.out.println("TTS On");
//        }
//        ToggleButton toggleButton1 = new ToggleButton("TTS Off");
//        toggleButton1.setSelected(isSelected);
//
//        toggleButton1.setOnAction(event -> {
//            isSelected = toggleButton1.isSelected();
//            if(isSelected){toggleButton1.setText("TTS On");
//            }else{toggleButton1.setText("TTS Off");}
//            System.out.println("Toggle switched " + (isSelected ? "ON" : "OFF"));
//        });



        //Settings Tab layout
        VBox settingsContent = new VBox(colourSection,helpSection,emailText,emailAddress);
        settingsContent.setPadding(new Insets(8, 10, 8, 10));
        settingsContent.setSpacing(7);
        settingsTab.setContent(settingsContent);
        // Pane layout
        tabPane.getTabs().addAll(runwaysTab, obstacleTab, xmlTab, presetsTab, settingsTab);
        // Set the TabPane to fill the height of the VBox
        tabPane.setMaxHeight(Double.MAX_VALUE);
        // Add the TabPane to the VBox
        this.getChildren().add(tabPane);
        //Background Colour
        this.setStyle("-fx-background-color: lightGrey;");
        setValues(controller.getPresetAirportValues());
        setOValues(controller.getPresetObstacleValues());
    }



    private void odistanceChanged(int num, String oldValue, String newValue) {
        if (!oldValue.equals("") && oldValue.substring(oldValue.length() - 1).equals("␣")) {
            return;
        }
        if (!newValue.equals("") && newValue.substring(newValue.length() - 1).equals("␣")) {
            setOdistances(num, newValue.substring(0, newValue.length() - 1));
            return;
        }
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        if (!newValue.equals("")) {
            if (!pattern.matcher(newValue).matches()) {
                setOdistances(num, oldValue);
            } else if (oldValue.equals("0")) {
                setOdistances(num, newValue.substring(1));
            } else {
                float length = controller.getActiveRunwayStrip().getLength();
                if (Float.parseFloat(newValue) > length) {
                    setOdistances(num, String.valueOf(Math.round(length)));
                } else {
                    setOdistances((num+1)%2, String.valueOf(Math.round(length - Float.parseFloat(newValue))) + "␣");
                }
            }
        } else {
            setOdistances(num, "0");
        }
    }
    private void setOdistances(int num, String text) {
        switch (num) {
            case 0 -> odistance0.setText(text);
            case 1 -> odistance1.setText(text);
            default -> System.out.println("Invalid index given");
        }
    }

    public void actionPerformed(ActionEvent event) {
        switch (event.getID()) {
            case 0:
                controller.inputAirportXML(new File(event.getActionCommand()));
                break;
            case 1:
                controller.outputAirportXML(event.getActionCommand());
                break;
            case 2:
                controller.inputObstacleXML(new File(event.getActionCommand()));
                break;
            case 3:
                controller.outputObstacleXML(event.getActionCommand());
                break;
        }
    }

    private void switchRunway(String oldValue, String newValue) {
        if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
            switch (selectedRunway) {
                case 0 -> selectedRunway = 1;
                case 1 -> selectedRunway = 0;
            }
            String designator1 = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList.get(0).designator.getStringDesignator();
            String designator2 = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList.get(1).designator.getStringDesignator();
            runwayNameLabel.setText("Input for runway " + designator1 + "/" + designator2 + ":");
            designator1Input.setText(designator1);
            designator2Input.setText(designator2);
            oldDesignators[0] = designator1;
            oldDesignators[1] = designator2;
            oldLength = String.valueOf(currentParameters.runwayStripParameters[selectedRunway].length);
            oldResaLength = String.valueOf(currentParameters.runwayStripParameters[selectedRunway].RESALength);
            resaLengthInput.setText(String.valueOf(currentParameters.runwayStripParameters[selectedRunway].RESALength));
            runwayLengthInput.setText(String.valueOf(currentParameters.runwayStripParameters[selectedRunway].length));
            RunwayParameters runway1 = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList.get(0);
            RunwayParameters runway2 = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList.get(1);
            updateTableValues(runway1, runway2);
            table.setLabels(designator1, designator2);
            controller.setActiveRunwayStrip(controller.getRunwayStrips()[selectedRunway]);
            obstacleTabRunwayText.setText(newValue);
            if (currentObstacles[selectedRunway] != null) {
                setOValues(currentObstacles[selectedRunway]);
                //reshowObstacleMenu();
            } else {
                removeObstacleButton.fire();
            }
        }
    }
    private void reshowObstacleMenu() {
        obstacleTabContents = new VBox(obstacleRunwayHBox, obstacleInputBox);
        obstacleTabContents.setSpacing(2);
        obstacleTabContents.setPadding(new Insets(10, 0, 0, 10));
        obstacleTab.setContent(obstacleTabContents);
    }
    private String[][] updateTableValues(RunwayParameters runway1, RunwayParameters runway2) {
        String[][] values = new String[5][2];
        values[0][0] = String.valueOf(Math.round(Float.valueOf(runway1.TORA)));
        values[0][1] = String.valueOf(Math.round(Float.valueOf(runway2.TORA)));
        values[1][0] = String.valueOf(Math.round(Float.valueOf(runway1.TODA)));
        values[1][1] = String.valueOf(Math.round(Float.valueOf(runway2.TODA)));
        values[2][0] = String.valueOf(Math.round(Float.valueOf(runway1.ASDA)));
        values[2][1] = String.valueOf(Math.round(Float.valueOf(runway2.ASDA)));
        values[3][0] = String.valueOf(Math.round(Float.valueOf(runway1.LDA)));
        values[3][1] = String.valueOf(Math.round(Float.valueOf(runway2.LDA)));
        values[4][0] = String.valueOf(Math.round(Float.valueOf(runway1.displacedThreshold)));
        values[4][1] = String.valueOf(Math.round(Float.valueOf(runway2.displacedThreshold)));
        table.setValues(values);
        return values;
    }

    public void setValues(AirportParameters aParams) {
        selectedRunway = 0;
        float tempOldLength = -1;
        if (currentParameters != null) {
            tempOldLength = currentParameters.runwayStripParameters[selectedRunway].length;
        }
        currentParameters = aParams;
        airportNameInput.setText(aParams.name);
        runwayNoInput.setText(String.valueOf(aParams.runwayStripParameters.length));
        String designator1 = aParams.runwayStripParameters[0].runwayParametersList.get(0).designator.getStringDesignator();
        String designator2 = aParams.runwayStripParameters[0].runwayParametersList.get(1).designator.getStringDesignator();
        runwayNameLabel.setText("Input for runway " + designator1 + "/" + designator2 + ":");
        designator1Input.setText(designator1);
        designator2Input.setText(designator2);
        //Make smaller designator on left hand side
        try {
            Designator Convdesignator1 = new Designator(designator1Input.getText());
            Designator Convdesignator2 = new Designator(designator2Input.getText());
            if (Convdesignator1.getDegree() > Convdesignator2.getDegree()) {
                String tempDes = designator2Input.getText();
                designator2Input.setText(designator1Input.getText());
                designator1Input.setText(tempDes);
            }
        } catch(Exception e) { System.out.println("SidebarTabPane SetValues() error: " + e); }
        oldDesignators[0] = designator1;
        oldDesignators[1] = designator2;
        oldRunwayNo = String.valueOf(aParams.runwayStripParameters.length);
        oldLength = String.valueOf(aParams.runwayStripParameters[0].length);
        oldResaLength = String.valueOf(aParams.runwayStripParameters[0].RESALength);
        resaLengthInput.setText(String.valueOf(aParams.runwayStripParameters[0].RESALength));
        if (tempOldLength != -1 && Float.compare(tempOldLength, aParams.runwayStripParameters[0].length) != 0) {//If runwayLength changed
            odistance0.setText(String.valueOf(aParams.runwayStripParameters[0].length / 2));
            odistance1.setText(String.valueOf(aParams.runwayStripParameters[0].length / 2));
            if (controller.getActiveObstacle() != null) {//If obstacle active
                oApplyButton.fire();
            }
        }
        runwayLengthInput.setText(String.valueOf(aParams.runwayStripParameters[0].length));
        RunwayParameters runway1 = aParams.runwayStripParameters[0].runwayParametersList.get(0);
        RunwayParameters runway2 = aParams.runwayStripParameters[0].runwayParametersList.get(1);
        oldTable = updateTableValues(runway1, runway2);
        table.setLabels(designator1, designator2);
        runwayDropdown.getItems().clear();
        runwayDropdown.getItems().add(designator1 + "/" + designator2);
        if (aParams.runwayStripParameters.length == 2) {
            String designator3 = aParams.runwayStripParameters[1].runwayParametersList.get(0).designator.getStringDesignator();
            String designator4 = aParams.runwayStripParameters[1].runwayParametersList.get(1).designator.getStringDesignator();
            runwayDropdown.getItems().add(designator3 + "/" + designator4);
        }
        runwayDropdown.setValue(runwayDropdown.getItems().get(0));
        obstacleTabRunwayText.setText(designator1 + "/" + designator2);
        if (firstLoad) {
            firstLoad = false;
        } else {
            //updateUI();
            applyButton.fire();
        }
    }
    //For xml, presets and initial preset
    public void setOValues(Obstacle obstacle) {
        oname.setText(obstacle.getName());
        switch (obstacle.getClass().getName()) {
            case "runwaytool.model.FOD" -> otype.setValue(otype.getItems().get(1));
            case "runwaytool.model.GroundedAircraft" -> otype.setValue(otype.getItems().get(0));
        }
        oheight.setText(String.valueOf(obstacle.getHeight()));
        oldOheight = oheight.getText();
        owidth.setText(String.valueOf(obstacle.getWidth()));
        oldOwidth = owidth.getText();
        olength.setText(String.valueOf(obstacle.getLength()));
        oldOlength = olength.getText();
        oblastAllowance.setText(String.valueOf(obstacle.getBlastAllowance()));
        oldOblastAllowance = oblastAllowance.getText();
        odistanceToCentre.setText(String.valueOf(obstacle.getDistanceFromCentre()));
        oldOdistanceToCentre = odistanceToCentre.getText();
        float dist0 = Math.round(obstacle.getDistance0f());
        float dist1 = Math.round(obstacle.getDistance1f());
        float length = Math.round(controller.getActiveRunwayStrip().getLength());
        if (length % 2 != 0) {
            length -= 1;
        }
        if (dist0 == 0 && dist1 == 0) {
            odistance0.setText(String.valueOf(Math.round(length / 2)));
        } else {
            odistance0.setText(String.valueOf(dist0));
        }
        if (ofirstLoad) {
            ofirstLoad = false;
        } else {
            ArrayList<RunwayParameters> rParams = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList;
            obstacle.setDistance0(new Pair<>(rParams.get(0).designator, obstacle.getDistance0f()));
            obstacle.setDistance1(new Pair<>(rParams.get(1).designator, obstacle.getDistance1f()));
            currentObstacles[selectedRunway] = obstacle;
            updateUI();
            if (obstacle != null) {
                reshowObstacleMenu();
            }
        }
    }
    private void updateUI() {
        if (selectedRunway == 0) {
            controller.updateUI(currentParameters);
        } else {
            AirportParameters aParams = new AirportParameters();
            aParams.name = currentParameters.name;
            aParams.runwayStripParameters = new RunwayStripParameters[currentParameters.runwayStripParameters.length];
            if (currentParameters.runwayStripParameters.length == 2) {
                aParams.runwayStripParameters[0] = currentParameters.runwayStripParameters[1];
                aParams.runwayStripParameters[1] = currentParameters.runwayStripParameters[0];
            } else {
                aParams.runwayStripParameters[0] = currentParameters.runwayStripParameters[0];
            }
            controller.updateUI(aParams);
        }
    }

    private void updateModel() {
        try {
            RunwayStrip[] currentRStrips = controller.getRunwayStrips();
            AirportParameters aParams = new AirportParameters();
            aParams.name = airportNameInput.getText();
            aParams.runwayStripParameters = new RunwayStripParameters[Integer.parseInt(runwayNoInput.getText())];
            RunwayParameters rParam1 = new RunwayParameters(new Designator(designator1Input.getText()), Float.parseFloat(table.getValues()[0][0]), Float.parseFloat(table.getValues()[1][0]), Float.parseFloat(table.getValues()[2][0]), Float.parseFloat(table.getValues()[3][0]), Float.parseFloat(table.getValues()[4][0]));
            RunwayParameters rParam2 = new RunwayParameters(new Designator(designator2Input.getText()), Float.parseFloat(table.getValues()[0][1]), Float.parseFloat(table.getValues()[1][1]), Float.parseFloat(table.getValues()[2][1]), Float.parseFloat(table.getValues()[3][1]), Float.parseFloat(table.getValues()[4][1]));
            ArrayList<RunwayParameters> rParams = new ArrayList<RunwayParameters>();
            rParams.add(rParam1);
            rParams.add(rParam2);
            String runwayName = designator1Input.getText() + "/" + designator2Input.getText();
            if (runwayNoInput.getText().equals("2") && currentParameters.runwayStripParameters.length == 2) {
                aParams.runwayStripParameters[selectedRunway] = new RunwayStripParameters(runwayName, Float.parseFloat(runwayLengthInput.getText()), rParams, currentRStrips[selectedRunway].getRESAWidth(), Float.parseFloat(resaLengthInput.getText()), currentRStrips[selectedRunway].getCentreToClearedEnd(), currentRStrips[selectedRunway].getCentreToClearedMid(), currentRStrips[selectedRunway].getClearedEndMin(), currentRStrips[selectedRunway].getClearedEndMax()); //ALWAYS RETURNS SAME CTCE... VALUES
                aParams.runwayStripParameters[(selectedRunway+1)%2] = currentParameters.runwayStripParameters[(selectedRunway+1)%2];
            } else if(runwayNoInput.getText().equals("2")) {
                rParam1 = new RunwayParameters(new Designator(new Designator(designator1Input.getText()).getDegree(), DesignatorSuffix.L), Float.parseFloat(table.getValues()[0][0]), Float.parseFloat(table.getValues()[1][0]), Float.parseFloat(table.getValues()[2][0]), Float.parseFloat(table.getValues()[3][0]), Float.parseFloat(table.getValues()[4][0]));
                rParam2 = new RunwayParameters(new Designator(new Designator(designator2Input.getText()).getDegree(), DesignatorSuffix.R), Float.parseFloat(table.getValues()[0][1]), Float.parseFloat(table.getValues()[1][1]), Float.parseFloat(table.getValues()[2][1]), Float.parseFloat(table.getValues()[3][1]), Float.parseFloat(table.getValues()[4][1]));
                rParams = new ArrayList<RunwayParameters>();
                rParams.add(rParam1);
                rParams.add(rParam2);
                aParams.runwayStripParameters[0] = new RunwayStripParameters(runwayName, Float.parseFloat(runwayLengthInput.getText()), rParams, currentRStrips[0].getRESAWidth(), Float.parseFloat(resaLengthInput.getText()), currentRStrips[0].getCentreToClearedEnd(), currentRStrips[0].getCentreToClearedMid(), currentRStrips[0].getClearedEndMin(), currentRStrips[0].getClearedEndMax()); //ALWAYS RETURNS SAME CTCE... VALUES
                Designator newdes1 = new Designator(new Designator(designator1Input.getText()).getDegree(), DesignatorSuffix.R);
                Designator newdes2 = new Designator(new Designator(designator2Input.getText()).getDegree(), DesignatorSuffix.L);
                RunwayParameters rParam21 = new RunwayParameters(newdes1, Float.parseFloat(table.getValues()[0][0]), Float.parseFloat(table.getValues()[1][0]), Float.parseFloat(table.getValues()[2][0]), Float.parseFloat(table.getValues()[3][0]), Float.parseFloat(table.getValues()[4][0]));
                RunwayParameters rParam22 = new RunwayParameters(newdes2, Float.parseFloat(table.getValues()[0][1]), Float.parseFloat(table.getValues()[1][1]), Float.parseFloat(table.getValues()[2][1]), Float.parseFloat(table.getValues()[3][1]), Float.parseFloat(table.getValues()[4][1]));
                ArrayList<RunwayParameters> rParams2 = new ArrayList<RunwayParameters>();
                rParams2.add(rParam21);
                rParams2.add(rParam22);
                aParams.runwayStripParameters[1] = new RunwayStripParameters(newdes1.getStringDesignator() + "/" + newdes2.getStringDesignator(), Float.parseFloat(runwayLengthInput.getText()), rParams2, currentRStrips[0].getRESAWidth(), Float.parseFloat(resaLengthInput.getText()), currentRStrips[0].getCentreToClearedEnd(), currentRStrips[0].getCentreToClearedMid(), currentRStrips[0].getClearedEndMin(), currentRStrips[0].getClearedEndMax()); //ALWAYS RETURNS SAME CTCE... VALUES
                setValues(aParams);
            } else {
                aParams.runwayStripParameters[0] = new RunwayStripParameters(runwayName, Float.parseFloat(runwayLengthInput.getText()), rParams, currentRStrips[selectedRunway].getRESAWidth(), Float.parseFloat(resaLengthInput.getText()), currentRStrips[selectedRunway].getCentreToClearedEnd(), currentRStrips[selectedRunway].getCentreToClearedMid(), currentRStrips[selectedRunway].getClearedEndMin(), currentRStrips[selectedRunway].getClearedEndMax()); //ALWAYS RETURNS SAME CTCE... VALUES
            }
            controller.updateAirportModel(aParams);
            currentParameters = aParams;
        } catch(Exception exception) {
            exception.printStackTrace();
            //SEND ALERT VIA UI (Invalid values supplied)
        }
    }
    private void updateoModel() {
        Obstacle obstacle;
        ArrayList<RunwayParameters> rParamsList = currentParameters.runwayStripParameters[selectedRunway].runwayParametersList;
        switch (otype.getValue().toString()) {
            case "Foreign Object Debris" -> obstacle = new FOD(oname.getText(), Float.parseFloat(oheight.getText()), Float.parseFloat(odistanceToCentre.getText()),
                new Pair<>(rParamsList.get(0).designator, Float.parseFloat(odistance0.getText())),
                new Pair<>(rParamsList.get(1).designator, Float.parseFloat(odistance1.getText())),
                    Float.parseFloat(oblastAllowance.getText()), Float.parseFloat(owidth.getText()), Float.parseFloat(olength.getText()));
            case "Grounded Aircraft" -> obstacle = new GroundedAircraft(oname.getText(), Float.parseFloat(oheight.getText()), Float.parseFloat(odistanceToCentre.getText()),
                new Pair<>(rParamsList.get(0).designator, Float.parseFloat(odistance0.getText())),
                new Pair<>(rParamsList.get(1).designator, Float.parseFloat(odistance1.getText())),
                    Float.parseFloat(oblastAllowance.getText()), Float.parseFloat(owidth.getText()), Float.parseFloat(olength.getText()));
            default -> obstacle = currentObstacles[selectedRunway];
        }
        currentObstacles[selectedRunway] = obstacle;
        controller.setObstacle(obstacle);
    }

    private boolean checkInputValues() {
        try {
            Designator designator1 = new Designator(designator1Input.getText());
            Designator designator2 = new Designator(designator2Input.getText());
            if (designator1.getOppositeDesignator().getStringDesignator().equals(designator2.getStringDesignator())) {
                if (runwayNoInput.getText().equals("1") || runwayNoInput.getText().equals("2")) {
                    Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
                    if (pattern.matcher(runwayLengthInput.getText()).matches()) {
                        if (pattern.matcher(resaLengthInput.getText()).matches()) {
                            enforceRelations();
                            //Make smaller designator on left hand side
                            if (designator1.getDegree() > designator2.getDegree()) {
                                String tempDes = designator2Input.getText();
                                designator2Input.setText(designator1Input.getText());
                                designator1Input.setText(tempDes);
                                controller.sendNotification("Designators flipped to keep smallest angle on left.");
                            }
                            oldRunwayNo = runwayNoInput.getText();
                            if (Float.compare(currentParameters.runwayStripParameters[selectedRunway].length, Float.valueOf(runwayLengthInput.getText())) != 0) {
                                changedLength = true;
                            }
                            oldLength = runwayLengthInput.getText();
                            oldDesignators[0] = designator1Input.getText();
                            oldDesignators[1] = designator2Input.getText();
                            oldResaLength = resaLengthInput.getText();
                            oldTable = table.getValues();
                            controller.sendNotification("?All runway values successfully changed.");
                            return true;
                        } else {
                            resaLengthInput.setText(oldResaLength);
                            controller.sendNotification("Invalid 'RESA Length' provided, value change reverted.");
                        }
                    } else {
                        runwayLengthInput.setText(oldLength);
                        controller.sendNotification("Invalid runway 'Length' provided, value change reverted.");
                    }
                } else {
                    runwayNoInput.setText(oldRunwayNo);
                    controller.sendNotification("Invalid 'Number of Runways' provided, value change reverted. Allowed values: 1, 2.");
                }
            } else {
                designator1Input.setText(oldDesignators[0]);
                designator2Input.setText(oldDesignators[1]);
                controller.sendNotification("Invalid 'Designators' provided, value change reverted. Ensure angles are opposite.");
            }
        } catch(Exception exception) {
            controller.sendNotification("An error occurred, all runway values reverted.");
            revertValues();
            return false;
        }
        return false;
    }
    private boolean ocheckInputValues() {
        try {
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            if (pattern.matcher(oheight.getText()).matches() && pattern.matcher(odistanceToCentre.getText()).matches() &&
                    pattern.matcher(oblastAllowance.getText()).matches() && pattern.matcher(owidth.getText()).matches() && pattern.matcher(olength.getText()).matches()) {
                ArrayList<String> revertValues = new ArrayList<>();
                if (Float.parseFloat(oheight.getText()) > 200) {
                    oheight.setText("200");
                    revertValues.add("Height");
                }
                if (Float.parseFloat(oheight.getText()) < 0) {
                    oheight.setText("0");
                    if (!revertValues.contains("Height")) {
                        revertValues.add("Height");
                    }
                }
                if (Float.parseFloat(odistanceToCentre.getText()) > 150) {
                    odistanceToCentre.setText("150");
                    revertValues.add("Distance to Centreline");
                }
                if (Float.parseFloat(odistanceToCentre.getText()) < 0) {
                    odistanceToCentre.setText("0");
                    if (!revertValues.contains("Distance to Centreline")) {
                        revertValues.add("Distance to Centreline");
                    }
                }
                if (Float.parseFloat(oblastAllowance.getText()) > 800) {
                    oblastAllowance.setText("800");
                    revertValues.add("Blast Allowance");
                }
                if (Float.parseFloat(oblastAllowance.getText()) < 0) {
                    oblastAllowance.setText("0");
                    if (!revertValues.contains("Blast Allowance")) {
                        revertValues.add("Blast Allowance");
                    }
                }
                if (Float.parseFloat(owidth.getText()) > 150) {
                    owidth.setText("150");
                    revertValues.add("Width");
                }
                if (Float.parseFloat(owidth.getText()) < 0.5) {
                    owidth.setText("0.5");
                    if (!revertValues.contains("Width")) {
                        revertValues.add("Width");
                    }
                }
                if (Float.parseFloat(olength.getText()) > currentParameters.runwayStripParameters[selectedRunway].length) {
                    olength.setText(String.valueOf(currentParameters.runwayStripParameters[selectedRunway].length));
                    revertValues.add("Length");
                }
                if (Float.parseFloat(olength.getText()) < 0.5) {
                    olength.setText("0.5");
                    if (!revertValues.contains("Length")) {
                        revertValues.add("Length");
                    }
                }
                oldOheight = oheight.getText();
                oldOdistanceToCentre = odistanceToCentre.getText();
                oldOblastAllowance = oblastAllowance.getText();
                oldOwidth = owidth.getText();
                oldOlength = olength.getText();
                if (revertValues.size() > 0) {
                    String revert = "";
                    for (String s : revertValues) {
                        revert += " " + s + ",";
                    }
                    controller.sendNotification("+Invalid obstacle values reverted:" + revert.substring(0, revert.length()-1) + ".");
                } else {
                    controller.sendNotification("?All obstacle values successfully changed.");
                }
                return true;
            } else {
                controller.sendNotification("Non-numeric values provided to obstacle tab, all values reverted.");
                oheight.setText(oldOheight);
                odistanceToCentre.setText(oldOdistanceToCentre);
                oblastAllowance.setText(oldOblastAllowance);
                owidth.setText(oldOwidth);
                olength.setText(oldOlength);
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }
    private void enforceRelations() {
        ArrayList<String> revertString = new ArrayList<>();
        String[][] newValues = table.getValues();
        float newDis1 = Float.parseFloat(newValues[4][0]);
        float newDis2 = Float.parseFloat(newValues[4][1]);
        if ((newDis1 + newDis2) > Float.parseFloat(runwayLengthInput.getText())) {
            newDis1 = Float.parseFloat(oldTable[4][0]);
            newDis2 = Float.parseFloat(oldTable[4][1]);
            newValues[4][0] = String.valueOf(Math.round(newDis1));
            newValues[4][1] = String.valueOf(Math.round(newDis2));
            revertString.add("DThresh");
        }
        for (int i = 0; i < 2; i++) {
            //Length
            float newLength = Float.parseFloat(runwayLengthInput.getText());
            if (newLength < 720) {
                newLength = 720;
                if (!revertString.contains("Length")) {
                    revertString.add("Length");
                }
            }
            runwayLengthInput.setText(String.valueOf(newLength));
            //Displaced Threshold
            float newDis = Float.parseFloat(newValues[4][i]);
            if (newDis > newLength) {
                newDis = newLength;
                if (!revertString.contains("DThresh")) {
                    revertString.add("DThresh");
                }
            }
            newValues[4][i] = String.valueOf(Math.round(newDis));
            //LDA
            float oldDis = Float.parseFloat(oldTable[4][i]);
            float newLDA = Float.parseFloat(newValues[3][i]);
            if (newDis != oldDis) {
                newLDA = newLDA - (newDis - oldDis);
            }
            if (newLDA > (newLength - newDis)) {
                newLDA = newLength - newDis;
                if (!revertString.contains("LDA")) {
                    revertString.add("LDA");
                }
            }
            newValues[3][i] = String.valueOf(Math.round(newLDA));
            //TORA
            float newTORA = Float.parseFloat(newValues[0][i]);
            if (newTORA > newLength) {
                newTORA = newLength;
                if (!revertString.contains("TORA")) {
                    revertString.add("TORA");
                }
            }
            newValues[0][i] = String.valueOf(Math.round(newTORA));
            //ASDA
            float newASDA = Float.parseFloat(newValues[2][i]);
            float stopway = 60;
            newASDA = newTORA + stopway;
            if (newASDA > (newTORA + stopway)) {
                //
            }
            newValues[2][i] = String.valueOf(Math.round(newASDA));
            //TODA
            float oldTODA = Float.parseFloat(oldTable[1][i]);
            float newTODA = Float.parseFloat(newValues[1][i]);
            float oldLengthf = Float.parseFloat(oldLength);
            if (newTODA == oldTODA && oldLengthf != newLength) {
                newTODA = newTODA - (oldLengthf - newLength);
                if (!revertString.contains("TODA")) {
                    revertString.add("TODA");
                }
            }
            newValues[1][i] = String.valueOf(Math.round(newTODA));
        }
        if (revertString.size() > 0) {
            String revert = "";
            for (String s : revertString) {
                revert += " " + s + ",";
            }
            controller.sendNotification("+Invalid table values reverted:" + revert.substring(0, revert.length()-1) + ".");
        } else {
            controller.sendNotification("?All table values successfully changed.");
        }
        table.setValues(newValues);
    }
    private void revertValues() {
        designator1Input.setText(oldDesignators[0]);
        designator2Input.setText(oldDesignators[1]);
        runwayNoInput.setText(oldRunwayNo);
        runwayLengthInput.setText(oldLength);
        resaLengthInput.setText(oldResaLength);
        table.setValues(oldTable);
    }

    public AirportParameters getCurrentParameters() {
        return currentParameters;
    }

    public int getSelectedRunway() {
        return selectedRunway;
    }

    public void setColorChangeListener(Consumer<Color> listener){
        this.colorChangeListener = listener;
    }

}