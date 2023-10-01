package runwaytool.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import runwaytool.model.*;

public class XMLParser {
    private RunwayStripParameters[] rSParamArray;
    public AirportParameters parseAirportXML(File file) throws Exception {
        FileInputStream fileIS = new FileInputStream(file.getAbsoluteFile());
        return parseAirportInputStream(fileIS);
    }
    public AirportParameters parseAirportXML(InputStream inputStream) throws Exception {
        return parseAirportInputStream(inputStream);
    }
    private AirportParameters parseAirportInputStream(InputStream inputStream)throws Exception {
        AirportParameters aParams = new AirportParameters();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource source = new InputSource(inputStream);
        Document document = builder.parse(source);
        Element rootElement = document.getDocumentElement();
        aParams.name = rootElement.getChildNodes().item(1).getChildNodes().item(1).getTextContent();
        NodeList allNodes = rootElement.getChildNodes();
        int i = 0;
        do {
            i +=1;
        } while (allNodes.item(i).getNodeName() != "runwayStrips");
        if (i < allNodes.getLength()) {
            Element runwayStrips = (Element)allNodes.item(i);
            int runwayNo = Integer.parseInt(runwayStrips.getAttribute("amount"));
            NodeList rSNodes = runwayStrips.getChildNodes();
            if (runwayNo == 1) {
                rSParamArray = new RunwayStripParameters[1];
                rSParamArray[0] = parseStripNodes(rSNodes.item(1));
            } else {
                rSParamArray = new RunwayStripParameters[2];
                rSParamArray[0] = parseStripNodes(rSNodes.item(1));
                rSParamArray[1] = parseStripNodes(rSNodes.item(3));
            }
            aParams.runwayStripParameters = rSParamArray;
            return aParams;
        } else {
            throw new Exception("Cannot find 'runwayStrips' node");
        }
    }
    private RunwayStripParameters parseStripNodes(Node n) throws Exception {
        RunwayStripParameters rParams = new RunwayStripParameters();
        //RunwayStripParameters
        rParams.name = n.getChildNodes().item(1).getTextContent();
        rParams.length = Float.parseFloat(n.getChildNodes().item(3).getTextContent());
        rParams.RESALength = Float.parseFloat(n.getChildNodes().item(5).getTextContent());
        rParams.centreToClearedMid = Float.parseFloat(n.getChildNodes().item(7).getChildNodes().item(1).getTextContent());
        rParams.centreToClearedEnd = Float.parseFloat(n.getChildNodes().item(7).getChildNodes().item(3).getTextContent());
        rParams.clearedEndMin = Float.parseFloat(n.getChildNodes().item(7).getChildNodes().item(5).getTextContent());
        rParams.clearedEndMax = Float.parseFloat(n.getChildNodes().item(7).getChildNodes().item(7).getTextContent());
        //RunwayParameters
        ArrayList<RunwayParameters> rParamsArray = new ArrayList<RunwayParameters>();
        Designator designator1 = new Designator(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
        float TORA1 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(3).getTextContent());
        float TODA1 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(5).getTextContent());
        float ASDA1 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(7).getTextContent());
        float LDA1 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(9).getTextContent());
        float threshold1 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(1).getChildNodes().item(11).getTextContent());
        RunwayParameters rParams1 = new RunwayParameters(designator1, TORA1, TODA1, ASDA1, LDA1, threshold1);
        Designator designator2 = new Designator(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(1).getTextContent());
        float TORA2 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(3).getTextContent());
        float TODA2 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(5).getTextContent());
        float ASDA2 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(7).getTextContent());
        float LDA2 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(9).getTextContent());
        float threshold2 = Float.parseFloat(n.getChildNodes().item(9).getChildNodes().item(3).getChildNodes().item(11).getTextContent());
        RunwayParameters rParams2 = new RunwayParameters(designator2, TORA2, TODA2, ASDA2, LDA2, threshold2);
        rParamsArray.add(rParams1);
        rParamsArray.add(rParams2);
        rParams.runwayParametersList = rParamsArray;
        return rParams;
    }
    public void createAirportXML(File file, RunwayStrip[] strips, String airportName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootElement = document.createElement("airport");
        document.appendChild(rootElement);

        Element identifiers = document.createElement("identifiers");
        rootElement.appendChild(identifiers);
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(airportName));
        identifiers.appendChild(name);

        Element runwayStrips = document.createElement("runwayStrips");
        Attr a1 = document.createAttribute("amount");
        a1.setValue(String.valueOf(strips.length));
        runwayStrips.setAttributeNode(a1);
        rootElement.appendChild(runwayStrips);

        Element runwayStrip1 = document.createElement("runwayStrip");
        runwayStrips.appendChild(runwayStrip1);

        Element direction1 = document.createElement("direction");
        direction1.appendChild(document.createTextNode(strips[0].getRunwayStripName()));
        runwayStrip1.appendChild(direction1);
        Element length1 = document.createElement("length");
        length1.appendChild(document.createTextNode(String.valueOf(strips[0].getLength())));
        runwayStrip1.appendChild(length1);
        Element resa1 = document.createElement("resa");
        resa1.appendChild(document.createTextNode(String.valueOf(strips[0].getRESALength())));
        runwayStrip1.appendChild(resa1);

        Element clearedGradedArea1 = document.createElement("clearedGradedArea");
        runwayStrip1.appendChild(clearedGradedArea1);
        Element centerlineToClearedMax1 = document.createElement("centerlineToClearedMax");
        centerlineToClearedMax1.appendChild(document.createTextNode(String.valueOf(strips[0].getCentreToClearedMid())));
        clearedGradedArea1.appendChild(centerlineToClearedMax1);
        Element centerlineToClearedMin1 = document.createElement("centerlineToClearedMin");
        centerlineToClearedMin1.appendChild(document.createTextNode(String.valueOf(strips[0].getCentreToClearedEnd())));
        clearedGradedArea1.appendChild(centerlineToClearedMin1);
        Element clearedEndMin1 = document.createElement("clearedEndMin");
        clearedEndMin1.appendChild(document.createTextNode(String.valueOf(strips[0].getClearedEndMin())));
        clearedGradedArea1.appendChild(clearedEndMin1);
        Element clearedEndMax1 = document.createElement("clearedEndMax");
        clearedEndMax1.appendChild(document.createTextNode(String.valueOf(strips[0].getClearedEndMax())));
        clearedGradedArea1.appendChild(clearedEndMax1);

        Element runways1 = document.createElement("runways");
        runwayStrip1.appendChild(runways1);

        Element runway1 = document.createElement("runway");
        runways1.appendChild(runway1);
        Element designator1 = document.createElement("designator");
        designator1.appendChild(document.createTextNode(strips[0].getLogicalRunways().get(0).getDesignator().getStringDesignator()));
        runway1.appendChild(designator1);
        Element tora1 = document.createElement("tora");
        tora1.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(0).getTORA())));
        runway1.appendChild(tora1);
        Element toda1 = document.createElement("toda");
        toda1.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(0).getTODA())));
        runway1.appendChild(toda1);
        Element asda1 = document.createElement("asda");
        asda1.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(0).getASDA())));
        runway1.appendChild(asda1);
        Element lda1 = document.createElement("lda");
        lda1.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(0).getLDA())));
        runway1.appendChild(lda1);
        Element threshold1 = document.createElement("threshold");
        threshold1.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(0).getDisplacedThreshold())));
        runway1.appendChild(threshold1);

        Element runway2 = document.createElement("runway");
        runways1.appendChild(runway2);
        Element designator2 = document.createElement("designator");
        designator2.appendChild(document.createTextNode(strips[0].getLogicalRunways().get(1).getDesignator().getStringDesignator()));
        runway2.appendChild(designator2);
        Element tora2 = document.createElement("tora");
        tora2.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(1).getTORA())));
        runway2.appendChild(tora2);
        Element toda2 = document.createElement("toda");
        toda2.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(1).getTODA())));
        runway2.appendChild(toda2);
        Element asda2 = document.createElement("asda");
        asda2.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(1).getASDA())));
        runway2.appendChild(asda2);
        Element lda2 = document.createElement("lda");
        lda2.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(1).getLDA())));
        runway2.appendChild(lda2);
        Element threshold2 = document.createElement("threshold");
        threshold2.appendChild(document.createTextNode(String.valueOf(strips[0].getLogicalRunways().get(1).getDisplacedThreshold())));
        runway2.appendChild(threshold2);

        if (strips.length == 2 && strips[1] != null) {
            Element runwayStrip2 = document.createElement("runwayStrip");
            runwayStrips.appendChild(runwayStrip2);

            Element direction2 = document.createElement("direction");
            direction2.appendChild(document.createTextNode(strips[1].getRunwayStripName()));
            runwayStrip2.appendChild(direction2);
            Element length2 = document.createElement("length");
            length2.appendChild(document.createTextNode(String.valueOf(strips[1].getLength())));
            runwayStrip2.appendChild(length2);
            Element resa2 = document.createElement("resa");
            resa2.appendChild(document.createTextNode(String.valueOf(strips[1].getRESALength())));
            runwayStrip2.appendChild(resa2);

            Element clearedGradedArea2 = document.createElement("clearedGradedArea");
            runwayStrip2.appendChild(clearedGradedArea2);
            Element centerlineToClearedMax2 = document.createElement("centerlineToClearedMax");
            centerlineToClearedMax2.appendChild(document.createTextNode(String.valueOf(strips[1].getCentreToClearedMid())));
            clearedGradedArea2.appendChild(centerlineToClearedMax2);
            Element centerlineToClearedMin2 = document.createElement("centerlineToClearedMin");
            centerlineToClearedMin2.appendChild(document.createTextNode(String.valueOf(strips[1].getCentreToClearedEnd())));
            clearedGradedArea2.appendChild(centerlineToClearedMin2);
            Element clearedEndMin2 = document.createElement("clearedEndMin");
            clearedEndMin2.appendChild(document.createTextNode(String.valueOf(strips[1].getClearedEndMin())));
            clearedGradedArea2.appendChild(clearedEndMin2);
            Element clearedEndMax2 = document.createElement("clearedEndMax");
            clearedEndMax2.appendChild(document.createTextNode(String.valueOf(strips[1].getClearedEndMax())));
            clearedGradedArea2.appendChild(clearedEndMax2);

            Element runways2 = document.createElement("runways");
            runwayStrip2.appendChild(runways2);

            Element runway3 = document.createElement("runway");
            runways2.appendChild(runway3);
            Element designator3 = document.createElement("designator");
            designator3.appendChild(document.createTextNode(strips[1].getLogicalRunways().get(0).getDesignator().getStringDesignator()));
            runway3.appendChild(designator3);
            Element tora3 = document.createElement("tora");
            tora3.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(0).getTORA())));
            runway3.appendChild(tora3);
            Element toda3 = document.createElement("toda");
            toda3.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(0).getTODA())));
            runway3.appendChild(toda3);
            Element asda3 = document.createElement("asda");
            asda3.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(0).getASDA())));
            runway3.appendChild(asda3);
            Element lda3 = document.createElement("lda");
            lda3.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(0).getLDA())));
            runway3.appendChild(lda3);
            Element threshold3 = document.createElement("threshold");
            threshold3.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(0).getDisplacedThreshold())));
            runway3.appendChild(threshold3);

            Element runway4 = document.createElement("runway");
            runways2.appendChild(runway4);
            Element designator4 = document.createElement("designator");
            designator4.appendChild(document.createTextNode(strips[1].getLogicalRunways().get(1).getDesignator().getStringDesignator()));
            runway4.appendChild(designator4);
            Element tora4 = document.createElement("tora");
            tora4.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(1).getTORA())));
            runway4.appendChild(tora4);
            Element toda4 = document.createElement("toda");
            toda4.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(1).getTODA())));
            runway4.appendChild(toda4);
            Element asda4 = document.createElement("asda");
            asda4.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(1).getASDA())));
            runway4.appendChild(asda4);
            Element lda4 = document.createElement("lda");
            lda4.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(1).getLDA())));
            runway4.appendChild(lda4);
            Element threshold4 = document.createElement("threshold");
            threshold4.appendChild(document.createTextNode(String.valueOf(strips[1].getLogicalRunways().get(1).getDisplacedThreshold())));
            runway4.appendChild(threshold4);
        }

        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        DOMSource dSource = new DOMSource(document);
        StreamResult sResult = new StreamResult(file);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(dSource, sResult);
    }
    public void createObstacleXML(File file, Obstacle obstacle) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootElement = document.createElement("obstacle");
        document.appendChild(rootElement);

        Element type = document.createElement("type");
        if (obstacle.getClass() == GroundedAircraft.class) {
            type.appendChild(document.createTextNode("airplane"));
        } else if (obstacle.getClass() == FOD.class) {
            type.appendChild(document.createTextNode("fod"));
        } else {
            throw new Exception("Obstacle is invalid class");
        }
        rootElement.appendChild(type);
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(obstacle.getName()));
        rootElement.appendChild(name);
        Element height = document.createElement("height");
        height.appendChild(document.createTextNode(String.valueOf(obstacle.getHeight())));
        rootElement.appendChild(height);
        Element blastAllowance = document.createElement("blastAllowance");
        blastAllowance.appendChild(document.createTextNode(String.valueOf(obstacle.getBlastAllowance())));
        rootElement.appendChild(blastAllowance);
        Element position = document.createElement("position");
        rootElement.appendChild(position);
        Element distanceFromCentre = document.createElement("distanceFromCentre");
        distanceFromCentre.appendChild(document.createTextNode(String.valueOf(obstacle.getDistanceFromCentre())));
        position.appendChild(distanceFromCentre);
        Element distance0 = document.createElement("distance0");
        distance0.appendChild(document.createTextNode(String.valueOf(obstacle.getDistance0().getValue())));
        position.appendChild(distance0);
        Element distance1 = document.createElement("distance1");
        distance1.appendChild(document.createTextNode(String.valueOf(obstacle.getDistance1().getValue())));
        position.appendChild(distance1);
        //
        Element dimensions = document.createElement("dimensions");
        rootElement.appendChild(dimensions);
        Element width = document.createElement("width");
        width.appendChild(document.createTextNode(String.valueOf(obstacle.getWidth())));
        dimensions.appendChild(width);
        Element length = document.createElement("length");
        length.appendChild(document.createTextNode(String.valueOf(obstacle.getLength())));
        dimensions.appendChild(length);

        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        DOMSource dSource = new DOMSource(document);
        StreamResult sResult = new StreamResult(file);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(dSource, sResult);
    }
    public Obstacle parseObstacleXML(File file) throws Exception {
        return parseObstacleInputStream(new FileInputStream(file.getAbsoluteFile()));
    }
    public Obstacle parseObstacleXML(InputStream inputStream) throws Exception {
        return parseObstacleInputStream(inputStream);
    }
    private Obstacle parseObstacleInputStream(InputStream inputStream) throws Exception {
        Obstacle obstacle;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource source = new InputSource(inputStream);
        Document document = builder.parse(source);
        Element rootElement = document.getDocumentElement();
        NodeList allNodes = rootElement.getChildNodes();
        String type = allNodes.item(1).getTextContent();
        String name = allNodes.item(3).getTextContent();
        float height = Float.parseFloat(allNodes.item(5).getTextContent());
        float blastAllowance = Float.parseFloat(allNodes.item(7).getTextContent());
        float distanceFromCentre = Float.parseFloat(allNodes.item(9).getChildNodes().item(1).getTextContent());
        float distance0 = Float.parseFloat(allNodes.item(9).getChildNodes().item(3).getTextContent());
        float distance1 = Float.parseFloat(allNodes.item(9).getChildNodes().item(5).getTextContent());
        float width = Float.parseFloat(allNodes.item(11).getChildNodes().item(1).getTextContent());
        float length = Float.parseFloat(allNodes.item(11).getChildNodes().item(3).getTextContent());
        switch (type) {
            case "airplane" ->
                obstacle = new GroundedAircraft(name, height, distanceFromCentre,
                    new Pair<>(null, distance0), new Pair<>(null, distance1),
                    blastAllowance, width, length);
            case "fod" ->
                obstacle = new FOD(name, height, distanceFromCentre,
                    new Pair<>(null, distance0), new Pair<>(null, distance1),
                    blastAllowance, width, length);
            default -> throw new Exception("Obstacle XML wrong type");
        }
        return obstacle;
    }
}