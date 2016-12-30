package regression.ann;

import common.Fraction;
import common.Layer;
import common.Neuron;

/**
 * Created by bianca on 29.12.2016.
 */
public class Network {
    private final Double limitSEE;
    private final boolean adaptiveLR;
    private int noOfInputs;
    private int noOfOutput;
    private int noOfHiddenLayers;
    private Layer layers[];
    private Double epsilon;
    private int noEpoch = 100;
    private Fraction learningRate =new Fraction().valueOf(0.4);
    Fraction alpha=new Fraction().valueOf(0.05);

    public Network(int noOfFeatures, int noOfOuputs, int noOfHidden, int noOfNeuronsPerLayer, Double epsilon,
                   int noEpoch, double learningRate, boolean adaptiveLR, Double limitSEE, double alpha) {
        this.learningRate = new Fraction().valueOf(learningRate);
        this.adaptiveLR = adaptiveLR;
        this.limitSEE = limitSEE;
        this.noOfInputs = noOfFeatures;
        this.noOfOutput = noOfOuputs;
        this.noOfHiddenLayers = noOfHidden;
        this.epsilon = epsilon;
        this.noEpoch = noEpoch;
        this.alpha = new Fraction().valueOf(alpha);
        int noOfNeuronLayer = noOfNeuronsPerLayer;
        layers = new Layer[noOfNeuronLayer + 2];
        layers[0] = new Layer(noOfInputs, 0);
        layers[1] = new Layer(noOfNeuronLayer, noOfInputs);
        for (int i = 2; i < noOfHiddenLayers + 1; i++) {
            layers[i] = new Layer(noOfNeuronLayer, noOfNeuronLayer);
        }
        layers[noOfHiddenLayers + 1] = new Layer(noOfOutput, noOfNeuronLayer);
        //printNetwork();
    }

    private void printNetwork() {
        System.out.println("Print network");
        for (int i = 0; i < noOfHiddenLayers + 2; i++) {
            System.out.println("LAYER " + i + " :");
            if (i < 4) {
                for (Neuron n : layers[i].getNeurons()) {
                    System.out.println("Output: " + n.getOutput());
                    System.out.print("Weights: ");
                    for (Fraction w : n.getWeights()) {
                        System.out.print(w.toDouble() + ", ");
                    }
                    System.out.println();
                }
            }
        }
    }

    public void learn(Fraction[][] input, Fraction[][] output) {
        boolean stopCondition = false;
        int epoch = 0;
        Fraction[] globalError = new Fraction[input.length];
        Double previousSEE = 1000.0;
        while ((!stopCondition) && (epoch < this.noEpoch)){
            System.out.print("EPOCH: "+epoch);
            for (int inputNo = 0; inputNo < input.length; inputNo++){
                activate(input[inputNo]);
                Fraction[] err = new Fraction[output.length];
                globalError[inputNo] = errorComputationRegression(output[inputNo], err);
                errorsBackpropagation(err);
            }
            Double SEE = computeSEE(globalError);
            System.out.println(" SEE: "+SEE+" Learning Rate: "+learningRate);
            stopCondition = checkGlobarErr(SEE, previousSEE);
            previousSEE = SEE;
            epoch++;
        }
    }

    private Double computeSEE(Fraction[] globalError) {
        Fraction see = new Fraction();
//        System.out.print(" "+globalError[0].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))
//                +" "+globalError[1].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))
//                +" "+globalError[2].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))+" ");
        for (Fraction err: globalError){
            see = see.add(err.recalibreaza().recalibreaza());
        }
        see = see.div(new Fraction().valueOf(globalError.length));
        double err = Math.sqrt(see.toDouble());
        if (this.adaptiveLR) {
            adjustLearningRate(err);
        }
        return err;
    }

    private void adjustLearningRate(Double err) {
        if(err<this.limitSEE) {
            learningRate = new Fraction().valueOf(Math.log((this.limitSEE+20)-err)).div(new Fraction().valueOf(1.1*Math.exp(1)));
        }
    }

    private boolean checkGlobarErr(Double SEE, Double previousSEE) {
        if (SEE > epsilon){
            return false;
        }
        return true;
    }

    private void errorsBackpropagation(Fraction[] err) {
        //de la sfarsit spre inceput
        for (int currentLayerNo = noOfHiddenLayers+1; currentLayerNo >=1; currentLayerNo--){
            int i =0;
            for( Neuron n1: layers[currentLayerNo].getNeurons()){
                if (currentLayerNo == noOfHiddenLayers+1){
                    n1.setErrorSoftPlus(err[i]);
                }else{
                    Fraction sumError = new Fraction();
                    for(Neuron n2: layers[currentLayerNo+1].getNeurons()){
                        sumError = sumError.add(n2.getWeight(i).mul(n2.getError()));
                    }
                    n1.setErrorSigmoidal(sumError);
                }
                Fraction lastWeightUpdate = new Fraction();
                for (int j =0; j < n1.getNoInputs(); j++){
                    Fraction netWeight = new Fraction();
                    Fraction increment = learningRate.mul(n1.getError().mul(layers[currentLayerNo-1].getNeuron(j).getOutput()));
                    Fraction momentum = alpha.mul(lastWeightUpdate);
                    netWeight = n1.getWeight(j).add(increment);
                    netWeight = netWeight.add(momentum);
                    //System.out.println("WEIGHTS "+n1.getWeight(j)+" "+netWeight+" "+momentum);
                    lastWeightUpdate= increment.add(momentum);
                    n1.setWeight(j, netWeight);
                }
                i++;
            }
        }
    }

    private Fraction errorComputationRegression(Fraction[] target, Fraction[] err) {
        Fraction globalError = new Fraction();
        for (int i=0; i< layers[noOfHiddenLayers+1].getNoNeurons();i++){
            err[i] = target[i].sub(layers[noOfHiddenLayers+1].getNeuron(i).getOutput());
//            System.out.println("Target "+target[i].recalibreaza()+" output "+
//                    layers[noOfHiddenLayers+1].getNeuron(i).getOutput().recalibreaza()+ " error "+err[i].recalibreaza());
            globalError = globalError.add(err[i].mul(err[i]));
        }
        return globalError;
    }

    public void activate(Fraction[] input) {
        int i = 0;
        //layer intrare
        for (Neuron neuron : layers[0].getNeurons()) {
            neuron.setOutput(input[i]);
        }
        //restul layerelor
        for (int noCurrentLayer = 1; noCurrentLayer <= noOfHiddenLayers + 1; noCurrentLayer++) {
            for (Neuron neuron: layers[noCurrentLayer].getNeurons()){
                Fraction[] info = new Fraction[neuron.getNoOfInputs()];
                for (int inputNo=0; inputNo < neuron.getNoOfInputs(); inputNo++){
                    info[inputNo] = layers[noCurrentLayer-1].getNeuron(inputNo).getOutput();
                }
                neuron.fireSigmoidal(info);
            }
        }
    }

    public Layer getLayer(int i) {
        return layers[i];
    }

    public void validate(Fraction[][] validationInput, Fraction[][] validationOutput) {
        System.out.println("________________________VALIDATION________________________");
        Double totalErr =0.0;
        for (int i = 0; i< validationInput.length; i++){
            activate(validationInput[i]);
            Layer layer = this.getLayer(noOfHiddenLayers+1);
            Fraction error = validationOutput[i][0].recalibreaza().sub(layer.getNeuron(0).getOutput().recalibreaza());
            System.out.println("Validation set: "+i+" expected output: "+ validationOutput[i][0].recalibreaza()+
                                    " - actual output: "+layer.getNeuron(0).getOutput().recalibreaza()
                                    +" - error "+ error);
            totalErr = totalErr+Math.abs(error.toDouble());
        }
        System.out.println("Average error: "+totalErr/validationInput.length);
    }
}
