package runwaytool.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import runwaytool.ScenarioTestingParams;

class ParameterCalcTestScenario1 {

  @org.junit.jupiter.api.Test
  void TORA_S1_09L_Test() {
    System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY TORA ###");
    try {
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTORAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TORA_S1_09L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void ASDA_S1_09LTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY ASDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayASDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.ASDA_S1_09L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void TODA_S1_09LTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF AWAY TODA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffAwayTODAParameterCalc(obstacle.getDistance0f(),
          obstacle.getBlastAllowance());

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TODA_S1_09L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void LDA_S1_09LTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - LANDING OVER TDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator09L);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getLandingOverLDAParameterCalc(obstacle.getDistance0f(),
          obstacle.getHeight(), obstacle.getBlastAllowance(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.LDA_S1_09L, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @org.junit.jupiter.api.Test
  void TORA_S1_27RTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF TOWARDS TORA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27R);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsTORAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(), ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TORA_S1_27R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void ASDA_S1_27RTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF TOWARDS ASDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27R);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsASDAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.ASDA_S1_27R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void TODA_S1_27RTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - TAKE OFF TOWARDS TODA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27R);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getTakeoffTowardsTODAParameterCalc(obstacle.getDistance1f(),
          obstacle.getHeight(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END,
          ScenarioTestingParams.ALS);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.TODA_S1_27R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  void LDA_S1_27RTest() {
    try {
      System.out.println("### TESTING SCENARIO 1 - LANDING TOWARDS LDA ###");
      RunwayStrip exampleStrip = ScenarioTestingParams.constructExampleRunwayStrip();
      LogicalRunway runway = exampleStrip.getLogicalRunway(ScenarioTestingParams.designator27R);
      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      ParameterCalc param = runway.getLandingTowardsLDAParameterCalc(obstacle.getDistance1f(),
          ScenarioTestingParams.RESA_WIDTH, ScenarioTestingParams.STRIP_END);

      System.out.println(param.getCalculationSequence());
      assertEquals(ScenarioTestingParams.LDA_S1_27R, param.getValuef());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}