package runwaytool.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import runwaytool.ScenarioTestingParams;
import runwaytool.model.Designator;
import runwaytool.model.DesignatorSuffix;
import runwaytool.model.Obstacle;
import runwaytool.model.RunwayParameters;
import runwaytool.model.RunwayStrip;
import runwaytool.model.RunwayStripParameters;

class ControllerTest {
  @Test
  void activeRunwayStrip(){
    Controller controller = new Controller();
    try {
      RunwayStrip strip = ScenarioTestingParams.constructExampleRunwayStrip();
      RunwayStrip[] strips = new RunwayStrip[2];
      strips[0] = strip;

      controller.setActiveRunwayStrip(strip);
      controller.setRunwayStrips(strips);

      assertEquals(strip, controller.getActiveRunwayStrip());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  void activeObstacle(){
    Controller controller = new Controller();
    try {
      assignStrips(controller);

      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      controller.setObstacle(obstacle);
      assertEquals(obstacle, controller.getActiveObstacle());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  void Scenario1_09L_ControllerTest() {
    Controller controller = new Controller();
    try {
      assignStrips(controller);


      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      controller.setObstacle(obstacle);

      RunwayParameters outputParams = controller.recalculateRunwayParameters(ScenarioTestingParams.designator09L);
      //RunwayParameters expectedParams = ScenarioTestingParams.expectedParameters09LScenario1();
      //assertRunwayParametersEquals(expectedParams, outputParams);
      assertEquals(ScenarioTestingParams.TORA_S1_09L, outputParams.TORA);
      assertEquals(ScenarioTestingParams.TODA_S1_09L, outputParams.TODA);
      assertEquals(ScenarioTestingParams.ASDA_S1_09L, outputParams.ASDA);
      assertEquals(ScenarioTestingParams.LDA_S1_09L, outputParams.LDA);

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  void recalculateRunwayParameters27R() {
    Controller controller = new Controller();
    try {
      assignStrips(controller);


      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      controller.setObstacle(obstacle);

      RunwayParameters outputParams = controller.recalculateRunwayParameters(ScenarioTestingParams.designator27R);
//      RunwayParameters expectedParams = expectedParameters27RScenario1();
//      assertRunwayParametersEquals(expectedParams, outputParams);
      assertEquals(ScenarioTestingParams.TORA_S1_27R, outputParams.TORA);
      assertEquals(ScenarioTestingParams.TODA_S1_27R, outputParams.TODA);
      assertEquals(ScenarioTestingParams.ASDA_S1_27R, outputParams.ASDA);
      assertEquals(ScenarioTestingParams.LDA_S1_27R, outputParams.LDA);

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  void recalculateRunwayParametersString09L() {
    Controller controller = new Controller();
    try {
      assignStrips(controller);

      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      controller.setObstacle(obstacle);

      String outputString = controller.recalculateRunwayParametersString(ScenarioTestingParams.designator09L);
      assertNotNull(outputString);
      System.out.println(outputString);

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  void recalculateRunwayParametersString27R() {
    Controller controller = new Controller();
    try {
      assignStrips(controller);

      Obstacle obstacle = ScenarioTestingParams.scenario1Obstacle();
      controller.setObstacle(obstacle);

      String outputString = controller.recalculateRunwayParametersString(ScenarioTestingParams.designator27R);
      assertNotNull(outputString);
      System.out.println(outputString);

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }


//  void assertRunwayParametersEquals(RunwayParameters expectedParams, RunwayParameters outputParams){
//    assertEquals(expectedParams.designator, outputParams.designator);
//    assertEquals(expectedParams.TORA, outputParams.TORA);
//    assertEquals(expectedParams.TODA, outputParams.TODA);
//    assertEquals(expectedParams.ASDA, outputParams.ASDA);
//    assertEquals(expectedParams.LDA, outputParams.LDA);
//    assertEquals(expectedParams.displacedThreshold, outputParams.displacedThreshold);
//  }

  Controller assignStrips(Controller controller){
    RunwayStrip strip;
    try {
      strip = ScenarioTestingParams.constructExampleRunwayStrip();
      RunwayStrip[] strips = new RunwayStrip[2];
      strips[0] = strip;

      controller.setActiveRunwayStrip(strip);
      controller.setRunwayStrips(strips);
      return controller;

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Cannot assign strips to controller");
      return controller;
    }

  }
}