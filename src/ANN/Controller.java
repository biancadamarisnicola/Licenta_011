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
    private Double epsilon;
    private ProcessData processData;
    private Double learningRate;
    private Integer limitSEE;
    private boolean adaptiveLR;
    private Double alpha;

    public Controller(int noEpoch, Double epsilon, Double learningRate, Integer limitSEE, boolean adaptiveLR, Double alpha) throws IOException, ParseException {
        this.noEpoch = noEpoch;
        this.epsilon = epsilon;
        this.learningRate = learningRate;
        this.limitSEE = limitSEE;
        this.adaptiveLR = adaptiveLR;
        this.alpha =alpha;

        //read data
        processData = new ProcessData("src/resources/data.txt");
        processData.readInputData();
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
        int noOfHidden = 2;
        int noOfNeuronsPerLayer = 3;
        Network network = new Network(noOfFeatures, noOfOuputs, noOfHidden, noOfNeuronsPerLayer,
                epsilon, noEpoch, learningRate, adaptiveLR, limitSEE, alpha);
        network.learn(input, output);
//        network.test(inputTest, outputTest);
        //pt datele introduse de user
        Fraction userInput[];
        processData = new ProcessData("src/resources/validation.txt");
        processData.readInputData();
        Fraction[][] validationInput = processData.getInput();
        Fraction[][] validationOutput = processData.getOutput();
        network.validate(validationInput, validationOutput);
        processData = new ProcessData("src/resources/userData.txt");
        processData.readInputData();
        userInput = processData.getInput()[0];
        network.activate(userInput);
        Layer layer = network.getLayer(noOfHidden+1);
        return layer;
    }
}
