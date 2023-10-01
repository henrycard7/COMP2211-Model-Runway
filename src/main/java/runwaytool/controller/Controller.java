package runwaytool.controller;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.util.Pair;
import runwaytool.model.*;
import runwaytool.view.Window;
import java.io.File;
import java.io.InputStream;

public class Controller {
  public Window window;
  private RunwayStrip[] runwayStrips;
  private Obstacle[] obstacles;
  private RunwayStrip activeRunwayStrip;
  private Color colorVar = Color.DARKCYAN;
  private String calculationText = "";
  private String airportName = "New Airport";
  LogicalRunway activeLogicalRunway;
  int logicalRunwayIndex;
  XMLParser parser;

  public Controller() {
    parser = new XMLParser();
    //Load preset runways
    runwayStrips = getPresetRunwayStrips();
    activeRunwayStrip = runwayStrips[0];
    obstacles = new Obstacle[2];
    obstacles[0] = null;
    obstacles[1] = null;
    setActiveLogicalRunway(activeRunwayStrip.getLogicalRunways().get(0));
  }

  public void inputAirportXML(File file) {
    try {
      AirportParameters aParams = parser.parseAirportXML(file);
      inputAirportHelper(aParams);
      window.sendValuesToSidebar(aParams);
      sendNotification("!Airport XML imported successfully.");
    } catch (Exception exception) {
      System.out.println("ERROR READING FILE: " + exception);
      sendNotification("!Airport XML import failed, invalid XML file provided.");
    }
  }
  public void inputAirportXML(InputStream inputStream) {
    try {
      AirportParameters aParams = parser.parseAirportXML(inputStream);
      inputAirportHelper(aParams);
      window.sendValuesToSidebar(aParams);
    } catch (Exception exception) {
      System.out.println("ERROR READING FILE: " + exception);
      exception.printStackTrace();
    }
  }
  private void inputAirportHelper(AirportParameters aParams) {
    int currentStripIndex;
    if (activeRunwayStrip.equals(runwayStrips[0])) {
      currentStripIndex = 0;
    } else {
      currentStripIndex = 1;
    }
    RunwayStripParameters[] params = aParams.runwayStripParameters;
    airportName = aParams.name;
    if (params.length == 2) {
      if (params[0] != null) {
        runwayStrips[0] = new RunwayStrip(params[0]);
      } else {
        runwayStrips[0] = null;
      }
      if (params[1] != null) {
        runwayStrips[1] = new RunwayStrip(params[1]);
      } else {
        runwayStrips[1] = null;
      }
    } else {
      runwayStrips[0] = new RunwayStrip(params[0]);
      runwayStrips[1] = null;
    }
    setActiveRunwayStrip(runwayStrips[currentStripIndex]);
  }

  public void outputAirportXML(String directory) {
    try {
      parser.createAirportXML(new File(directory + "\\UserAir_" + airportName + ".xml"), runwayStrips, airportName);
      sendNotification("!Airport XML saved successfully.");
    } catch (Exception exception) {
      System.out.print("ERROR CREATING FILE: " + exception);
      sendNotification("!Airport XML could not be saved.");
    }
  }

  public void outputObstacleXML(String directory) {
    try {
      parser.createObstacleXML(new File(directory + "\\UserObs_" + getActiveObstacle().getName() + ".xml"), getActiveObstacle());
      sendNotification("!Obstacle XML saved successfully.");
    } catch (Exception exception) {
      System.out.print("ERROR CREATING FILE: " + exception);
      sendNotification("!Obstacle XML could not be saved.");
    }
  }

  public void inputObstacleXML(File file) {
    try {
      Obstacle obstacle = parser.parseObstacleXML(file);
      setObstacle(obstacle);
      window.sendValuesToSidebar(obstacle);
      sendNotification("!Obstacle XML imported successfully.");
    } catch (Exception exception) {
      System.out.println("ERROR READING FILE: " + exception);
      sendNotification("!Obstacle XML import failed, invalid XML file provided.");
    }
  }
  public void inputObstacleXML(InputStream inputStream) {
    try {
      Obstacle obstacle = parser.parseObstacleXML(inputStream);
      setObstacle(obstacle);
      window.sendValuesToSidebar(obstacle);
    } catch (Exception exception) {
      System.out.println("ERROR READING FILE: " + exception);
    }
  }

  public void updateAirportModel(AirportParameters aParams) {
    inputAirportHelper(aParams);
  }

  public RunwayStripParameters recalculateRunwayStripParameters() throws Exception{
    if(!ShouldRecalculateRunwayStripParameters()){
      throw new Exception("Runway strip recalculations not needed");
    }
    RunwayStripParameters stripParameters = new RunwayStripParameters();
    stripParameters.name = getActiveRunwayStrip().getRunwayStripName();
    stripParameters.length = getActiveRunwayStrip().getLength();
    stripParameters.RESAWidth = getActiveRunwayStrip().getRESAWidth();
    stripParameters.RESALength = getActiveRunwayStrip().getRESALength();
    stripParameters.centreToClearedEnd = 75f;
    stripParameters.centreToClearedMid = 105f;
    stripParameters.clearedEndMin = 150f;
    stripParameters.clearedEndMax = 300f;

    ArrayList<RunwayParameters> runwayParametersArrayList = getActiveRunwayStrip().getParameters().runwayParametersList;
    stripParameters.runwayParametersList = new ArrayList<>();

    //Print string calculations to console as well
    System.out.println("TESTING RECALC PARAMS");
    Designator designator0 = runwayParametersArrayList.get(0).designator;
    RunwayParameters recalcParams0 = recalculateRunwayParameters(designator0);
    System.out.println(recalculateRunwayParametersString(designator0));

    Designator designator1 = runwayParametersArrayList.get(1).designator;
    RunwayParameters recalcParams1 = recalculateRunwayParameters(designator1);
    System.out.println(recalculateRunwayParametersString(designator1));

    stripParameters.runwayParametersList.add(recalcParams0);
    stripParameters.runwayParametersList.add(recalcParams1);
    return stripParameters;

  }

  public boolean ShouldRecalculateRunwayStripParameters(){
    return getActiveRunwayStrip().ShouldRecalculateRunwayStripParameters(getActiveObstacle());
  }


  /**
   * Given an obstacle and the direction, recalculate the parameters of the active runway.
   * @param startingDesignator designator the aircraft starts from. Either from taking off from
   *                           this point or landing towards this point.
   */
  public RunwayParameters recalculateRunwayParameters(Designator startingDesignator) {
    //Active obstacle set from UI
    return activeRunwayStrip.recalculateRunwayParameters(startingDesignator, getActiveObstacle());
  }
  /**
   * Given an obstacle and the direction, recalculate the parameters of the active runway.
   * @param startingDesignatorString string designator the aircraft starts from.
   */
  public RunwayParameters recalculateRunwayParameters(String startingDesignatorString) {
    //Active obstacle set from UI
    Designator startingDesignator = null;
    try {
      startingDesignator = new Designator(startingDesignatorString);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return activeRunwayStrip.recalculateRunwayParameters(startingDesignator, getActiveObstacle());
  }

  /**
   * Given an obstacle and the direction, return the string for all the parameter recalculations.
   * @param startingDesignator designator the aircraft starts from. Either from taking off from
   *                           this point or landing towards this point.
   */
  public String recalculateRunwayParametersString(Designator startingDesignator){
    //String of calculations
    return activeRunwayStrip.recalculateRunwayParametersString(startingDesignator, getActiveObstacle());
  }
  /**
   * Given an obstacle and the direction, return the string for all the parameter recalculations.
   * @param startingDesignatorString string designator the aircraft starts from.
   */
  public String recalculateRunwayParametersString(String startingDesignatorString) {
    //String of calculations
    Designator startingDesignator = null;
    try {
      startingDesignator = new Designator(startingDesignatorString);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return activeRunwayStrip.recalculateRunwayParametersString(startingDesignator, getActiveObstacle());
  }

  public AirportParameters getPresetAirportValues() {
    AirportParameters aParams = new AirportParameters();
    aParams.name = airportName;
    //RunwayStripParameters rStripParams1 = new RunwayStripParameters();
    aParams.runwayStripParameters = new RunwayStripParameters[2];
    aParams.runwayStripParameters[0] = runwayStrips[0].getParameters();
    aParams.runwayStripParameters[1] = runwayStrips[1].getParameters();
    return aParams;
  }

  public Obstacle getPresetObstacleValues() {
    return new FOD("Small Vehicle", 4, 0,
        new Pair<Designator, Float>(activeRunwayStrip.getLogicalRunways().get(0).getDesignator(),0f),
        new Pair<Designator, Float>(activeRunwayStrip.getLogicalRunways().get(1).getDesignator(), 0f),
        500, 3, 5);
  }

  public void updateUI(AirportParameters aParams) {
    window.updateUI(aParams);
  }
  public void updateUIRecalculations(){
    try{
      RunwayStripParameters recalcParams = recalculateRunwayStripParameters();
      window.updateUIRecalculations(recalcParams);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  private RunwayStrip[] getPresetRunwayStrips() {
    //Initialise runwayStrips array and load startup preset runways
    RunwayStrip[] runwayStrips = new RunwayStrip[2];
    runwayStrips[0] = new RunwayStrip("09L/27R");
    runwayStrips[0].addRunway(new LogicalRunway(new Designator(9, DesignatorSuffix.L)));
    runwayStrips[0].addRunway(new LogicalRunway(new Designator(27, DesignatorSuffix.R)));
    runwayStrips[1] = new RunwayStrip("09R/27L");
    runwayStrips[1].addRunway(new LogicalRunway(new Designator(9, DesignatorSuffix.R)));
    runwayStrips[1].addRunway(new LogicalRunway(new Designator(27, DesignatorSuffix.L)));

    //activeLogicalRunway = runwayStrips[0].getLogicalRunways().get(0);

    return runwayStrips;
  }

  /**
   * Increment the active logical runway index
   * @return logical runway index
   */
  public int incrementLogicalRunwayIndex(){
    logicalRunwayIndex++;
    if(logicalRunwayIndex >= activeRunwayStrip.getLogicalRunways().size()){
      logicalRunwayIndex = 0;
    }
    return logicalRunwayIndex;
  }
  public int setLogicalRunwayIndex(int index){
    logicalRunwayIndex = index;
    return logicalRunwayIndex;
  }
  public int getLogicalRunwayIndex() {
    return logicalRunwayIndex;
  }




  /**
   * Set the runway strips list.
   * <p>
   * Call this function when the 'apply' button is pressed.
   * LogicalRunways will be constructed using the constructor function from the properties of the
   * table of inputs.
   * RunwayStrip objects will be constructed using the constructor function, add list of logical
   * runways to it.
   * List of RunwayStrips built and then assigned to the runwayStrips of the Controller.
   *
   * @param runwayStrips list of runway strips
   */
  public void setRunwayStrips(RunwayStrip[] runwayStrips) {
    this.runwayStrips = runwayStrips;
  }
  public RunwayStrip[] getRunwayStrips() {
    return this.runwayStrips;
  }

  public void setActiveRunwayStrip(RunwayStrip runwayStrip) {
    activeRunwayStrip = runwayStrip;
    System.out.println("Active runway strip set to " + activeRunwayStrip.getRunwayStripName());
  }
  public void setActiveLogicalRunway(LogicalRunway logicalRunway) {
    activeLogicalRunway = logicalRunway;
    System.out.println("Active logical runway set to " + activeLogicalRunway.getDesignator().getStringDesignator());
  }

  public RunwayStrip getActiveRunwayStrip() {
    return activeRunwayStrip;
  }
  public LogicalRunway getActiveLogicalRunway() {
    activeLogicalRunway = getActiveRunwayStrip().getLogicalRunways().get(logicalRunwayIndex);
    return activeLogicalRunway;
  }

  public Obstacle getActiveObstacle() {
    if (activeRunwayStrip == runwayStrips[0]) {
      return obstacles[0];
    } else if (activeRunwayStrip == runwayStrips[1]) {
      return obstacles[1];
    } else {
      return null;
    }
  }
  public Obstacle[] getObstacles() {
    return obstacles;
  }
  public void setObstacle(Obstacle obstacle) {
    if (activeRunwayStrip == runwayStrips[1]) {
      this.obstacles[1] = obstacle;
    } else {
      this.obstacles[0] = obstacle;
    }

    setCalculationText(recalculateRunwayParametersString(getActiveLogicalRunway().getDesignator()));
  }

    public void setCalculationText(String calculationText) {
        this.calculationText = calculationText;
    }

    public String getCalculationText() {
        return calculationText;
    }

    public void setColorVar(Color color){
        this.colorVar=color;
    }
    public javafx.scene.paint.Color getColorVar(){
      return this.colorVar;
    }

    public void sendNotification(String message) {
      window.sendNotification(message);
    }
}
