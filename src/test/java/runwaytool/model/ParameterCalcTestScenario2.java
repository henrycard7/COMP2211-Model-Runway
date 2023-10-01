package runwaytool.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import runwaytool.ScenarioTestingParams;

class ParameterCalcTestScenario2 {

  @org.junit.jupiter.api.Test
  void TORA_S2_27LTest() {
    System.out.println("### TESTING SCENARIO 2 - TAKE OFF AWAY TORA ###");
    try {
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27L);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTORAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TORA_S2_27L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void ASDA_S2_27LTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - TAKE OFF AWAY ASDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27L);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffAwayASDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.ASDA_S2_27L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void TODA_S2_27LTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - TAKE OFF AWAY TODA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27L);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTODAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TODA_S2_27L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void LDA_S2_27LTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - LANDING OVER TDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27L);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getLandingOverLDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getHeight(), obstacle.getBlastAllowance(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.LDA_S2_27L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @org.junit.jupiter.api.Test
  void TORA_S2_09RTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - TAKE OFF TOWARDS TORA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09R);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsTORAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(), ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TORA_S2_09R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void ASDA_S2_09RTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - TAKE OFF TOWARDS ASDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09R);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsASDAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.ASDA_S2_09R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void TODA_S2_09RTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - TAKE OFF TOWARDS TODA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09R);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsTODAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TODA_S2_09R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void LDA_S2_09RTest() {
    try {
      System.out.println("### TESTING SCENARIO 2 - LANDING TOWARDS LDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09R);
      Obstacle obstacle = ScenarioTestingParams.scenario2Obstacle();
      ParameterCalc param = runway.getLandingTowardsLDAParameterCalc(obstacle.getDistance1f(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.LDA_S2_09R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}