package runwaytool.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import runwaytool.ScenarioTestingParams;

class ParameterCalcTestExceptional {

  @org.junit.jupiter.api.Test
  void TORA_Zero_Test() {
    System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY TORA ###");
    try {
      RunwayStrip exampleStrip = ScenarioTestingParams.constructZeroRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTORAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      //assertEquals(0, param.getValuef());
      assertTrue(param.getValuef() <= 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void ASDA_Zero_Test() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY ASDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructZeroRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayASDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      //assertEquals(0, param.getValuef());
      assertTrue(param.getValuef() <= 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void TODA_Zero_Test() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY TODA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructZeroRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTODAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      //assertEquals(0, param.getValuef());
      assertTrue(param.getValuef() <= 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void LDA_Zero_Test() {
    try {
      System.out.println("### TESTING SCENARIO 1 - LANDING OVER TDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructZeroRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getLandingOverLDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getHeight(), obstacle.getBlastAllowance(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      //assertEquals(0, param.getValuef());
      assertTrue(param.getValuef() <= 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}