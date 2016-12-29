package ANN;

import Utils.Fraction;
import Utils.ProcessData;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by bianca on 29.12.2016.
 */
public class Controller {
    private final Double normalizationRate;
    private Fraction input[][], output[][];
    private Fraction inputTest[][], outputTest[][];
    private int noOfExample, noOfExampleTest;
    private int noOfOuputs, noOfOuputsTest;
    private int noOfFeatures, noOfFeaturesTest;
    private int noEpoch;
    private Fraction epsilon;
    private ProcessData processData;

    public Controller(int noEpoch, Fraction epsilon) throws IOException, ParseException {
        //read data
        processData = new ProcessData("src/resources/data.txt");
        processData.readInputData();
        this.noEpoch = noEpoch;
        this.epsilon = epsilon;
        this.input = processData.getInput();
        this.output = processData.getOutput();
        this.noOfExample = processData.getNoOfExample();
        this.noOfFeatures = processData.getNoOfFeatures();
        this.noOfOuputs = processData.getNoOfOuputs();
        this.normalizationRate = processData.getBiggestNumber();
        //read data for test
        processData = new ProcessData("src/resources/dataTest.txt");
        processData.readInputData();
        this.inputTest = processData.getInput();
        this.outputTest = processData.getOutput();
        this.noOfExampleTest = processData.getNoOfExample();
        this.noOfFeaturesTest = processData.getNoOfFeatures();
        this.noOfOuputsTest = processData.getNoOfOuputs();
    }

    public Layer getOutput() throws IOException, ParseException {
        int noOfHidden = 3;
        int noOfNeuronsPerLayer = 3;
        Network network = new Network(noOfFeatures, noOfOuputs, noOfHidden, noOfNeuronsPerLayer, epsilon, noEpoch);
        network.learn(input, output);
//        network.test(inputTest, outputTest);
        //pt datele introduse de user
        Fraction userInput[];
        processData = new ProcessData("src/resources/userData.txt");
        processData.readInputData();
        userInput = processData.getInput()[0];
        network.activate(userInput);
        Layer layer = network.getLayer(noOfHidden+1);
        return layer;
    }
}
