package runwaytool.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

class RunwayStripTest {

  Designator designator09L = new Designator(9, DesignatorSuffix.L);
  public static final float OBS_DISTANCE_09L = -50;
  public static final float OBS_HEIGHT = 12;
  public static final float RESA_WIDTH = 240;
  public static final float STRIP_END = 60;
  public static final float ALS = 50;
  Designator designator27R = new Designator(27, DesignatorSuffix.R);
  public static final float OBS_DISTANCE_27R = 3646;
  public static final float OBS_DISTANCE_FROM_CENTRE = 0;

  RunwayStrip constructExampleRunwayStrip() throws Exception {
    ArrayList<RunwayParameters> runwayParametersArrayList = new ArrayList<>();
    RunwayStripParameters stripParameters = new RunwayStripParameters();
    runwayParametersArrayList.add(new RunwayParameters(new Designator("09R"), 3660, 3660, 3660, 3353, 307));
    runwayParametersArrayList.add(new RunwayParameters(new Designator("27L"), 3660, 3660, 3660, 3660, 0));
    runwayParametersArrayList.add(new RunwayParameters(new Designator("09L"), 3902, 3902, 3902, 3595, 306));
    runwayParametersArrayList.add(new RunwayParameters(new Designator("27R"), 3884, 3962, 3884, 3884, 0));
    stripParameters.runwayParametersList = runwayParametersArrayList;
    stripParameters.RESAWidth = RESA_WIDTH;
    stripParameters.stripEnd = STRIP_END;
    stripParameters.name = "Example Runway Strip Heathrow";

    return new RunwayStrip(stripParameters);
  }

  RunwayParameters expectedParameters09LScenario1(){
    return new RunwayParameters(designator09L, 3346, 3346, 3346, 2985, 0);
  }

  RunwayParameters expectedParameters27RScenario1(){
    return new RunwayParameters(designator27R, 2986, 2986, 2986, 3346, 0);
  }

  Obstacle scenario1Obstacle(){
    Obstacle test = new Obstacle("Obstacle1", 12, 0,
        new Pair<>(designator09L, -50f),
        new Pair<>(designator27R, 3646f), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }
  Obstacle scenario2Obstacle(){
    Obstacle test = new Obstacle("Obstacle2", OBS_HEIGHT, OBS_DISTANCE_FROM_CENTRE,
        new Pair<>(designator09L, OBS_DISTANCE_09L),
        new Pair<>(designator27R, OBS_DISTANCE_27R), 0, 0);
    test.setBlastAllowance(300f);
    return test;
  }

  @Test
  void scenario1Runway1TORA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator09L, scenario1Obstacle());
    assertEquals(expectedParameters09LScenario1().TORA, recalculatedParameters.TORA);
  }
  @Test
  void scenario1Runway1TODA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator09L, scenario1Obstacle());
    assertEquals(expectedParameters09LScenario1().TODA, recalculatedParameters.TODA);
  }
  @Test
  void scenario1Runway1ASDA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator09L, scenario1Obstacle());
    assertEquals(expectedParameters09LScenario1().ASDA, recalculatedParameters.ASDA);
  }
  @Test
  void scenario1Runway1LDA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator09L, scenario1Obstacle());
    assertEquals(expectedParameters09LScenario1().LDA, recalculatedParameters.LDA);
  }
  @Test
  void scenario1Runway2TORA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator27R, scenario1Obstacle());
    assertEquals(expectedParameters27RScenario1().TORA, recalculatedParameters.TORA);
  }
  @Test
  void scenario1Runway2TODA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator27R, scenario1Obstacle());
    assertEquals(expectedParameters27RScenario1().TODA, recalculatedParameters.TODA);
  }
  @Test
  void scenario1Runway2ASDA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator27R, scenario1Obstacle());
    assertEquals(expectedParameters27RScenario1().ASDA, recalculatedParameters.ASDA);
  }
  @Test
  void scenario1Runway2LDA() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    RunwayParameters recalculatedParameters = exampleStrip.recalculateRunwayParameters(designator27R, scenario1Obstacle());
    assertEquals(expectedParameters27RScenario1().LDA, recalculatedParameters.LDA);
  }


  @Test
  void scenario1Runway1String() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    String recalculatedParametersString = exampleStrip.recalculateRunwayParametersString(designator09L, scenario1Obstacle());
    System.out.println(recalculatedParametersString);
    assertNotNull(recalculatedParametersString);
  }
  @Test
  void scenario1Runway2String() {
    RunwayStrip exampleStrip = null;
    try {
      exampleStrip = constructExampleRunwayStrip();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert exampleStrip != null;
    String recalculatedParametersString = exampleStrip.recalculateRunwayParametersString(designator27R, scenario1Obstacle());
    System.out.println(recalculatedParametersString);
    assertNotNull(recalculatedParametersString);
  }
}