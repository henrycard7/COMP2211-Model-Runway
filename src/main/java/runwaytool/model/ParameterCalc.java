package runwaytool.model;

/**
 * Class to keep track of a parameter's value, name and list of parameters used to calculate
 * this parameter.
 *
 * The value of the parameter can be returned as normal
 */
public class ParameterCalc {
  String paramName;

  float valuef;
  String inputParamNames = "";
  String inputCalcString = "";
  //String units;
  public ParameterCalc(String paramName, float valuef) {
    this.paramName = paramName;
    this.valuef = valuef;
  }

  public ParameterCalc(String paramName, float valuef, String inputParamNames,
      String inputCalcString) {
    this.paramName = paramName;
    this.valuef = valuef;
    this.inputParamNames = inputParamNames;
    this.inputCalcString = inputCalcString;
  }

  public String getParamName() {
    return paramName;
  }

  public float getValuef() {
    return valuef;
  }
  public float getValuefNonNegative(){
    return Math.max(0, getValuef());
  }

  public String getInputParamNames() {
    return inputParamNames;
  }

  public String getInputCalcString() {
    return inputCalcString;
  }

  /**
   * Return the steps used to calculate this parameter.
   * Newlines used between each step
   * @return string of steps used.
   */
  public String getCalculationSequence(){
    if(getValuef() < 0){
      return getParamName() + " = " + getInputParamNames() + "\n"
          + "= " + getInputCalcString() + "\n"
          + "= " + getValuef() + "(Min 0)";
    }

    return getParamName() + " = " + getInputParamNames() + "\n"
    + "= " + getInputCalcString() + "\n"
    + "= " + getValuef();
  }

  public void addInputParam(String inputParamName, String inputParamCalc){
    if(inputParamNames.isBlank()){
      inputParamNames += inputParamName;
    }
    else {
      inputParamNames += " " + inputParamName;
    }

    if(inputCalcString.isBlank()){
      inputCalcString += inputParamCalc;
    }
    else {
      inputCalcString += " " + inputParamCalc;
    }
  }

  public void addInputParam(String inputParamName, String inputParamCalc, String operator){
    addInputParam(" "+operator+" "+inputParamName, " "+operator+" "+inputParamCalc);
  }

  /*
  public enum ArithOperator{
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE
  }

  float valuef;
  String paramName;

  //Parameter can be calculated from zero or more input parameters
  ParameterCalc param1;
  ArithOperator operator;
  ParameterCalc param2;

  public ParameterCalc(float valuef, String paramName){
    this.valuef = valuef;
    this.paramName = paramName;
  }
  public ParameterCalc(String paramName, ParameterCalc param1, ArithOperator operator, ParameterCalc param2){
    //this.valuef = valuef;
    this.paramName = paramName;

    this.param1 = param1;
    this.operator = operator;
    this.param2 = param2;

    this.valuef = getValue();
  }

  public ParameterCalc(ParameterCalc param1, ArithOperator operator, ParameterCalc param2){
    this.param1 = param1;
    this.operator = operator;
    this.param2 = param2;

    this.valuef = getValue();
  }

  public float getValue(){
    switch(operator){
      case PLUS -> {
        return param1.getValue() + param2.getValue();
      }
      case MINUS -> {
        return param1.getValue() - param2.getValue();
      }
      case MULTIPLY -> {
        return param1.getValue() * param2.getValue();
      }
      case DIVIDE -> {
        return param1.getValue() / param2.getValue();
      }
    }
    return valuef;
  }


  public String toString(){
    switch(operator){
      case PLUS -> {
        return param1.toString() + "+" + param2.toString();
      }
      case MINUS -> {
        return param1.toString() + "-" + param2.toString();
      }
      case MULTIPLY -> {
        return param1.toString() + "*" + param2.toString();
      }
      case DIVIDE -> {
        return param1.toString() + "/" + param2.toString();
      }
    }
    return paramName;
  }

  public String calcToString(){
    switch(operator){
      case PLUS -> {
        return param1.calcToString() + "+" + param2.calcToString();
      }
      case MINUS -> {
        return param1.calcToString() + "-" + param2.calcToString();
      }
      case MULTIPLY -> {
        return param1.calcToString() + "*" + param2.calcToString();
      }
      case DIVIDE -> {
        return param1.calcToString() + "/" + param2.calcToString();
      }
    }
    return String.valueOf(valuef);
  }

   */

}
