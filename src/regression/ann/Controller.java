package regression.ann;

import common.Fraction;
import common.Layer;
import regression.utils.ProcessDataRegr;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by bianca on 29.12.2016.
 */
public class Controller {
    private final Double normalizationRate;
    private final String stock;
    private Fraction input[][], output[][];
    private Fraction inputTest[][], outputTest[][];
    private int noOfExample, noOfExampleTest;
    private int noOfOuputs, noOfOuputsTest;
    private int noOfFeatures, noOfFeaturesTest;
    private int noEpoch;
    private Double epsilon;
    private ProcessDataRegr processData;
    private Double learningRate;
    private Double limitSEE;
    private boolean adaptiveLR;
    private Double alpha;
    private int noOfHidden;
    private int noOfNeuronsPerLayer;

    public Controller(String stock, int noEpoch, Double epsilon, Double learningRate, Double limitSEE, boolean adaptiveLR, Double alpha, int noHidden, int noNeurPerHidden) throws IOException, ParseException {
        this.noEpoch = noEpoch;
        this.epsilon = epsilon;
        this.learningRate = learningRate;
        this.limitSEE = limitSEE;
        this.adaptiveLR = adaptiveLR;
        this.alpha =alpha;
        this.stock = stock;
        this.noOfNeuronsPerLayer = noNeurPerHidden;
        this.noOfHidden = noHidden;

        //read data
        processData = new ProcessDataRegr("src/resources/"+stock+"/"+stock+".txt");
        processData.readInputData();
        this.input = processData.getInput();
        this.output = processData.getOutput();
        this.noOfExample = processData.getNoOfExample();
        this.noOfFeatures = processData.getNoOfFeatures();
        this.noOfOuputs = processData.getNoOfOuputs();
        this.normalizationRate = processData.getBiggestNumber();
        this.limitSEE = Math.sqrt(processData.getAverageOutput()*10)+30;
        System.out.println("LimitSEE "+this.limitSEE);

        //read data for test
        processData = new ProcessDataRegr("src/resources/"+stock+"/"+stock+"Test.txt");
        processData.readInputData();
        this.inputTest = processData.getInput();
        this.outputTest = processData.getOutput();
        this.noOfExampleTest = processData.getNoOfExample();
        this.noOfFeaturesTest = processData.getNoOfFeatures();
        this.noOfOuputsTest = processData.getNoOfOuputs();
    }

    public Layer getOutput() throws IOException, ParseException {
        Network network = new Network(noOfFeatures, noOfOuputs, noOfHidden, noOfNeuronsPerLayer,
                epsilon, noEpoch, learningRate, adaptiveLR, limitSEE, alpha);
        network.learn(input, output);
//        network.test(inputTest, outputTest);
        //pt datele introduse de user
        Fraction userInput[];
        processData = new ProcessDataRegr("src/resources/"+stock+"/"+stock+"Validation.txt");
        processData.readInputData();
        Fraction[][] validationInput = processData.getInput();
        Fraction[][] validationOutput = processData.getOutput();
        network.validate(validationInput, validationOutput);
        processData = new ProcessDataRegr("src/resources/"+stock+"/"+stock+"UserData.txt");
        processData.readInputData();
        userInput = processData.getInput()[0];
        network.activate(userInput);
        Layer layer = network.getLayer(noOfHidden+1);
        return layer;
    }
}
