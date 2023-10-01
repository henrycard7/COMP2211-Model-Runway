package runwaytool.model;

import java.util.ArrayList;
import java.util.Collections;

public class LogicalRunway {
  private final Designator designator;
  float TORA = 3902f;
  float TODA = 3902f;
  float ASDA = 3902f;
  float LDA = 3902f;

  float displacedThreshold = 0f;

  public LogicalRunway(Designator designator) {
    this.designator = designator;
  }
  /*
  public LogicalRunway(Designator designator, float TORA, float TODA, float ASDA, float LDA, float displacedThreshold){
    this.TORA = TORA;
    this.TODA = TODA;
    this.LDA = LDA;
    this.displacedThreshold = displacedThreshold;
    this.designator = designator;
  }
  */
  public LogicalRunway(RunwayParameters params){
    this.TORA = params.TORA;
    this.TODA = params.TODA;
    this.ASDA = params.ASDA; //SOMEHOW FORGOT THIS LINE IN THE CONSTRUCTOR
    this.LDA = params.LDA;
    this.displacedThreshold = params.displacedThreshold;
    this.designator = params.designator;
  }

  public RunwayParameters getParameters() {
    return new RunwayParameters(designator, TORA, TODA, ASDA, LDA, displacedThreshold);
  }

  //Landing Over

  /**
   * (Unaffected)
   * Return the recalculated TORA when the aircraft is landing over an obstacle.
   * @return recalculated TORA
   */
  public float getLandingOverTORA(){
    return getTORA();
  }
  public String getLandingOverTORACalcSequence(){
    return new ParameterCalc("New TORA", getTORA(), "TORA", String.valueOf(getTORA())).getCalculationSequence();
  }
  /**
   * (Unaffected)
   * Return the recalculated TODA when the aircraft is landing over an obstacle.
   * @return recalculated TODA
   */
  public float getLandingOverTODA(){
    return getTODA();
  }
  public String getLandingOverTODACalcSequence(){
    return new ParameterCalc("New TODA", getTODA(), "TODA", String.valueOf(getTODA())).getCalculationSequence();
  }
  /**
   * (Unaffected)
   * Return the recalculated ASDA when the aircraft is landing over an obstacle.
   * @return recalculated ASDA
   */
  public float getLandingOverASDA(){
    return getASDA();
  }
  public String getLandingOverASDACalcSequence(){
    return new ParameterCalc("New ASDA", getASDA(), "ASDA", String.valueOf(getASDA())).getCalculationSequence();
  }

  /**
   * Return the recalculated LDA when the aircraft is landing over an obstacle.
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param obsHeight height in metres of the obstacle
   * @param blastProtection width in metres of an area preventing engine blast from affecting obstacles
   * @param RESAWidth width in metres of the RESA
   * @param stripEnd width in metres of area between the end of runway and end of runway strip
   * @param ALS 1:n slope for aircraft minimum angle of descent
   * @return recalculated LDA
   */
  public float getLandingOverLDA(float obsDistance, float obsHeight, float blastProtection, float RESAWidth, float stripEnd, int ALS){
    return getLandingOverLDAParameterCalc(obsDistance, obsHeight, blastProtection, RESAWidth, stripEnd, ALS).getValuefNonNegative();
  }
  public String getLandingOverLDACalcSequence(float obsDistance, float obsHeight, float blastProtection, float RESAWidth, float stripEnd, int ALS){
    return getLandingOverLDAParameterCalc(obsDistance, obsHeight, blastProtection, RESAWidth, stripEnd, ALS).getCalculationSequence();
  }

  ParameterCalc getLandingOverLDAParameterCalc(float obsDistance, float obsHeight, float blastProtection, float RESAWidth, float stripEnd, int ALS){
    float tempThreshold = obsHeight * ALS;
    ArrayList<Float> paramsArrayList = new ArrayList<>();
    paramsArrayList.add(blastProtection);
    paramsArrayList.add(RESAWidth);
    paramsArrayList.add(tempThreshold);

    //Max value = most contraining
    float mostConstrainingValue = Collections.max(paramsArrayList);
    float result = getLDA() - obsDistance - stripEnd - mostConstrainingValue;
    //Construct ParameterCalc
    ParameterCalc calc = new ParameterCalc("New LDA", result);
    calc.addInputParam("Original LDA", String.valueOf(getLDA())); //LDA
    calc.addInputParam("Distance from Threshold", String.valueOf(obsDistance), "-");// - Distance from Threshold
    calc.addInputParam("Strip End", String.valueOf(stripEnd), "-");// - Strip End
    //Minus most constraining value
    if (mostConstrainingValue == blastProtection) {
      calc.addInputParam("Blast Protection",String.valueOf(blastProtection), "-"); // - Blast Protection
    } else if (mostConstrainingValue == RESAWidth) {
      calc.addInputParam("RESA Width",String.valueOf(RESAWidth), "-"); // - RESAWidth
    } else if (mostConstrainingValue == tempThreshold) {
      calc.addInputParam("Slope Calculation",obsHeight+"*"+ ALS, "-"); // - Slope Calculation
    }

    return calc;
  }

  //Landing Towards
  /**
   * (Unaffected)
   * Return the recalculated TORA when the aircraft is landing towards an obstacle.
   * @return recalculated TORA
   */
  public float getLandingTowardsTORA(){
    return getTORA();
  }
  public String getLandingTowardsTORACalcSequence(){
    return new ParameterCalc("New TORA", getTORA(), "TORA", String.valueOf(getTORA())).getCalculationSequence();
  }
  /**
   * (Unaffected)
   * Return the recalculated TODA when the aircraft is landing towards an obstacle.
   * @return recalculated TODA
   */
  public float getLandingTowardsTODA(){
    return getTODA();
  }
  public String getLandingTowardsTODACalcSequence(){
    return new ParameterCalc("New TODA", getTODA(), "TODA", String.valueOf(getTODA())).getCalculationSequence();
  }
  /**
   * (Unaffected)
   * Return the recalculated ASDA when the aircraft is landing towards an obstacle.
   * @return recalculated ASDA
   */
  public float getLandingTowardsASDA(){
    return getASDA();
  }
  public String getLandingTowardsASDACalcSequence(){
    return new ParameterCalc("New ASDA", getASDA(), "ASDA", String.valueOf(getASDA())).getCalculationSequence();
  }

  /**
   * Return the recalculated LDA when the aircraft is landing towards an obstacle.
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param RESAWidth width in metres of the RESA
   * @param stripEnd width in metres of area between the end of runway and end of runway strip
   * @return recalculated LDA
   */
  public float getLandingTowardsLDA(float obsDistance, float RESAWidth, float stripEnd){
    return getLandingTowardsLDAParameterCalc(obsDistance, RESAWidth, stripEnd).getValuefNonNegative();
  }
  public String getLandingTowardsLDACalcSequence(float obsDistance, float RESAWidth, float stripEnd){
    return getLandingTowardsLDAParameterCalc(obsDistance, RESAWidth, stripEnd).getCalculationSequence();
  }
  ParameterCalc getLandingTowardsLDAParameterCalc(float obsDistance, float RESAWidth, float stripEnd){
    float result = (obsDistance - RESAWidth - stripEnd);
    ParameterCalc calc = new ParameterCalc("New LDA", result);
    calc.addInputParam("Distance from Threshold", String.valueOf(obsDistance));
    calc.addInputParam("RESA Width", String.valueOf(RESAWidth), "-");
    calc.addInputParam("Strip End", String.valueOf(stripEnd), "-");

    return calc;
  }

  //Takeoff Towards
  /**
   * Return the recalculated TORA when the aircraft is landing towards an obstacle
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param obsHeight height in metres of the obstacle
   * @param RESAWidth width in metres of the RESA
   * @param stripEnd width in metres of area between the end of runway and end of runway strip
   * @param ALS 1:n slope for aircraft minimum angle of descent
   * @return recalculated TORA
   */
  public float getTakeoffTowardsTORA(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    return getTakeoffTowardsTORAParameterCalc(obsDistance, obsHeight, RESAWidth, stripEnd, ALS).getValuefNonNegative();
  }
  public String getTakeoffTowardsTORACalcSequence(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    return getTakeoffTowardsTORAParameterCalc(obsDistance, obsHeight, RESAWidth, stripEnd, ALS).getCalculationSequence();
  }
  ParameterCalc getTakeoffTowardsTORAParameterCalc(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    float tempThreshold = obsHeight * ALS;
    ArrayList<Float> paramsArrayList = new ArrayList<>();
    paramsArrayList.add(RESAWidth);
    paramsArrayList.add(tempThreshold);

    //Max value = most contraining
    float mostConstrainingValue = Collections.max(paramsArrayList);
    float result = obsDistance + getDisplacedThreshold() - mostConstrainingValue - stripEnd;
    //Construct ParameterCalc
    ParameterCalc calc = new ParameterCalc("New TORA", result);
    calc.addInputParam("Distance from Threshold", String.valueOf(obsDistance));// - Distance from Threshold
    calc.addInputParam("Displaced Threshold", String.valueOf(getDisplacedThreshold()), "+");// + Displaced Threshold
    calc.addInputParam("Strip End", String.valueOf(stripEnd), "-");// - Strip End
    //Minus most constraining value
    if (mostConstrainingValue == RESAWidth) {
      calc.addInputParam("RESA Width",String.valueOf(RESAWidth), "-"); // - RESAWidth
    } else if (mostConstrainingValue == tempThreshold) {
      calc.addInputParam("Slope Calculation",obsHeight+"*"+ ALS, "-"); // - Slope Calculation
    }

    return calc;
  }

  /**
   * Return the recalculated TODA when the aircraft is taking off towards an obstacle.
   * Equal to recalculated TORA.
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param obsHeight height in metres of the obstacle
   * @param RESAWidth width in metres of the RESA
   * @param stripEnd width in metres of area between the end of runway and end of runway strip
   * @param ALS 1:n slope for aircraft minimum angle of descent
   * @return recalculated TODA
   */
  public float getTakeoffTowardsTODA(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS) {
    return getTakeoffTowardsTORA(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
  }
  public float getTakeoffTowardsTODA(float recalculatedTORA) {
    return recalculatedTORA;
  }
  public String getTakeoffTowardsTODACalcSequence(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    float result = getTakeoffTowardsTORA(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
    return new ParameterCalc("New TODA", result, "New TORA",
        String.valueOf(result)).getCalculationSequence();
  }
  public String getTakeoffTowardsTODACalcSequence(float recalculatedTORA){
    return new ParameterCalc("New TODA", recalculatedTORA, "New TORA",
        String.valueOf(recalculatedTORA)).getCalculationSequence();
  }
  ParameterCalc getTakeoffTowardsTODAParameterCalc(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    return getTakeoffTowardsTORAParameterCalc(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
  }
  /**
   * Return the recalculated ASDA when the aircraft is taking off towards an obstacle.
   * Equal to recalculated TORA.
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param obsHeight height in metres of the obstacle
   * @param RESAWidth width in metres of the RESA
   * @param stripEnd width in metres of area between the end of runway and end of runway strip
   * @param ALS 1:n slope for aircraft minimum angle of descent
   * @return recalculated ASDA
   */
  public float getTakeoffTowardsASDA(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS) {
    return getTakeoffTowardsTORA(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
  }
  public float getTakeoffTowardsASDA(float recalculatedTORA) {
    return recalculatedTORA;
  }
  public String getTakeoffTowardsASDACalcSequence(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    float result = getTakeoffTowardsTORA(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
    return new ParameterCalc("New ASDA", result, "New TORA",
        String.valueOf(result)).getCalculationSequence();
  }
  public String getTakeoffTowardsASDACalcSequence(float recalculatedTORA){
    return new ParameterCalc("New ASDA", recalculatedTORA, "New TORA",
        String.valueOf(recalculatedTORA)).getCalculationSequence();
  }
  ParameterCalc getTakeoffTowardsASDAParameterCalc(float obsDistance, float obsHeight, float RESAWidth, float stripEnd, int ALS){
    return getTakeoffTowardsTORAParameterCalc(obsDistance, obsHeight, RESAWidth, stripEnd, ALS);
  }
  /**
   * (Unaffected)
   * Return the recalculated LDA when the aircraft is taking off towards an obstacle.
   * @return recalculated LDA
   */
  public float getTakeoffTowardsLDA(){
    return getLDA();
  }
  public String getTakeoffTowardsLDACalcSequence(){
    return new ParameterCalc("New LDA", getLDA(), "LDA", String.valueOf(getLDA())).getCalculationSequence();
  }


//  public float getTakeoffTowardsSlopeAngle(float obsHeight, float RESAWidth, float stripEnd, int ALS){
//    float tempThreshold = obsHeight * ALS;
//    ArrayList<Float> paramsArrayList = new ArrayList<>();
//    paramsArrayList.add(RESAWidth);
//    paramsArrayList.add(tempThreshold);
//
//    //Max value = most contraining
//    float mostConstrainingValue = Collections.max(paramsArrayList);
//
//    float x = mostConstrainingValue + stripEnd;
//    double angleDouble = Math.atan2(obsHeight, x);
//    return (float) angleDouble;
//  }


  //Takeoff Away

  /**
   * Return the recalculated TORA when the aircraft is taking off away from an obstacle.
   * @param obsDistance distance in metres of obstacle from the threshold
   * @param blastProtection width in metres of an area preventing engine blast from affecting obstacles
   * @return recalculated TORA
   */
  public float getTakeoffAwayTORA(float obsDistance, float blastProtection){
    return getTakeoffAwayTORAParameterCalc(obsDistance, blastProtection).getValuefNonNegative();
  }
  public String getTakeoffAwayTORACalcSequence(float obsDistance, float blastProtection){
    return getTakeoffAwayTORAParameterCalc(obsDistance, blastProtection).getCalculationSequence();
  }
  ParameterCalc getTakeoffAwayTORAParameterCalc(float obsDistance, float blastProtection){
    boolean useDisplacedThreshold = getDisplacedThreshold() >= obsDistance;
    float result = getTORA() - obsDistance - blastProtection;
    if(useDisplacedThreshold){
      result -= getDisplacedThreshold();
    }

    ParameterCalc calc = new ParameterCalc("New TORA", result);
    calc.addInputParam("Original TORA", String.valueOf(getTORA()));
    calc.addInputParam("Distance From Threshold", String.valueOf(obsDistance), "-");
    calc.addInputParam("Blast Protection", String.valueOf(blastProtection), "-");

    if(useDisplacedThreshold){
      calc.addInputParam("Displaced Threshold", String.valueOf(getDisplacedThreshold()), "-");
    }

    return calc;
  }

  public float getTakeoffAwayTODA(float obsDistance, float blastProtection){
    float clearway = getTORA() - getTODA();
    return getTakeoffAwayTODAParameterCalc(obsDistance, blastProtection).getValuefNonNegative();
  }
  public String getTakeoffAwayTODACalcSequence(float obsDistance, float blastProtection){
    return getTakeoffAwayTODAParameterCalc(obsDistance, blastProtection).getCalculationSequence();
  }
  ParameterCalc getTakeoffAwayTODAParameterCalc(float obsDistance, float blastProtection){
    float clearway = Math.abs(getTORA() - getTODA());
    float result = getTakeoffAwayTORA(obsDistance, blastProtection);
    if(clearway > 0){
      result += clearway;
    }

    ParameterCalc calc = new ParameterCalc("New TODA", result);
    calc.addInputParam("Recalculated TORA", String.valueOf(getTakeoffAwayTORA(obsDistance, blastProtection)));
    //calc.addInputParam("Distance From Threshold", String.valueOf(getTORA()), "-");
    //calc.addInputParam("Blast Protection", String.valueOf(blastProtection), "-");

//    calc.addInputParam("Clearway", String.valueOf(clearway), "+");
    if(getTORA() > getTODA()){
      calc.addInputParam("Original TORA", String.valueOf(getTORA()), "+");
      calc.addInputParam("Original TODA", String.valueOf(getTODA()), "-");
    } else if(getTORA() < getTODA()){
      calc.addInputParam("Original TODA", String.valueOf(getTODA()), "+");
      calc.addInputParam("Original TORA", String.valueOf(getTORA()), "-");
    }

    return calc;
  }
  public float getTakeoffAwayASDA(float obsDistance, float blastProtection){
    return getTakeoffAwayASDAParameterCalc(obsDistance, blastProtection).getValuefNonNegative();
  }
  public String getTakeoffAwayASDACalcSequence(float obsDistance, float blastProtection){
    return getTakeoffAwayASDAParameterCalc(obsDistance, blastProtection).getCalculationSequence();
  }
  ParameterCalc getTakeoffAwayASDAParameterCalc(float obsDistance, float blastProtection){
    //If obstacle obstructs stopway, ASDA is equal to TORA
    float stopway = Math.abs(getTORA() - getASDA());
    float result = getTakeoffAwayTORA(obsDistance, blastProtection);
    if(stopway > 0){
      result += stopway;
    }
    ParameterCalc calc = new ParameterCalc("New ASDA", result);
    calc.addInputParam("Recalculated TORA", String.valueOf(getTakeoffAwayTORA(obsDistance, blastProtection)));
    //calc.addInputParam("Distance From Threshold", String.valueOf(getTORA()), "-");
    //calc.addInputParam("Blast Protection", String.valueOf(blastProtection), "-");
//    calc.addInputParam("Stopway", String.valueOf(stopway), "+");

    if(getTORA() > getASDA()){
      calc.addInputParam("Original TORA", String.valueOf(getTORA()), "+");
      calc.addInputParam("Original ASDA", String.valueOf(getASDA()), "-");
    } else if(getTORA() < getASDA()){
      calc.addInputParam("Original ASDA", String.valueOf(getASDA()), "+");
      calc.addInputParam("Original TORA", String.valueOf(getTORA()), "-");
    }
    return calc;
  }
  /**
   * (Unaffected)
   * Return the recalculated LDA when the aircraft is taking off away from an obstacle.
   * @return recalculated LDA
   */
  public float getTakeoffAwayLDA(){
    return getLDA();
  }
  public String getTakeoffAwayLDACalcSequence(){
    return new ParameterCalc("New LDA", getLDA(), "LDA", String.valueOf(getLDA())).getCalculationSequence();
  }



  public Designator getDesignator() {
    return designator;
  }
  public float getTORA() {
    return TORA;
  }
  public float getTODA() {
    return TODA;
  }
  public float getASDA() {
    return ASDA;
  }
  public float getLDA() {
    return LDA;
  }
  public float getDisplacedThreshold() {
    return displacedThreshold;
  }
}