package HERMES.Selector.GA;

import HERMES.Exceptions.NoSuchFeatureException;
import HERMES.FeatureManager;
import HERMES.Selector.RuleBasedHeuristicSelector;
import Utils.Files;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provides the methods to create and use distance-based heuristic selectors.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class DistanceBasedHeuristicSelector extends RuleBasedHeuristicSelector {

    private int[] hFrequency, rFrequency;
    private Rule rules[];
    protected List<double[]> decisionPoints;

    /**
     * Creates a new instance of <code>DistanceBasedHeuristicSelector</code>.
     * <p>
     * @param featureNames The names of the fValues used by this distance-based heuristic selector.
     * @param heuristicNames The names of the heuristics used by this distance-based heuristic selector.
     * @param input The input examples.
     * @param output The expected outputs of the examples provided.
     */
    public DistanceBasedHeuristicSelector(String[] featureNames, String[] heuristicNames, double[][] input, int[] output) {
        super(featureNames, heuristicNames);
        rules = new Rule[input.length];
        for (int i = 0; i < input.length; i++) {
            rules[i] = new Rule(input[i], output[i]);
        }
        /*
         * Initializes the variables for statistics.
         */
        hFrequency = new int[heuristicNames.length];
        rFrequency = new int[rules.length];
        decisionPoints = new LinkedList();
    }

    /**
     * Creates a new instance of <code>DistanceBasedHeuristicSelector</code>.
     * <p>
     * @param fileName The name of the file where this distance-based heuristic selector will be loaded
     * from.
     */
    public DistanceBasedHeuristicSelector(String fileName) {
        int j, expectedOutput;
        double[] fValues;
        String text;
        StringTokenizer tokens;
        Node node;
        NodeList featureList, heuristicList, ruleList;
        Element element;
        File file;
        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc;
        try {
            file = new File(fileName);
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            /*
             * Reads the fValues.
             */
            featureList = doc.getElementsByTagName("feature");
            this.features = new String[featureList.getLength()];
            for (int i = 0; i < featureList.getLength(); i++) {
                node = featureList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    this.features[i] = element.getAttribute("name");
                }
            }
            /*
             * Reads the heuristics.
             */
            heuristicList = doc.getElementsByTagName("heuristic");
            heuristics = new String[heuristicList.getLength()];
            for (int i = 0; i < heuristicList.getLength(); i++) {
                node = heuristicList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    heuristics[i] = element.getAttribute("name");
                }
            }
            /*
             * Reads the list of rules.
             */
            ruleList = doc.getElementsByTagName("rule");
            rules = new Rule[ruleList.getLength()];
            for (int i = 0; i < ruleList.getLength(); i++) {
                node = ruleList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    text = element.getAttribute("condition");
                    /*
                     * Reads the condition of this rule.
                     */
                    tokens = new StringTokenizer(text, " ");
                    fValues = new double[tokens.countTokens()];
                    j = 0;
                    while (tokens.hasMoreTokens()) {
                        fValues[j++] = Double.parseDouble(tokens.nextToken().trim());
                    }
                    text = element.getAttribute("action");
                    expectedOutput = Integer.parseInt(text);
                    rules[i] = new Rule(fValues, expectedOutput);
                }
            }
            /*
             * Initializes the variables for statistics.
             */
            hFrequency = new int[heuristics.length];
            rFrequency = new int[rules.length];
            decisionPoints = new LinkedList();
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException e) {
            System.err.println("Exception " + e.toString());
            System.exit(1);
        }
    }

    @Override
    public String getHeuristic(FeatureManager problem) {
        int index;
        double distance, minDistance;
        double[] state;        
        /*
         * Gets the current problem state.
         */
        state = new double[features.length];
        for (int i = 0; i < features.length; i++) {
            try {
                state[i] = problem.getFeature(features[i]);
            } catch (NoSuchFeatureException e) {
                System.out.println("Feature \'" + features[i] + "\' is not recognized by the problem domain.");
                System.out.println("The system will halt.");
                System.exit(1);
            }
        }
        /*
         * Selects a suitable heuristic.
         */
        index = -1;        
        minDistance = Double.MAX_VALUE;        
        for (int i = 0; i < rules.length; i++) {
            distance = rules[i].distance(state);
            if (distance < minDistance) {
                index = i;
                minDistance = distance;
            }
        }        
        // Hack!!!
        /*
        minDistance = Double.MIN_VALUE;        
        for (int i = 0; i < rules.length; i++) {
            distance = rules[i].distance(state);
            if (distance > minDistance) {
                index = i;
                minDistance = distance;
            }
        }
        */
        // End of hack!
        /*
         * Updates the statistics.
         */
        decisionPoints.add(state);
        hFrequency[rules[index].getActionId()]++;
        rFrequency[index]++;
        /*
         * Returns the selected heuristic.
         */
        return heuristics[rules[index].getActionId()];
    }

    @Override
    public void restart() {
        for (int i = 0; i < hFrequency.length; i++) {
            hFrequency[i] = 0;
        }
        for (int i = 0; i < rFrequency.length; i++) {
            rFrequency[i] = 0;
        }        
        decisionPoints = new LinkedList();
    }
    
    /**
     * Saves this distance-based heuristic selector to an XML file.
     * <p>
     * @param fileName The name of the file where this rule-based heuristic selector will be saved
     * to.
     */
    public void save(String fileName) {
        StringBuilder string;
        DecimalFormat format;
        string = new StringBuilder();
        format = new DecimalFormat("0.00000");
        string.append("<!--");
        string.append("=============================================\r\n");
        string.append("Feel free to add any comments in this section\r\n");
        string.append("=============================================\r\n");
        string.append("-->\r\n");
        string.append("<distance-based-heuristic-selector>\r\n");
        /*
         * Writes information about the fValues.
         */
        string.append("\t<features nbFeatures=\"").append(features.length).append("\">\r\n");
        for (String featureName : features) {
            string.append("\t\t<feature name=\"").append(featureName).append("\"/>\r\n");
        }
        string.append("\t</features>\r\n");
        /*
         * Writes information about the heuristics.
         */
        string.append("\t<heuristics nbHeuristics=\"").append(heuristics.length).append("\">\r\n");
        for (String heuristic : heuristics) {
            string.append("\t\t<heuristic name=\"").append(heuristic).append("\"/>\r\n");
        }
        string.append("\t</heuristics>\r\n");
        /*
         * Writes the examples and their expected outputs.
         */
        string.append("\t<rules nbRules=\"").append(rules.length).append("\">\r\n");
        for (Rule rule : rules) {
            /*
             * Writes the condition.
             */
            string.append("\t\t<rule condition=\"");
            for (double value : rule.getCondition()) {
                string.append(format.format(value)).append(" ");
            }
            string.deleteCharAt(string.length() - 1);
            /*
             * Writes the action.
             */
            string.append("\" action=\"").append(rule.getActionId()).append("\"/>\r\n");
        }
        string.append("\t</rules>\r\n");
        string.append("</distance-based-heuristic-selector>\r\n");
        /*
         * Saves the information.
         */
        Files.save(string.toString().trim(), fileName);
    }

    /**
     * Returns the frequency of use of each heuristic in this distance-based heuristic selector.
     * <p>
     * @return The frequency of use of each heuristic in this distance-based heuristic selector.
     */
    public int[] getHeuristicUse() {
        return hFrequency;
    }

    /**
     * Returns the frequency of use of each rule in this distance-based heuristic selector.
     * <p>
     * @return The frequency of use of each rule in this distance-based heuristic selector.
     */
    public int[] getRuleUse() {
        return rFrequency;
    }

    @Override
    public String toString() {
        StringBuilder string;
        string = new StringBuilder();
        string.append(super.toString()).append("\n");
        for (Rule rule : rules) {
            string.append(rule).append("\n");
        }
        return string.toString().trim();
    }

}
