package runwaytool.model;


public class Designator {
  int degree;
  DesignatorSuffix suffix;

  public Designator(int degree, DesignatorSuffix suffix){
    setDegree(degree);
    if (suffix != null) {
      setSuffix(suffix);
    } else {
      this.suffix = null;
    }
  }
  public Designator(String designator) throws Exception{
    switch(designator.length()) {
      case 3:
        setDegree(Integer.parseInt(designator.substring(0, 2)));
        switch(designator.substring(2)) {
          case "L" -> setSuffix(DesignatorSuffix.L);
          case "R" -> setSuffix(DesignatorSuffix.R);
          case "C" -> setSuffix(DesignatorSuffix.C);
          default -> throw new Exception("Invalid designator suffix");
        }
        break;
      case 2:
        setDegree(Integer.parseInt(designator));
        setSuffix(null);
        break;
      default:
        throw new Exception("Invalid designator length - " + designator + " : " + designator.length());
    }
  }

  /**
   * Return a string representation of the designator.
   *
   * Centred designators do not require a suffix.
   * 
   * @return string representation of the designator.
   */
  public String getStringDesignator(){
    String stringDesignator = getDegreeTwoFigure();
    if (suffix != null) {
      switch (suffix){
        case L -> stringDesignator += 'L';
        case R -> stringDesignator += 'R';
        case C -> stringDesignator += 'C';
      }
    }
    return stringDesignator;
  }

  /**
   * Return a String two-figure representation of the degree.
   * e.g. degree 7 would return '07'.
   * @return string representation of the degree.
   */
  public String getDegreeTwoFigure() {
    if(degree < 10){
      return '0'+ Integer.toString(degree);
    }
    else {
      return Integer.toString(degree);
    }
  }

  /**
   * Return the designator opposite this one.
   * The opposite degree is 18 units away from the current degree, hence 36 - degree.
   * The opposite suffix is L for R, and vice versa.
   * C suffixes should have opposite C suffixes. (?)
   *
   * @return opposite designator to this designator.
   */
  public Designator getOppositeDesignator(){
    if (suffix != null) {
      DesignatorSuffix oppositeSuffix;
      if(suffix == DesignatorSuffix.L){
        oppositeSuffix = DesignatorSuffix.R;
      }
      else if (suffix == DesignatorSuffix.R){
        oppositeSuffix = DesignatorSuffix.L;
      }
      else {
        oppositeSuffix = DesignatorSuffix.C;
      }
      return new Designator((getDegree() + 18)%36, oppositeSuffix);
    } else {
      return new Designator((getDegree() + 18)%36, null);
    }
  }

  /**
   * Set the degree of the designator. Degrees must be between 01 and 36.
   * @param degree new degree for the designator
   */
  public void setDegree(int degree) {
    if(degree <= 36 && degree >= 1){
      this.degree = degree;
    }
    else {
      System.out.println("Error: designator degree must be between 01 and 36");
    }
  }

  public int getDegree() {
    return degree;
  }
  public void setSuffix(DesignatorSuffix suffix) {
    this.suffix = suffix;
  }
  public DesignatorSuffix getSuffix() {
    return suffix;
  }
}
