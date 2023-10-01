package runwaytool.model;


/**
 * Data object returned by the XML Parser. Represents the parameters of a
 * logical runway in a single direction.
 *
 * This is defined in an input as one row of the table of runways.
 * (A RunwayStripParameters contains multiple of these objects.)
 */
public class RunwayParameters {
  public Designator designator;
  public float TORA;
  public float TODA;
  public float ASDA;
  public float LDA;
  public float displacedThreshold;

  public RunwayParameters(Designator designator){
    this.designator = designator;
  }

  public RunwayParameters(Designator designator, float TORA, float TODA, float ASDA, float LDA, float displacedThreshold) {
    this.designator = designator;
    this.TORA = TORA;
    this.TODA = TODA;
    this.ASDA = ASDA;
    this.LDA = LDA;
    this.displacedThreshold = displacedThreshold;
  }
}
