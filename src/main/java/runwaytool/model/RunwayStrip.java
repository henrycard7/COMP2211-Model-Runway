package runwaytool.model;

import java.util.ArrayList;

public class RunwayStrip {
  private final String name;
  private final ArrayList<LogicalRunway> runwaysArrayList;
  private float length = 3902f;
  //Should be at least 2x runway width
  private float RESAWidth = 100f;
  private float RESALength = 240f;
  private final int ALS = 50;
  private final int TOCS = 50;
  private final float stripEnd = 60f;
  private float centreToClearedEnd = 75f;
  private float centreToClearedMid = 105f;
  private float clearedEndMin = 150f;
  private float clearedEndMax = 300f;
  private float clearway;
  private float stopway;
  private float  TOCSslope;
  public RunwayStrip(String name) {
    this.name = name;
    this.runwaysArrayList = new ArrayList<>();
  }
  public RunwayStrip(String name, float length, float resa, float ctce, float ctcm, float cemin, float cemax) {
    this.name = name;
    this.length = length;
    this.RESALength = resa;
    this.centreToClearedEnd = ctce;
    this.centreToClearedMid = ctcm;
    this.clearedEndMin = cemin;
    this.clearedEndMax = cemax;
    this.runwaysArrayList = new ArrayList<>();
  }
  public RunwayStrip(RunwayStripParameters params) {
    this.name = params.name;
    this.length = params.length;
    this.RESALength = params.RESALength;
    this.RESAWidth = params.RESAWidth;
    this.centreToClearedEnd = params.centreToClearedEnd;
    this.centreToClearedMid = params.centreToClearedMid;
    this.clearedEndMin = params.clearedEndMin;
    this.clearedEndMax = params.clearedEndMax;
    this.runwaysArrayList = new ArrayList<>();
    ArrayList<RunwayParameters> runwayParams = params.runwayParametersList;
    for (RunwayParameters r : runwayParams) {
      addRunway(r);
    }
  }

  public ArrayList<LogicalRunway> getLogicalRunways() {
    return runwaysArrayList;
  }

  public LogicalRunway getLogicalRunway(String designatorString){
    for (var runway: runwaysArrayList) {
      if(runway.getDesignator().getStringDesignator().equals(designatorString)){
        return runway;
      }
    }
    System.out.println("Error: no such runway with designator "+designatorString);
    return null;
  }

  public LogicalRunway getLogicalRunway(Designator designator){
    return getLogicalRunway(designator.getStringDesignator());
  }

  public void addRunway(LogicalRunway logicalRunway){
    runwaysArrayList.add(logicalRunway);
  }
  public void addRunway(RunwayParameters runwayParameters){
    runwaysArrayList.add(new LogicalRunway(runwayParameters));
  }

  public RunwayStripParameters getParameters() {
    RunwayStripParameters rParams = new RunwayStripParameters();
    rParams.name = name;
    rParams.length = length;
    rParams.RESALength = RESALength;
    rParams.RESAWidth = RESAWidth;
    rParams.centreToClearedEnd = centreToClearedEnd;
    rParams.centreToClearedMid = centreToClearedMid;
    rParams.clearedEndMin = clearedEndMin;
    rParams.clearedEndMax = clearedEndMax;
    ArrayList<RunwayParameters> rwParams = new ArrayList<>();
    if (runwaysArrayList.size() == 1) {
      RunwayParameters rwParam1 = runwaysArrayList.get(0).getParameters();
      rwParams.add(rwParam1);
    } else {
      RunwayParameters rwParam1 = runwaysArrayList.get(0).getParameters();
      RunwayParameters rwParam2 = runwaysArrayList.get(1).getParameters();
      rwParams.add(rwParam1);
      rwParams.add(rwParam2);
    }
    rParams.runwayParametersList = rwParams;
    return rParams;
  }
  public boolean ShouldRecalculateRunwayStripParameters(Obstacle obstacle){
    if(obstacle == null){
      return false;
    }
    //If within 75m of the centreline (within centre to cleared end)
    if(Math.abs(obstacle.getDistanceFromCentre()) < centreToClearedEnd){
      //If within 60m of either runway (within strip end)
      if(obstacle.getDistance0f() > -1 * getStripEnd()){
        return true;
      }
      return obstacle.getDistance1f() > -1 * getStripEnd();
    }
    return false;
  }



  /**
   * Given an obstacle and the direction, recalculate the parameters of the active runway.
   * @param startingDesignator designator the aircraft starts from. Either from taking off from
   *                           this point or landing towards this point.
   * @param obstacle obstacle on the active runway.
   */
  public RunwayParameters recalculateRunwayParameters(Designator startingDesignator, Obstacle obstacle) {
    LogicalRunway runway = getLogicalRunway(startingDesignator);
    if(runway == null || !runwaysArrayList.contains(runway)){
      throw new IllegalArgumentException("Runway must be a part of this runway strip.");
    }
    System.out.println("Recalculating parameters of "+getRunwayStripName()+" with starting designator "+startingDesignator.getStringDesignator());

    //Check which designator the obstacle is closer to
    float closeObsDistance;
    float farObsDistance;
    Designator closeDesignator;
    Designator farDesignator;

    if(obstacle.getDistance0().getValue() <= obstacle.getDistance1().getValue()){
      closeObsDistance = obstacle.getDistance0().getValue();
      closeDesignator = obstacle.getDistance0().getKey();

      farObsDistance = obstacle.getDistance1().getValue();
      farDesignator = obstacle.getDistance1().getKey();
    }
    else {
      closeObsDistance = obstacle.getDistance1().getValue();
      closeDesignator = obstacle.getDistance1().getKey();

      farObsDistance = obstacle.getDistance0().getValue();
      farDesignator = obstacle.getDistance0().getKey();
    }

    /*
    TODO: REMOVE THESE LATER, Obstacles currently cannot access their Designators
     */
    if(closeDesignator == null){
      closeDesignator = startingDesignator;
    }
    if(farDesignator == null){
      farDesignator = startingDesignator.getOppositeDesignator();
    }

    //Closer designator = takeoff away + landing over
    if(startingDesignator.equals(closeDesignator)){
      return new RunwayParameters(closeDesignator,
          runway.getTakeoffAwayTORA(closeObsDistance, obstacle.getBlastAllowance()),
//          runway.getTakeoffAwayTODA(closeObsDistance, obstacle.getBlastAllowance(), this.getClearway()),
          runway.getTakeoffAwayTODA(closeObsDistance, obstacle.getBlastAllowance()),
//          runway.getTakeoffAwayASDA(closeObsDistance, obstacle.getBlastAllowance(), this.getStopway()),
          runway.getTakeoffAwayASDA(closeObsDistance, obstacle.getBlastAllowance()),
          runway.getLandingOverLDA(closeObsDistance, obstacle.getHeight(), obstacle.getBlastAllowance(), this.getRESAWidth(), this.getStripEnd(), this.getALS()),
          runway.getDisplacedThreshold()
      );
    }
    //Further designator = takeoff towards + landing towards
    else {
      return new RunwayParameters(farDesignator,
          runway.getTakeoffTowardsTORA(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()),
          runway.getTakeoffTowardsTODA(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()),
          runway.getTakeoffTowardsASDA(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()),
          runway.getLandingTowardsLDA(farObsDistance, this.getRESAWidth(), this.getStripEnd()),
          runway.getDisplacedThreshold()
      );
    }
  }

  public String recalculateRunwayParametersString(Designator startingDesignator, Obstacle obstacle){
    if(obstacle == null) return "";

    LogicalRunway runway = getLogicalRunway(startingDesignator);
    if(runway == null || !runwaysArrayList.contains(runway)){
      throw new IllegalArgumentException("Runway must be a part of this runway strip.");
    }
    System.out.println("Recalculating parameters of "+getRunwayStripName()+" with starting designator "+startingDesignator.getStringDesignator());

    //Check which designator the obstacle is closer to
    float closeObsDistance;
    float farObsDistance;
    Designator closeDesignator;
    Designator farDesignator;

    if(obstacle.getDistance0().getValue() <= obstacle.getDistance1().getValue()){
      closeObsDistance = obstacle.getDistance0().getValue();
      closeDesignator = obstacle.getDistance0().getKey();

      farObsDistance = obstacle.getDistance1().getValue();
      farDesignator = obstacle.getDistance1().getKey();
    }
    else {
      closeObsDistance = obstacle.getDistance1().getValue();
      closeDesignator = obstacle.getDistance1().getKey();

      farObsDistance = obstacle.getDistance0().getValue();
      farDesignator = obstacle.getDistance0().getKey();
    }

    /*
    TODO: REMOVE THESE LATER, Obstacles currently cannot access their Designators
     */
    if(closeDesignator == null){
      closeDesignator = startingDesignator;
    }
    if(farDesignator == null){
      farDesignator = startingDesignator.getOppositeDesignator();
    }

    StringBuilder stringBuilder = new StringBuilder();
    //Closer designator = takeoff away + landing over
    if(startingDesignator.equals(closeDesignator)){
      stringBuilder.append(runway.getTakeoffAwayTORACalcSequence(closeObsDistance, obstacle.getBlastAllowance()));
      stringBuilder.append("\n");
//      stringBuilder.append(runway.getTakeoffAwayTODACalcSequence(closeObsDistance, obstacle.getBlastAllowance(), this.getClearway()));
      stringBuilder.append(runway.getTakeoffAwayTODACalcSequence(closeObsDistance, obstacle.getBlastAllowance()));
      stringBuilder.append("\n");
//      stringBuilder.append(runway.getTakeoffAwayASDACalcSequence(closeObsDistance, obstacle.getBlastAllowance(), this.getStopway()));
      stringBuilder.append(runway.getTakeoffAwayASDACalcSequence(closeObsDistance, obstacle.getBlastAllowance()));
      stringBuilder.append("\n");
      stringBuilder.append(runway.getLandingOverLDACalcSequence(closeObsDistance, obstacle.getHeight(), obstacle.getBlastAllowance(), this.getRESAWidth(), this.getStripEnd(), this.getALS()));
    }
    //Further designator = takeoff towards + landing towards
    else {
      stringBuilder.append(runway.getTakeoffTowardsTORACalcSequence(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()));
      stringBuilder.append("\n");
      stringBuilder.append(runway.getTakeoffTowardsTODACalcSequence(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()));
      stringBuilder.append("\n");
      stringBuilder.append(runway.getTakeoffTowardsASDACalcSequence(farObsDistance, obstacle.getHeight(), this.getRESAWidth(), this.getStripEnd(), this.getALS()));
      stringBuilder.append("\n");
      stringBuilder.append(runway.getLandingTowardsLDACalcSequence(farObsDistance, this.getRESAWidth(), this.getStripEnd()));
    }
    return stringBuilder.toString();
  }


  public String getRunwayStripName() {
    return name;
  }
  public Float getLength() {
    return length;
  }
  public float getRESAWidth() {
    return RESAWidth;
  }
  public float getRESALength() {
    return RESALength;
  }
  public int getALS() {
    return ALS;
  }
  public int getTOCS() {
    return TOCS;
  }
  public float getStripEnd() {
    return stripEnd;
  }
  public float getCentreToClearedEnd() {
    return centreToClearedEnd;
  }
  public float getCentreToClearedMid() {
    return centreToClearedMid;
  }
  public float getClearedEndMin() {
    return clearedEndMin;
  }
  public float getClearedEndMax() {
    return clearedEndMax;
  }

  public float getClearway() {
    return clearway;
  }

  public float getStopway() {
    return stopway;
  }
  public float getTOCSSlope() {
      return TOCSslope;
  }
}
