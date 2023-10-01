package runwaytool.model;


import java.util.ArrayList;

/**
 * This class will be returned by the XML Parser such that a RunwayStrip object can be constructed.
 *
 * The RunwayParameters class will be used to construct each LogicalRunway.
 */
public class RunwayStripParameters {
  public String name;
  public float length;
  public ArrayList<RunwayParameters> runwayParametersList;

  public float RESAWidth = 240f;
  public float RESALength;
  public float stripEnd = 60f;
  public float centreToClearedEnd;
  public float centreToClearedMid;
  public float clearedEndMin;
  public float clearedEndMax;

  public RunwayStripParameters(String name, float length,
      ArrayList<RunwayParameters> runwayParametersList, float RESAWidth, float RESALength,
      float centreToClearedEnd, float centreToClearedMid, float clearedEndMin,
      float clearedEndMax) {
    this.name = name;
    this.length = length;
    this.runwayParametersList = runwayParametersList;
    this.RESAWidth = RESAWidth;
    this.RESALength = RESALength;
    this.centreToClearedEnd = centreToClearedEnd;  //75
    this.centreToClearedMid = centreToClearedMid;  //105
    this.clearedEndMin = clearedEndMin;   //150
    this.clearedEndMax = clearedEndMax;   //300
  }
  public RunwayStripParameters() {}
}