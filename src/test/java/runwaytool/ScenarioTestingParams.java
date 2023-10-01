package runwaytool;

import java.util.ArrayList;
import javafx.util.Pair;
import runwaytool.model.Designator;
import runwaytool.model.DesignatorSuffix;
import runwaytool.model.Obstacle;
import runwaytool.model.RunwayParameters;
import runwaytool.model.RunwayStrip;
import runwaytool.model.RunwayStripParameters;

public class ScenarioTestingParams {
  //Runways
  public static final Designator designator09R = new Designator(9, DesignatorSuffix.R);
  public static final int TORA_STANDARD_09R = 3660;
  public static final int TODA_STANDARD_09R = 3660;
  public static final int ASDA_STANDARD_09R = 3660;
  public static final int LDA_STANDARD_09R = 3353;
  public static final int DISPLACED_THRESHOLD_09R = 307;
  public static final Designator designator27L = new Designator(27, DesignatorSuffix.L);
  public static final int TORA_STANDARD_27L = 3660;
  public static final int TODA_STANDARD_27L = 3660;
  public static final int ASDA_STANDARD_27L = 3660;
  public static final int LDA_STANDARD_27L = 3660;
  public static final int DISPLACED_THRESHOLD_27L = 0;
  public static final Designator designator09L = new Designator(9, DesignatorSuffix.L);
  public static final int TORA_STANDARD_09L = 3902;
  public static final int TODA_STANDARD_09L = 3902;
  public static final int ASDA_STANDARD_09L = 3902;
  public static final int LDA_STANDARD_09L = 3595;
  public static final int DISPLACED_THRESHOLD_09L = 306;
  public static final Designator designator27R = new Designator(27, DesignatorSuffix.R);
  public static final int TORA_STANDARD_27R = 3884;
  public static final int TODA_STANDARD_27R = 3962;
  public static final int ASDA_STANDARD_27R = 3884;
  public static final int LDA_STANDARD_27R = 3884;
  public static final int DISPLACED_THRESHOLD_27R = 0;

  public static final int STOPWAY = 0;
  public static final int CLEARWAY = 0;
  public static final int RESA_WIDTH = 240;
  public static final int STRIP_END = 60;
  public static final int ALS = 50;

  //Scenario 1
  public static final int TORA_S1_09L = 3346;
  public static final int ASDA_S1_09L = 3346;
  public static final int TODA_S1_09L = 3346;
  public static final int LDA_S1_09L = 2985;

  public static final int TORA_S1_27R = 2986;
  public static final int ASDA_S1_27R = 2986;
  public static final int TODA_S1_27R = 2986;
  public static final int LDA_S1_27R = 3346;

  public static Obstacle scenario1Obstacle(){
    Obstacle test = new Obstacle("Obstacle1", 12, 0,
        new Pair<>(designator09L, -50f),
        new Pair<>(designator27R, 3646f), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }

  //Scenario 2
  public static final int TORA_S2_09R = 1850;
  public static final int ASDA_S2_09R = 1850;
  public static final int TODA_S2_09R = 1850;
  public static final int LDA_S2_09R = 2553;

  public static final int TORA_S2_27L = 2860;
  public static final int ASDA_S2_27L = 2860;
  public static final int TODA_S2_27L = 2860;
  public static final int LDA_S2_27L = 1850;

  public static Obstacle scenario2Obstacle(){
    Obstacle test = new Obstacle("Obstacle2", 25, -20,
        new Pair<>(designator27L, 500f),
        new Pair<>(designator09R, 2853f), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }

  //Scenario 3
  public static final int TORA_S3_09R = 2903;
  public static final int ASDA_S3_09R = 2903;
  public static final int TODA_S3_09R = 2903;
  public static final int LDA_S3_09R = 2393;

  public static final int TORA_S3_27L = 2393;
  public static final int ASDA_S3_27L = 2393;
  public static final int TODA_S3_27L = 2393;
  public static final int LDA_S3_27L = 2903;

  public static Obstacle scenario3Obstacle(){
    Obstacle test = new Obstacle("Obstacle3", 15, 60,
        new Pair<>(designator09R, 150f),
        new Pair<>(designator27L, 3203f), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }

  //Scenario 4
  public static final int TORA_S4_09L = 2792;
  public static final int ASDA_S4_09L = 2792;
  public static final int TODA_S4_09L = 2792;
  public static final int LDA_S4_09L = 3246;

  public static final int TORA_S4_27R = 3534;
  public static final int ASDA_S4_27R = 3534;
  public static final int TODA_S4_27R = 3612;
  public static final int LDA_S4_27R = 2774;

  public static Obstacle scenario4Obstacle(){
    Obstacle test = new Obstacle("Obstacle4", 20, 20,
        new Pair<>(designator27R, 50f),
        new Pair<>(designator27R, 3546f), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }

  public static RunwayStrip constructExampleRunwayStrip() {
    ArrayList<RunwayParameters> runwayParametersArrayList = new ArrayList<>();
    RunwayStripParameters stripParameters = new RunwayStripParameters();
    runwayParametersArrayList.add(new RunwayParameters(designator09R, TORA_STANDARD_09R, TODA_STANDARD_09R, ASDA_STANDARD_09R, LDA_STANDARD_09R, DISPLACED_THRESHOLD_09R));
    runwayParametersArrayList.add(new RunwayParameters(designator27L, TORA_STANDARD_27L, TODA_STANDARD_27L, ASDA_STANDARD_27L, LDA_STANDARD_27L, DISPLACED_THRESHOLD_27L));
    runwayParametersArrayList.add(new RunwayParameters(designator09L, TORA_STANDARD_09L, TODA_STANDARD_09L, ASDA_STANDARD_09L, LDA_STANDARD_09L, DISPLACED_THRESHOLD_09L));
    runwayParametersArrayList.add(new RunwayParameters(designator27R, TORA_STANDARD_27R, TODA_STANDARD_27R, ASDA_STANDARD_27R, LDA_STANDARD_27R, DISPLACED_THRESHOLD_27R));
    stripParameters.runwayParametersList = runwayParametersArrayList;

    return new RunwayStrip(stripParameters);
  }

  public static RunwayStrip constructZeroRunwayStrip() {
    ArrayList<RunwayParameters> runwayParametersArrayList = new ArrayList<>();
    RunwayStripParameters stripParameters = new RunwayStripParameters();
    runwayParametersArrayList.add(new RunwayParameters(designator09R, 0, 0, 0, 0, 0));
    runwayParametersArrayList.add(new RunwayParameters(designator27L, 0, 0, 0, 0, 0));
    runwayParametersArrayList.add(new RunwayParameters(designator09L, 0, 0, 0, 0, 0));
    runwayParametersArrayList.add(new RunwayParameters(designator27R, 0, 0, 0, 0, 0));
    stripParameters.runwayParametersList = runwayParametersArrayList;

    return new RunwayStrip(stripParameters);
  }




}
